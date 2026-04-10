package se.mickelus.mutil.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PacketHandler {
    private static final Logger logger = LogManager.getLogger();

    private final String namespace;
    private final String channelId;
    private final String protocolVersion;
    private final List<PacketRegistration<? extends AbstractPacket>> packets = new ArrayList<>();
    private final Map<Class<? extends AbstractPacket>, PacketRegistration<? extends AbstractPacket>> packetsByClass = new HashMap<>();
    private boolean payloadsRegistered;

    public enum PacketDirection {
        CLIENTBOUND,
        SERVERBOUND,
        BIDIRECTIONAL
    }

    public PacketHandler(String namespace, String channelId, String protocolVersion) {
        this.namespace = namespace;
        this.channelId = channelId;
        this.protocolVersion = protocolVersion;
    }

    /**
     * Register a packet type so it can later be exposed as a NeoForge payload.
     *
     * @param packetClass the class to register
     * @param supplier A supplier returning an object instance of packetClass
     *
     * @return whether registration was successful. Failure may occur if 256 packets have been registered or if the registry already contains this packet
     */
    public <T extends AbstractPacket> boolean registerPacket(Class<T> packetClass, Supplier<T> supplier) {
        return registerPacket(packetClass, supplier, PacketDirection.BIDIRECTIONAL);
    }

    public <T extends AbstractPacket> boolean registerClientBoundPacket(Class<T> packetClass, Supplier<T> supplier) {
        return registerPacket(packetClass, supplier, PacketDirection.CLIENTBOUND);
    }

    public <T extends AbstractPacket> boolean registerServerBoundPacket(Class<T> packetClass, Supplier<T> supplier) {
        return registerPacket(packetClass, supplier, PacketDirection.SERVERBOUND);
    }

    private <T extends AbstractPacket> boolean registerPacket(Class<T> packetClass, Supplier<T> supplier, PacketDirection direction) {
        if (payloadsRegistered) {
            logger.warn("Attempted to register packet after payload registration phase: {}", packetClass);
            return false;
        }

        if (packets.size() > 256) {
            logger.warn("Attempted to register packet but packet list is full: {}", packetClass);
            return false;
        }

        if (packetsByClass.containsKey(packetClass)) {
            logger.warn("Attempted to register packet but packet is already in list: {}", packetClass);
            return false;
        }

        PacketRegistration<T> registration = new PacketRegistration<>(packetClass, supplier, getPacketId(packetClass), direction);
        packets.add(registration);
        packetsByClass.put(packetClass, registration);
        return true;
    }

    public void registerPayloads(RegisterPayloadHandlersEvent event) {
        if (payloadsRegistered) {
            return;
        }

        PayloadRegistrar registrar = event.registrar(protocolVersion);
        for (PacketRegistration<? extends AbstractPacket> packet : packets) {
            registerPayload(registrar, packet);
        }

        payloadsRegistered = true;
    }

    public void sendTo(AbstractPacket message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, createPayload(message));
    }

    public void sendToAllPlayers(AbstractPacket message) {
        PacketDistributor.sendToAllPlayers(createPayload(message));
    }

    public void sendToAllPlayersNear(AbstractPacket message, BlockPos pos, double radius, ResourceKey<Level> dimension) {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().getLevel(dimension);
        if (level != null) {
            PacketDistributor.sendToPlayersNear(level, null, pos.getX(), pos.getY(), pos.getZ(), radius, createPayload(message));
        }
    }

    public void sendToServer(AbstractPacket message) {
        // crashes sometimes happen due to the connection being null
        if (Minecraft.getInstance().getConnection() != null) {
            PacketDistributor.sendToServer(createPayload(message));
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractPacket> PacketPayload<T> createPayload(T message) {
        PacketRegistration<T> registration = (PacketRegistration<T>) packetsByClass.get(message.getClass());
        if (registration == null) {
            throw new IllegalStateException("Attempted to send unregistered packet: " + message.getClass().getName());
        }

        return new PacketPayload<>(registration, message);
    }

    private <T extends AbstractPacket> void registerPayload(PayloadRegistrar registrar, PacketRegistration<T> registration) {
        switch (registration.direction) {
            case CLIENTBOUND -> registrar.playToClient(registration.type, registration.codec,
                    (payload, context) -> payload.packet.handle(context.player()));
            case SERVERBOUND -> registrar.playToServer(registration.type, registration.codec,
                    (payload, context) -> payload.packet.handle(context.player()));
            case BIDIRECTIONAL -> registrar.playBidirectional(registration.type, registration.codec,
                    (payload, context) -> payload.packet.handle(context.player()));
        }
    }

    private ResourceLocation getPacketId(Class<? extends AbstractPacket> packetClass) {
        return ResourceLocation.fromNamespaceAndPath(
                namespace,
                channelId + "/" + packetClass.getName()
                        .replace('.', '/')
                        .replace('$', '/')
                        .toLowerCase(Locale.ROOT)
        );
    }

    private static class PacketPayload<T extends AbstractPacket> implements CustomPacketPayload {
        private final PacketRegistration<T> registration;
        private final T packet;

        private PacketPayload(PacketRegistration<T> registration, T packet) {
            this.registration = registration;
            this.packet = packet;
        }

        @Override
        public Type<PacketPayload<T>> type() {
            return registration.type;
        }
    }

    private static class PacketRegistration<T extends AbstractPacket> {
        private final CustomPacketPayload.Type<PacketPayload<T>> type;
        private final StreamCodec<RegistryFriendlyByteBuf, PacketPayload<T>> codec;
        private final PacketDirection direction;

        private PacketRegistration(Class<T> packetClass, Supplier<T> supplier, ResourceLocation id, PacketDirection direction) {
            type = new CustomPacketPayload.Type<>(id);
            this.direction = direction;
            codec = StreamCodec.of(
                    (buffer, payload) -> payload.packet.toBytes(buffer),
                    buffer -> {
                        T packet = supplier.get();
                        packet.fromBytes(buffer);
                        return new PacketPayload<>(this, packet);
                    }
            );
        }
    }
}

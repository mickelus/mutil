package se.mickelus.mutil.network;

import java.util.HashMap;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor.TargetPoint;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {
    private static final Logger logger = LogManager.getLogger();

    private final HashMap<ResourceLocation, Supplier<? extends AbstractPacket>> packets = new HashMap<>();
    
    private String modid;
    private String version;

	public PacketHandler(String modid, String version) {
		this.modid = modid;
		this.version = version;
	}
	
	public void register(final RegisterPayloadHandlerEvent event) {
		IPayloadRegistrar registrar = event.registrar(modid).versioned(version).optional();
		for (ResourceLocation id : packets.keySet()) {
			registrar.play(id, buf -> {
				AbstractPacket packet = packets.get(id).get();
				packet.fromBytes(buf);
				return packet;
			}, this::onMessage);
		}
	}
	
	public void onMessage(AbstractPacket message, PlayPayloadContext ctx) {
//      ctx.enqueueWork(() -> {
//          if (ctx.getDirection().getReceptionSide().isServer()) {
//              message.handle(ctx.getSender());
//          } else {
//              message.handle(getClientPlayer());
//          }
//      });
//      ctx.setPacketHandled(true);
		ctx.workHandler().submitAsync(() -> {
			if (ctx.flow().getReceptionSide().isServer()) {
				message.handle(ctx.player().get());
			} else {
				message.handle(getClientPlayer());
			}
		});
  }
	
	/**
     * Register your packet with the pipeline. Discriminators are automatically set.
     *
     * @param packetClass the class to register
     * @param supplier A supplier returning an object instance of packetClass
     *
     * @return whether registration was successful. Failure may occur if 256 packets have been registered or if the registry already contains this packet
     */
    public <T extends AbstractPacket> boolean registerPacket(ResourceLocation id, Supplier<T> packet) {
    	packets.put(id, packet);
    	return false; //TODO
    }
    @OnlyIn(Dist.CLIENT)
    private Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public void sendTo(AbstractPacket message, ServerPlayer player) {
    	PacketDistributor.PLAYER.with(player).send(message);
    }

    public void sendToAllPlayers(AbstractPacket message) {
    	PacketDistributor.ALL.with(null).send(message);
    }

    public void sendToAllPlayersNear(AbstractPacket message, BlockPos pos, double r2, ResourceKey<Level> dim) {
    	PacketDistributor.NEAR.with(TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), r2, dim).get()).send(message);
    }

    @OnlyIn(Dist.CLIENT)
    public void sendToServer(AbstractPacket message) {
    	PacketDistributor.SERVER.with(null).send(message);
    }
}

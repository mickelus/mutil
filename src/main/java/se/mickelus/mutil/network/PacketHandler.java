package se.mickelus.mutil.network;

import java.util.HashMap;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
	private static final Logger logger = LogManager.getLogger();

	private final HashMap<ResourceLocation, Supplier<AbstractPacket>> packets = new HashMap<>();

	private String modid;
	private String version;

	private @Nullable PayloadRegistrar registrar = null;

	public PacketHandler(String modid, String version) {
		this.modid = modid;
		this.version = version;
	}

	public void beginRegistration(final RegisterPayloadHandlersEvent event) {
		this.registrar = event.registrar(modid).versioned(version).optional();
	}

	public void endRegistration() {
		this.registrar = null;
	}

	public <T extends AbstractPacket> void onMessage(T message, IPayloadContext ctx) {
//      ctx.enqueueWork(() -> {
//          if (ctx.getDirection().getReceptionSide().isServer()) {
//              message.handle(ctx.getSender());
//          } else {
//              message.handle(getClientPlayer());
//          }
//      });
//      ctx.setPacketHandled(true);
		ctx.enqueueWork(() -> {
			if (ctx.flow().getReceptionSide().isServer()) {
				message.handle(ctx.player());
			} else {
				message.handle(getClientPlayer());
			}
		});
	}

	/**
	 * Register your packet with the pipeline. Discriminators are automatically set.
	 * <p>
	 * Must be called in the context of
	 * {@link net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent}.
	 *
	 * @param packetClass the class to register
	 * @param supplier    A supplier returning an object instance of packetClass
	 *
	 * @return whether registration was successful. Failure may occur if 256 packets
	 *         have been registered or if the registry already contains this packet
	 */
	public <T extends AbstractPacket> boolean registerPacket(ResourceLocation id, CustomPacketPayload.Type<T> type,
			StreamCodec<FriendlyByteBuf, T> codec, Supplier<T> packet) {
		if (this.registrar == null) {
			logger.warn("Attempted to register packet outside registration event: " + id.toString());
			return false;
		} else {
			this.registrar.playBidirectional(type, codec, (payload, context) -> {
				if (payload instanceof AbstractPacket) {
					this.onMessage((AbstractPacket)payload, context);
				}
			});
		}
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	private Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	public void sendTo(AbstractPacket message, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, message);
	}

	public void sendToAllPlayers(AbstractPacket message) {
		PacketDistributor.sendToAllPlayers(message);
	}

	public void sendToAllPlayersNear(AbstractPacket message, BlockPos pos, double r2, ServerLevel dim) {
		PacketDistributor.sendToPlayersNear(dim, null, pos.getX(), pos.getY(), pos.getZ(), r2, message);
	}

	@OnlyIn(Dist.CLIENT)
	public void sendToServer(AbstractPacket message) {
		PacketDistributor.sendToServer(message);
	}
}

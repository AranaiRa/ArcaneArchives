package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.tilenetwork.NetworkAggregator;
import com.aranaira.arcanearchives.tilenetwork.NetworkEntry;
import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

public class Handlers {
  public interface BaseHandler<T extends IMessage> extends IMessageHandler<T, IMessage> {
    void processMessage(T message, MessageContext ctx);
  }

  public interface ServerHandler<T extends IMessage> extends BaseHandler<T> {
    @Override
    default IMessage onMessage(T message, MessageContext ctx) {
      FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public interface TileHandlerServer<T extends Messages.TileMessage, V extends NetworkedBaseTile> extends ServerHandler<T> {
    @Override
    default void processMessage(T message, MessageContext ctx) {
      processMessage(message, ctx, getTile(message, ctx));
    }

    default V getTile(T message, MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().player;
      if (player == null) {
        return null;
      }

      UUID networkId = message.getNetworkId();

      Network network = NetworkAggregator.byId(networkId);

      if (network == null) {
        return null;
      }

      NetworkEntry entry = network.getEntryByTileId(message.getTileId());

      if (entry == null) {
        return null;
      }

      return entry.getTile();
    }

    void processMessage(T message, MessageContext ctx, V tile);
  }

  public interface ClientHandler<T extends IMessage> extends BaseHandler<T> {
    @Override
    default IMessage onMessage(T message, MessageContext ctx) {
      ArcaneArchives.proxy.scheduleTask(() -> processMessage(message, ctx), Side.CLIENT);

      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public interface TileHandlerClient<T extends Messages.TileMessage, V extends NetworkedBaseTile> extends ClientHandler<T> {
    @Override
    @SideOnly(Side.CLIENT)
    default void processMessage(T message, MessageContext ctx) {
      V tileEntity = getTile(message, ctx);
      if (tileEntity != null) {
        processMessage(message, ctx, tileEntity);
      } else {
        ArcaneArchives.logger.error("WARNING! Unable to handle client-side Tile packet due to invalid or missing tile entity.", new IllegalArgumentException());
      }
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    default V getTile(T message, MessageContext ctx) {
      Minecraft mc = Minecraft.getMinecraft();
      World world = mc.world;

      BlockPos pos = message.getPos();
      int dimension = message.getDimension();
      if (message.getPos() == null || dimension == -9999) {
        return null;
      }

      if (world.provider.getDimension() != dimension) {
        return null;
      }

      TileEntity te = world.getTileEntity(pos);
      if (te == null) {
        return null;
      }

      try {
        return (V) te;
      } catch (ClassCastException exception) {
        ArcaneArchives.logger.error("Attempted to cast to an invalid tile entity: " + te.getClass(), new IllegalArgumentException());
        return null;
      }
    }

    @SideOnly(Side.CLIENT)
    void processMessage(T message, MessageContext ctx, @Nullable V tile);
  }
}

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.api.cwb.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.containers.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
import com.aranaira.arcanearchives.network.Handlers.ServerHandler;
import com.aranaira.arcanearchives.network.Handlers.TileHandlerServer;
import com.aranaira.arcanearchives.network.Messages.TileMessage;
import com.aranaira.arcanearchives.registry.CrystalWorkbenchRegistry;
import com.aranaira.arcanearchives.tileentities.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.util.WorldUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class PacketCrystalWorkbench {
  public static class SetRecipeIndex extends TileMessage {
    private int recipeIndex = -1;

    public SetRecipeIndex() {
    }

    public SetRecipeIndex(UUID networkId, UUID tileId, int recipeIndex) {
      super(networkId, tileId);
      this.recipeIndex = recipeIndex;
    }

    public SetRecipeIndex(UUID networkId, BlockPos pos, int dimension, int recipeIndex) {
      super(networkId, pos, dimension);
      this.recipeIndex = recipeIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      super.fromBytes(buf);
      this.recipeIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
      super.toBytes(buf);
      buf.writeInt(this.recipeIndex);
    }

    public static class Handler implements TileHandlerServer<SetRecipeIndex, CrystalWorkbenchTile> {
      @Override
      public void processMessage(SetRecipeIndex message, MessageContext ctx, CrystalWorkbenchTile tile) {
        tile.setRecipe(message.recipeIndex);

        net.minecraft.inventory.container.Container container = ctx.getServerHandler().player.openContainer;
        if (container instanceof CrystalWorkbenchContainer) {
          ((CrystalWorkbenchContainer) container).updateRecipe();
        }
      }
    }
  }

  public static class ChangeRecipe implements IMessage {
    private ResourceLocation recipe;
    private BlockPos pos;
    private int dimension;

    @SuppressWarnings("unused")
    public ChangeRecipe() {
    }

    @SuppressWarnings("unused")
    public ChangeRecipe(ResourceLocation recipe, BlockPos pos, int dimension) {
      this.recipe = recipe;
      this.pos = pos;
      this.dimension = dimension;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      recipe = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
      pos = BlockPos.fromLong(buf.readLong());
      dimension = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, recipe.toString());
      buf.writeLong(pos.toLong());
      buf.writeInt(dimension);
    }

    public static class Handler implements ServerHandler<ChangeRecipe> {
      @Override
      public void processMessage(ChangeRecipe message, MessageContext ctx) {
        CrystalWorkbenchTile te = WorldUtil.getTileEntity(CrystalWorkbenchTile.class, message.dimension, message.pos);
        if (te != null) {
          te.setRecipe(message.recipe);
        }
      }
    }
  }

  public static class LastRecipe implements IMessage {
    private CrystalWorkbenchRecipe recipe;

    public LastRecipe() {
    }

    public LastRecipe(CrystalWorkbenchRecipe recipe) {
      this.recipe = recipe;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      recipe = CrystalWorkbenchRegistry.getRegistry().getValueByIndex(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
      buf.writeInt(recipe.getIndex());
    }

    public static class Handler implements ClientHandler<LastRecipe> {
      @Override
      public void processMessage(LastRecipe message, MessageContext ctx) {
        Container container = Minecraft.getMinecraft().player.openContainer;
        if (container instanceof CrystalWorkbenchContainer) {
          ((CrystalWorkbenchContainer) container).updateLastRecipeFromServer(message.recipe);
        }
      }
    }
  }
}

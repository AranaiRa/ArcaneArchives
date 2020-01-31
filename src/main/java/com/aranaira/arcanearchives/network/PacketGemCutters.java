/*package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.api.IGCTRecipe;
import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
import com.aranaira.arcanearchives.network.Handlers.ServerHandler;
import com.aranaira.arcanearchives.network.Handlers.TileHandlerServer;
import com.aranaira.arcanearchives.network.Messages.TileMessage;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class PacketGemCutters {
	public static class SetRecipeIndex extends TileMessage {
		private int recipeIndex = -1;

		public SetRecipeIndex () {
		}

		public SetRecipeIndex (UUID tileId, int recipeIndex) {
			super(tileId);
			this.recipeIndex = recipeIndex;
		}

		public SetRecipeIndex (BlockPos pos, int dimension, int recipeIndex) {
			super(pos, dimension);
			this.recipeIndex = recipeIndex;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			super.fromBytes(buf);
			this.recipeIndex = buf.readInt();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			super.toBytes(buf);
			buf.writeInt(this.recipeIndex);
		}

		public static class Handler implements TileHandlerServer<SetRecipeIndex, GemCuttersTableTileEntity> {
			@Override
			public void processMessage (SetRecipeIndex message, MessageContext ctx, GemCuttersTableTileEntity tile) {
				tile.setRecipe(message.recipeIndex);

				Container container = ctx.getServerHandler().player.openContainer;
				if (container instanceof ContainerGemCuttersTable) {
					((ContainerGemCuttersTable) container).updateRecipe();
				}
			}
		}
	}

	public static class ChangeRecipe implements IMessage {
		private ResourceLocation recipe;
		private BlockPos pos;
		private int dimension;

		@SuppressWarnings("unused")
		public ChangeRecipe () {
		}

		@SuppressWarnings("unused")
		public ChangeRecipe (ResourceLocation recipe, BlockPos pos, int dimension) {
			this.recipe = recipe;
			this.pos = pos;
			this.dimension = dimension;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			recipe = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
			pos = BlockPos.fromLong(buf.readLong());
			dimension = buf.readInt();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, recipe.toString());
			buf.writeLong(pos.toLong());
			buf.writeInt(dimension);
		}

		public static class Handler implements ServerHandler<ChangeRecipe> {
			@Override
			public void processMessage (ChangeRecipe message, MessageContext ctx) {
				GemCuttersTableTileEntity te = WorldUtil.getTileEntity(GemCuttersTableTileEntity.class, message.dimension, message.pos);
				if (te != null) {
					// Server-side call
					te.setRecipe(message.recipe);
				}
			}
		}
	}

	public static class LastRecipe implements IMessage {
		private IGCTRecipe recipe;

		public LastRecipe () {
		}

		public LastRecipe (IGCTRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			recipe = GCTRecipeList.instance.getRecipe(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
		}

		@Override
		public void toBytes (ByteBuf buf) {
			buf.writeInt(recipe.getIndex());
		}

		public static class Handler implements ClientHandler<LastRecipe> {
			@Override
			public void processMessage (LastRecipe message, MessageContext ctx) {
				Container container = Minecraft.getMinecraft().player.openContainer;
				if (container instanceof ContainerGemCuttersTable) {
					((ContainerGemCuttersTable) container).updateLastRecipeFromServer(message.recipe);
				}
			}
		}
	}
}*/

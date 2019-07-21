package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
import com.aranaira.arcanearchives.network.Handlers.ServerHandler;
import com.aranaira.arcanearchives.network.Handlers.TileHandlerServer;
import com.aranaira.arcanearchives.network.Messages.TileMessage;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import com.aranaira.arcanearchives.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketRadiantCrafting {
	public static class LastRecipe implements IMessage {
		private IRecipe recipe;

		public LastRecipe () {
		}

		public LastRecipe (IRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			recipe = ByteUtils.readIRecipe(buf);
		}

		@Override
		public void toBytes (ByteBuf buf) {
			ByteUtils.writeIRecipe(buf, recipe);
		}

		public static class Handler implements ClientHandler<LastRecipe> {
			@Override
			public void processMessage (LastRecipe message, MessageContext ctx) {
				Container container = Minecraft.getMinecraft().player.openContainer;
				if (container instanceof ContainerRadiantCraftingTable) {
					((ContainerRadiantCraftingTable) container).updateLastRecipeFromServer(message.recipe);
				}
			}
		}
	}

	public static class SetRecipe extends TileMessage {
		private int index;
		private IRecipe recipe;

		public SetRecipe () {
		}

		public SetRecipe (BlockPos pos, int dimension, int index, IRecipe recipe) {
			super(pos, dimension);
			this.index = index;
			this.recipe = recipe;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			super.fromBytes(buf);
			this.index = buf.readInt();
			this.recipe = ByteUtils.readIRecipe(buf);
		}

		@Override
		public void toBytes (ByteBuf buf) {
			super.toBytes(buf);
			buf.writeInt(this.index);
			ByteUtils.writeIRecipe(buf, this.recipe);
		}

		public static class Handler implements TileHandlerServer<SetRecipe> {
			@Override
			public void processMessage (SetRecipe message, MessageContext ctx, ImmanenceTileEntity tile) {
				if (tile instanceof RadiantCraftingTableTileEntity) {
					RadiantCraftingTableTileEntity rcte = (RadiantCraftingTableTileEntity) tile;
					rcte.setRecipe(message.index, message.recipe);
					rcte.markDirty();
					rcte.defaultServerSideUpdate();
				}
			}
		}
	}

	public static class UnsetRecipe extends TileMessage {
		private int index;

		public UnsetRecipe () {
		}

		public UnsetRecipe (BlockPos pos, int dimension, int index) {
			super(pos, dimension);
			this.index = index;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			super.fromBytes(buf);
			this.index = buf.readInt();
		}

		@Override
		public void toBytes (ByteBuf buf) {
			super.toBytes(buf);
			buf.writeInt(this.index);
		}

		public static class Handler implements TileHandlerServer<UnsetRecipe> {
			@Override
			public void processMessage (UnsetRecipe message, MessageContext ctx, ImmanenceTileEntity tile) {
				if (tile instanceof RadiantCraftingTableTileEntity) {
					RadiantCraftingTableTileEntity rcte = (RadiantCraftingTableTileEntity) tile;
					rcte.setRecipe(message.index, null);
					rcte.markDirty();
					rcte.defaultServerSideUpdate();
				}
			}
		}
	}

}

package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class PacketGemCutters
{
	public static class ChangeRecipe implements IMessage
	{
		private int recipe;
		private BlockPos pos;
		private int dimension;

		@SuppressWarnings("unused")
		public ChangeRecipe()
		{

		}

		@SuppressWarnings("unused")
		public ChangeRecipe(int recipe, BlockPos pos, int dimension)
		{
			this.recipe = recipe;
			this.pos = pos;
			this.dimension = dimension;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			recipe = buf.readInt();
			pos = BlockPos.fromLong(buf.readLong());
			dimension = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(recipe);
			buf.writeLong(pos.toLong());
			buf.writeInt(dimension);
		}

		public static class Handler extends NetworkHandler.ServerHandler<ChangeRecipe>
		{
			@Override
			public void processMessage(ChangeRecipe message, MessageContext ctx)
			{
				GemCuttersTableTileEntity te = WorldUtil.getTileEntity(GemCuttersTableTileEntity.class, message.dimension, message.pos);
				if(te != null)
				{
					// Server-side call
					te.setRecipe(message.recipe);
				}
			}
		}
	}

	public static class Consume implements IMessage
	{
		private UUID player;
		private BlockPos pos;
		private int dimension;

		@SuppressWarnings("unused")
		public Consume()
		{

		}

		@SuppressWarnings("unused")
		public Consume(EntityPlayer player, BlockPos pos, int dimension)
		{
			this.player = player.getPersistentID();
			this.pos = pos;
			this.dimension = dimension;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			this.player = UUID.fromString(ByteBufUtils.readUTF8String(buf));
			this.pos = BlockPos.fromLong(buf.readLong());
			this.dimension = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			ByteBufUtils.writeUTF8String(buf, player.toString());
			buf.writeLong(pos.toLong());
			buf.writeInt(dimension);
		}

		public static class Handler extends NetworkHandler.ServerHandler<Consume>
		{
			public void processMessage(Consume message, MessageContext ctx)
			{
				GemCuttersTableTileEntity te = WorldUtil.getTileEntity(GemCuttersTableTileEntity.class, message.dimension, message.pos);
				if(te != null)
				{
					te.consume(message.player);
				}
			}
		}
	}

	public static class LastRecipe implements IMessage
	{
		private GCTRecipe recipe;

		public LastRecipe()
		{
		}

		public LastRecipe(GCTRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			recipe = GCTRecipeList.getRecipeByIndex(buf.readInt());
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(recipe.getIndex());
		}

		public static class Handler extends NetworkHandler.ClientHandler<LastRecipe>
		{
			@Override
			public void processMessage(LastRecipe message, MessageContext ctx)
			{
				Container container = Minecraft.getMinecraft().player.openContainer;
				if(container instanceof ContainerGemCuttersTable)
				{
					((ContainerGemCuttersTable) container).updateLastRecipeFromServer(message.recipe);
				}
			}
		}
	}
}

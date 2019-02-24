package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class PacketGemCutters
{
	public static class ChangeRecipe implements IMessage
	{
		private ItemStack stack;
		private BlockPos pos;
		private int dimension;

		@SuppressWarnings("unused")
		public ChangeRecipe()
		{

		}

		@SuppressWarnings("unused")
		public ChangeRecipe(ItemStack stack, BlockPos pos, int dimension)
		{
			this.stack = stack;
			this.pos = pos;
			this.dimension = dimension;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			stack = ByteBufUtils.readItemStack(buf);
			pos = BlockPos.fromLong(buf.readLong());
			dimension = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			ByteBufUtils.writeItemStack(buf, stack);
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
					te.setRecipe(message.stack);
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
}

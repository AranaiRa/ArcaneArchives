package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
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

public class PacketGemCutters
{
	public static class ChangePage implements IMessage
	{
		private BlockPos mPos;
		private int page;
		private int dimension;

		public ChangePage()
		{
		}

		public ChangePage(BlockPos pos, int page, int dimension)
		{
			this.page = page;
			this.mPos = pos;
			this.dimension = dimension;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			this.page = buf.readInt();
			this.mPos = BlockPos.fromLong(buf.readLong());
			this.dimension = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(this.page);
			buf.writeLong(this.mPos.toLong());
			buf.writeInt(this.dimension);
		}

		public static class ChangePageHandler implements IMessageHandler<ChangePage, IMessage>
		{
			public IMessage onMessage(final ChangePage message, final MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			private void processMessage(ChangePage message, MessageContext ctx)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(server != null)
				{
					World world = DimensionManager.getWorld(message.dimension);
					TileEntity te = world.getTileEntity(message.mPos);
					if(te instanceof GemCuttersTableTileEntity)
					{
						((GemCuttersTableTileEntity) te).setPage(message.page);
					}
				}
			}
		}
	}

	public static class ChangeRecipe implements IMessage
	{
		private ItemStack stack;
		private BlockPos pos;
		private int dimension;

		public ChangeRecipe()
		{

		}

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

		public static class ChangeRecipeHandler implements IMessageHandler<ChangeRecipe, IMessage>
		{
			public IMessage onMessage(final ChangeRecipe message, final MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			private void processMessage(ChangeRecipe message, MessageContext ctx)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(server != null)
				{
					World world = DimensionManager.getWorld(message.dimension);
					TileEntity te = world.getTileEntity(message.pos);
					if(te instanceof GemCuttersTableTileEntity)
					{
						((GemCuttersTableTileEntity) te).setRecipe(message.stack);
					}
				}
			}
		}
	}

	public static class Consume implements IMessage
	{
		private UUID player;
		private BlockPos pos;
		private int dimension;

		public Consume()
		{

		}


		public Consume(EntityPlayer player, BlockPos pos, int dimension)
		{
			this.player = player.getPersistentID();
			this.pos = pos;
			this.dimension = dimension;
		}

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

		public static class ConsumeHandler implements IMessageHandler<Consume, IMessage>
		{
			public IMessage onMessage(final Consume message, final MessageContext ctx)
			{
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

				return null;
			}

			private void processMessage(Consume message, MessageContext ctx)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if(server != null)
				{
					World world = DimensionManager.getWorld(message.dimension);
					TileEntity te = world.getTileEntity(message.pos);
					if(te instanceof GemCuttersTableTileEntity)
					{
						((GemCuttersTableTileEntity) te).consume(message.player);
					}
				}
			}
		}
	}
}

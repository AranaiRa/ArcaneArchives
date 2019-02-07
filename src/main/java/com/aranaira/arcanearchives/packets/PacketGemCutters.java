package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
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

public class PacketsGemCutters
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

	public static class ChangeRecipe implements IMessage {
		private ItemStack stack;
		private BlockPos pos;
		private int dimension;

		public ChangeRecipe () {

		}

		public ChangeRecipe (ItemStack stack, BlockPos pos, int dimension) {
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
	}
}

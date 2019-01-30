package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.common.GCTItemHandler;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;

public class PacketGCTChangeRecipe implements IMessage
{
	private int mDimension;
	private BlockPos mPos;
	private ItemStack mRecipeItem;

	public PacketGCTChangeRecipe()
	{
	}

	public PacketGCTChangeRecipe(BlockPos pos, int dimension, ItemStack item)
	{
		mDimension = dimension;
		mRecipeItem = item;
		mPos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		mDimension = buf.readInt();
		mRecipeItem = ByteBufUtils.readItemStack(buf);
		mPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(mDimension);
		ByteBufUtils.writeItemStack(buf, mRecipeItem);
		buf.writeInt(mPos.getX());
		buf.writeInt(mPos.getY());
		buf.writeInt(mPos.getZ());
	}

	public static class PacketGCTChangeRecipeHandler implements IMessageHandler<PacketGCTChangeRecipe, IMessage>
	{
		public IMessage onMessage(final PacketGCTChangeRecipe message, final MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}

		private void processMessage(PacketGCTChangeRecipe message, MessageContext ctx)
		{
			if(Minecraft.getMinecraft().world.provider.getDimension() == message.mDimension)
			{
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.mPos);
				if(te instanceof GemCuttersTableTileEntity)
				{
					// Needs a null check on the capability
					((GCTItemHandler) te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setRecipe(message.mRecipeItem);
					;

					// Same as PAcketGCTChangePage

					te.markDirty();
					te.updateContainingBlockInfo();
				}
			}
		}
	}
}

package com.aranaira.arcanearchives.packets;

import java.nio.charset.Charset;

import com.aranaira.arcanearchives.common.GCTItemHandler;
import com.aranaira.arcanearchives.tileentities.GemcuttersTableTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;

public class PacketGCTChangePage implements IMessage {

	private int mDimension;
	private boolean mNext;
	private BlockPos mPos;
	
	public PacketGCTChangePage() {}
	
	public PacketGCTChangePage(BlockPos pos, int dimension, boolean next)
	{
		mDimension = dimension;
		mNext = next;
		mPos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		mDimension = buf.readInt();
		mNext = buf.readBoolean();
		mPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(mDimension);
		buf.writeBoolean(mNext);
		buf.writeInt(mPos.getX());
		buf.writeInt(mPos.getY());
		buf.writeInt(mPos.getZ());
	}

	public static class PacketGCTChangePageHandler implements IMessageHandler<PacketGCTChangePage, IMessage>
	{
		public IMessage onMessage(final PacketGCTChangePage message, final MessageContext ctx) 
		{
		    FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
	
		    return null;
		}
		
		private void processMessage(PacketGCTChangePage message, MessageContext ctx)
		{
			if (Minecraft.getMinecraft().world.provider.getDimension() == message.mDimension)
			{
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.mPos);
				if (te instanceof GemcuttersTableTileEntity)
			    {
					if (message.mNext)
						((GCTItemHandler)((GemcuttersTableTileEntity) te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).nextPage();
					else

						((GCTItemHandler)((GemcuttersTableTileEntity) te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).prevPage();
			    	((GemcuttersTableTileEntity) te).markDirty();
			    	te.updateContainingBlockInfo();
			    }
			}
		}
	}
}

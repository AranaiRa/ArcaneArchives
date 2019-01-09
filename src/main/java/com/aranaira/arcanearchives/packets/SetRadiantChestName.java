package com.aranaira.arcanearchives.packets;

import java.nio.charset.Charset;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetRadiantChestName implements IMessage {

	private BlockPos mPos;
	private String mName;
	private int mDimensionID;
	private UUID mPlayerID;
	
	public SetRadiantChestName() {}
	public SetRadiantChestName(BlockPos pos, String name, UUID uuid, int dimensionID)
	{
		mPos = pos;
		mName = name;
		mDimensionID = dimensionID;
		mPlayerID = uuid;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		mName = (String) buf.readCharSequence(buf.readInt(), Charset.defaultCharset());
		mPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
		mDimensionID = buf.readInt();
		
		mPlayerID = UUID.fromString((String) buf.readCharSequence(buf.readInt(), Charset.defaultCharset()));
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(mName.length());
		buf.writeCharSequence(mName, Charset.defaultCharset());
		
		buf.writeInt(mPos.getX());
		buf.writeInt(mPos.getY());
		buf.writeInt(mPos.getZ());
		
		buf.writeInt(mDimensionID);
		
		buf.writeInt(mPlayerID.toString().length());
		buf.writeCharSequence(mPlayerID.toString(), Charset.defaultCharset());
	}

	public static class SetRadiantChestNameHandler implements IMessageHandler<SetRadiantChestName, IMessage>
	{

		@Override 
		public IMessage onMessage(final SetRadiantChestName message, final MessageContext ctx) 
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

		    return null;
		}
		
		private void processMessage(SetRadiantChestName message, MessageContext ctx)
		{
		    EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		    TileEntity te = DimensionManager.getWorld(message.mDimensionID).getTileEntity(message.mPos);
		    if (te instanceof RadiantChestTileEntity)
		    {
		    	if (((RadiantChestTileEntity) te).NetworkID.compareTo(message.mPlayerID) == 0)
		    	{
		    		((RadiantChestTileEntity) te).mName = message.mName;
		    		((RadiantChestTileEntity) te).markDirty();
				    AAPacketHandler.CHANNEL.sendToAll(new PacketChangeRadiantChestNameClient(message.mPos, message.mDimensionID, message.mName));
		    	}
		    }

		}
	}
}

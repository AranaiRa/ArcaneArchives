package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.common.ManifestItemHandler;
import com.aranaira.arcanearchives.util.RadiantChestPlaceHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketRadiantChestsListResponse implements IMessage
{
	List<ItemStack> mItems;
	List<RadiantChestPlaceHolder> mChests;
	UUID mPlayerID;

	public PacketRadiantChestsListResponse()
	{
	}

	public PacketRadiantChestsListResponse(UUID playerId, List<ItemStack> items, List<RadiantChestPlaceHolder> chests)
	{
		mPlayerID = playerId;
		mItems = items;
		mChests = chests;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		mPlayerID.fromString((String) buf.readCharSequence(buf.readInt(), Charset.defaultCharset()));
		mChests = new ArrayList();

		mItems = new ArrayList();
		int i = buf.readInt();
		for(int j = 0; j < i; j++)
		{
			ItemStack s = ByteBufUtils.readItemStack(buf);
			s.setCount(buf.readInt());
			mItems.add(s);
		}
		i = buf.readInt();
		for(int j = 0; j < i; j++)
		{
			List<ItemStack> items = new ArrayList();
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			BlockPos pos = new BlockPos(x, y, z);

			int k = buf.readInt();
			for(int l = 0; l < k; l++)
			{
				ItemStack s = ByteBufUtils.readItemStack(buf);
				s.setCount(buf.readInt());
				items.add(s);
			}

			mChests.add(new RadiantChestPlaceHolder(pos, items));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(mPlayerID.toString().length());
		buf.writeCharSequence(mPlayerID.toString(), Charset.defaultCharset());
		buf.writeInt(mItems.size());
		for(ItemStack item : mItems)
		{
			ItemStack s = item.copy();
			s.setCount(1);
			ByteBufUtils.writeItemStack(buf, s);
			buf.writeInt(item.getCount());
		}
		buf.writeInt(mChests.size());
		for(RadiantChestPlaceHolder rcph : mChests)
		{
			buf.writeInt(rcph.mPos.getX());
			buf.writeInt(rcph.mPos.getY());
			buf.writeInt(rcph.mPos.getZ());
			buf.writeInt(rcph.mItems.size());

			for(ItemStack item : rcph.mItems)
			{
				ItemStack s = item.copy();
				s.setCount(1);
				ByteBufUtils.writeItemStack(buf, s);
				buf.writeInt(item.getCount());
			}
		}
	}

	public static class PacketRadiantChestsListResponseHandler implements IMessageHandler<PacketRadiantChestsListResponse, IMessage>
	{

		@Override
		public IMessage onMessage(final PacketRadiantChestsListResponse message, final MessageContext ctx)
		{
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}

		private void processMessage(PacketRadiantChestsListResponse message, MessageContext ctx)
		{
			//Bodgey Code
			//ManifestItemHandler mManifestItemHandler = NetworkHelper.getArcaneArchivesNetwork(message.mPlayerID).mManifestItemHandler;
			ManifestItemHandler.mInstance.Clear();

			for(ItemStack s : message.mItems)
			{
				ManifestItemHandler.mInstance.AddItemStack(s);
			}
			for(RadiantChestPlaceHolder rcte : message.mChests)
			{
				ManifestItemHandler.mInstance.mChests.add(rcte);
			}
			ManifestItemHandler.mInstance.SortChests();
		}
	}

}

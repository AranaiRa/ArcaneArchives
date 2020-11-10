//package com.aranaira.arcanearchives.network;
//
//import com.aranaira.arcanearchives.client.tracking.LineHandler;
//import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
//import io.netty.buffer.ByteBuf;
//import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PacketDebug {
//	public static class TrackPositions implements IMessage {
//		private List<BlockPos> positions = new ArrayList<>();
//		private int dimension;
//
//		public TrackPositions () {
//		}
//
//		public TrackPositions (List<BlockPos> positions, int dimension) {
//			this.positions = positions;
//			this.dimension = dimension;
//		}
//
//		@Override
//		public void fromBytes (ByteBuf buf) {
//			dimension = buf.readInt();
//			int count = buf.readInt();
//			positions.clear();
//			for (int i = 0; i < count; i++) {
//				positions.add(BlockPos.fromLong(buf.readLong()));
//			}
//		}
//
//		@Override
//		public void toBytes (ByteBuf buf) {
//			buf.writeInt(dimension);
//			buf.writeInt(positions.size());
//			for (BlockPos pos : positions) {
//				buf.writeLong(pos.toLong());
//			}
//		}
//
//		public static class Handler implements ClientHandler<TrackPositions> {
//
//			@Override
//			public void processMessage (TrackPositions message, MessageContext ctx) {
//				LineHandler.addLines(message.positions, message.dimension);
//			}
//		}
//	}
//}

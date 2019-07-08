package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.network.NetworkHandler.ClientHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class PacketClipboard {
	public static class CopyToClipboard implements IMessage {
		private String contents = "";

		public CopyToClipboard (String contents) {
			this.contents = contents;
		}

		public CopyToClipboard () {
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			this.contents = ByteBufUtils.readUTF8String(buf);
		}

		@Override
		public void toBytes (ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, this.contents);
		}

		public static class Handler implements ClientHandler<CopyToClipboard> {
			@Override
			public void processMessage (CopyToClipboard message, MessageContext ctx) {
				if (Desktop.isDesktopSupported()) {
					StringSelection select = new StringSelection(message.contents);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(select, null);
				}
			}
		}
	}
}

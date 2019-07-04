package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.network.NetworkHandler.EmptyMessageServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRadiantAmphora {
	public static class Toggle implements EmptyMessageServer<Toggle> {
		public Toggle () {
		}

		@Override
		public void processMessage (Toggle packet, MessageContext context) {
			EntityPlayerMP player = context.getServerHandler().player;
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem() == ItemRegistry.RADIANT_AMPHORA) {
				AmphoraUtil util = new AmphoraUtil(stack);
				util.toggleMode();
			}
		}
	}
}

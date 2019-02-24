package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRadiantCrafting
{
	public static class LastRecipe implements IMessage
	{
		private IRecipe recipe;

		public LastRecipe()
		{
		}

		public LastRecipe(IRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			recipe = CraftingManager.REGISTRY.getObjectById(buf.readInt());
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(CraftingManager.REGISTRY.getIDForObject(recipe));
		}

		public static class Handler extends NetworkHandler.ClientHandler<LastRecipe>
		{
			@Override
			public void processMessage(LastRecipe message, MessageContext ctx)
			{
				Container container = Minecraft.getMinecraft().player.openContainer;
				if(container instanceof ContainerRadiantCraftingTable)
				{
					((ContainerRadiantCraftingTable) container).updateLastRecipeFromServer(message.recipe);
				}
			}
		}
	}
}

package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity.CapabilityRef;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.List;

public class CommandBrazier extends CommandBase {
	public CommandBrazier () {
	}

	@Override
	public String getName () {
		return "brazier";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/brazier";
	}

	@Override
	public List<String> getAliases () {
		return Collections.singletonList("brazier");
	}

	@Override
	public int getRequiredPermissionLevel () {
		return 0;
	}

	@Override
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack item = player.getHeldItemMainhand();
			if (item.isEmpty()) {
				player.sendMessage(new TextComponentString("Can't trace an empty hand."));
				return;
			}
			ServerNetwork network = NetworkHelper.getServerNetwork(player.getUniqueID(), player.world);
			int ref = RecipeItemHelper.pack(item);
			player.sendMessage(new TextComponentString("Target is \"" + item.getDisplayName() + "\" packed to " + ref));
			List<CapabilityRef> caps = BrazierTileEntity.collectCapabilities(network, item);
			player.sendMessage(new TextComponentString("Total number of potential targets: " + caps.size()));
			int i = 1;
			for (CapabilityRef cap : caps) {
				if (cap.handler instanceof TroveItemHandler) {
					player.sendMessage(new TextComponentString("Cap #" + i + " is a trove containing " + ((TroveItemHandler) cap.handler).getCount() + " of " + ref));
				} else if (cap.tracking.contains(ref)) {
					player.sendMessage(new TextComponentString("Cap #" + i + " is a chest containing " + cap.tracking.quantity(ref) + " of " + ref));
				} else {
					player.sendMessage(new TextComponentString("Cap #" + i + " is just a chest."));
				}
				i++;
			}
		}
	}
}

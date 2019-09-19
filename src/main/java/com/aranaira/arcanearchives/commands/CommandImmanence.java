package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.api.immanence.IImmanenceBus;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.types.ServerNetwork;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.List;

public class CommandImmanence extends CommandBase {
	public CommandImmanence () {
	}

	@Override
	public String getName () {
		return "immanence";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/immanence";
	}

	@Override
	public List<String> getAliases () {
		return Collections.singletonList("immanence");
	}

	@Override
	public int getRequiredPermissionLevel () {
		return 0;
	}

	@Override
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ServerNetwork network = DataHelper.getServerNetwork(player.getUniqueID());
			if (network == null) {
				player.sendMessage(new TextComponentString("For some reason your network is null!"));
				return;
			}

			IImmanenceBus bus = network.getImmanenceBus();
			long diff = (System.currentTimeMillis() - bus.getLastTickTime()) / 1000;
			player.sendMessage(new TextComponentString("Current base immanence is " + bus.getBaseImmanence() + ", multiplier " + bus.getMultiplier() + " for a total of " + bus.getTotalImmanence() + ". The last tick was " + diff + " seconds ago, and " + bus.getLeftoverImmanence() + " of the total remains."));
		}
	}
}

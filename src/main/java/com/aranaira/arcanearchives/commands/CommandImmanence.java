package com.aranaira.arcanearchives.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

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
		}
	}
}

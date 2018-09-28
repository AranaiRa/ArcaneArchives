package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class ArcaneArchivesCommand extends CommandBase 
{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ArcaneArchives";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		//TODO : Add more commands and handle subcommands
		sender.sendMessage(new TextComponentString("UUID : " + sender.getCommandSenderEntity().getUniqueID().toString()));
		ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(sender.getCommandSenderEntity().getUniqueID());
		sender.sendMessage(new TextComponentString("Blocks on your network:"));
		for (BlockPos pos : network.getBlocks().keySet())
		{
			sender.sendMessage(new TextComponentString("Block : " + network.getBlocks().get(pos) + " @ " + pos.toString()));
		}
	}
}

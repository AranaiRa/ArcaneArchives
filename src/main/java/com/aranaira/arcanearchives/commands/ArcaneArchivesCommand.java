package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;

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
		sender.sendMessage(new TextComponentString("Net Immanence : " + network.GetImmanence()));
		sender.sendMessage(new TextComponentString("Blocks on your network:"));
		for (ImmanenceTileEntity ITS : network.getBlocks().keySet())
		{
			sender.sendMessage(new TextComponentString("Block : " + ITS.name + " @ " + ITS.blockpos.toString()));
			sender.sendMessage(new TextComponentString(">Immanence Drain : " + ITS.ImmanenceDrain));
			sender.sendMessage(new TextComponentString(">Immanence Gen   : " + ITS.ImmanenceGeneration));
			sender.sendMessage(new TextComponentString(">Immanence Paid  : " + ITS.IsDrainPaid));
		}
	}
}

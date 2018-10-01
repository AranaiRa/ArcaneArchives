package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.PasteBinHelper;

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
		if (args.length == 0)
		{
			sender.sendMessage(new TextComponentString("Type /AA help for available commands!"));
			return;
		}
		if (args[0].compareTo("network") == 0)
		{
			sender.sendMessage(new TextComponentString("UUID : " + sender.getCommandSenderEntity().getUniqueID().toString()));
			ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(sender.getCommandSenderEntity().getUniqueID());
			sender.sendMessage(new TextComponentString("Net Immanence : " + network.GetImmanence()));
			sender.sendMessage(new TextComponentString("Blocks on your network:"));
			for (ImmanenceTileEntity ITS : network.getBlocks().keySet())
			{
				sender.sendMessage(new TextComponentString("Block : " + ITS.name + " @ " + ITS.blockpos.toString() + " D: " + ITS.Dimension));
				sender.sendMessage(new TextComponentString(">Immanence Drain : " + ITS.ImmanenceDrain));
				sender.sendMessage(new TextComponentString(">Immanence Gen   : " + ITS.ImmanenceGeneration));
				sender.sendMessage(new TextComponentString(">Immanence Paid  : " + ITS.IsDrainPaid));
			}
		}
		else if (args[0].compareTo("structure") == 0)
		{
			if (args.length < 2)
			{
				sender.sendMessage(new TextComponentString("Valid Commands : import <pastebin-code>| export"));
				return;
			}
			if (args[1].compareTo("import") == 0)
			{
				if (args.length < 3)
				{
					sender.sendMessage(new TextComponentString("Command Usage : /AA structure import <pastebin-code>"));

				}
				else
				{
					String pastebin_link = args[2];
					String t = PasteBinHelper.ReadFromPasteBin(pastebin_link);
					sender.sendMessage(new TextComponentString(t));
				}
			}
			else if(args[1].compareTo("export") == 0)
			{
				String t = PasteBinHelper.PostToPasteBin("TEST");
				sender.sendMessage(new TextComponentString(t));
			}
			else
			{
				
			}
		}
	}
}

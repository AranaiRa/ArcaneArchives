package com.aranaira.arcanearchives.commands;

import java.util.List;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import scala.actors.threadpool.Arrays;

public class ArcaneArchivesCommand extends CommandBase 
{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ArcaneArchives";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/arcanearchives help";
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length == 0)
		{
			return Arrays.asList(new String[] {"network"});
		}
		else if (args.length == 1)
		{
			return Arrays.asList(new String[] {"invite", "accept"});
		}
		else if (args.length == 2)
		{
			if (args[1].compareTo("invite") == 0)
			{
				return Arrays.asList(server.getPlayerList().getOnlinePlayerNames());
			}
			else if (args[1].compareTo("accept") == 0)
			{
				return (List<String>) (NetworkHelper.getArcaneArchivesNetwork(sender.getCommandSenderEntity().getUniqueID()).pendingInvites.keySet());
			}
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0 && args[0].compareTo("network") == 0)
		{
			if (args.length > 1 && args[1].compareTo("invite") == 0)
			{
				if (args.length > 2 && args[2].compareTo(sender.getName()) != 0)
				{
					EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
					if (targetPlayer != null)
					{
						NetworkHelper.getArcaneArchivesNetwork(targetPlayer.getUniqueID()).Invite(sender.getName(), sender.getCommandSenderEntity().getUniqueID());
					}
				}
				else
				{
					sender.sendMessage(new TextComponentString("You cannot add yourself to your own network!"));
				}
			}
			else if (args.length > 1 && args[1].compareTo("accept") == 0)
			{
				if (NetworkHelper.getArcaneArchivesNetwork(sender.getCommandSenderEntity().getUniqueID()).Accept(args[2]))
				{
					sender.sendMessage(new TextComponentString("Joined " + args[2] + "'s Network!"));
				}
				else
				{
					sender.sendMessage(new TextComponentString("You have not received an invite to " + args[2] + "'s Network!"));
				}
			}
		}
		
	}
}

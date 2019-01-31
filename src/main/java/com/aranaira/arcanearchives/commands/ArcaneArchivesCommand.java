package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.data.NetworkHelper;
import com.google.common.collect.Lists;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ArcaneArchivesCommand extends CommandBase
{

	@Override
	public String getName()
	{
		return "ArcaneArchives";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/arcanearchives help";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nonnull BlockPos targetPos)
	{
		if(args.length == 0)
		{
			return Lists.newArrayList("network");
		} else if(args.length == 1)
		{
			return Lists.newArrayList("invite", "accept");
		} else if(args.length == 2)
		{
			if(args[1].compareTo("invite") == 0)
			{
				return Lists.newArrayList(server.getPlayerList().getOnlinePlayerNames());
			} else if(args[1].compareTo("accept") == 0 && sender.getCommandSenderEntity() != null)
			{
				Set<String> invites = NetworkHelper.getArcaneArchivesNetwork(sender.getCommandSenderEntity().getUniqueID()).pendingInvites.keySet();
				return new ArrayList<>(invites);
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity eSender = sender.getCommandSenderEntity();

		if(args.length > 0 && args[0].compareTo("network") == 0)
		{
			if(args.length > 1 && args[1].compareTo("invite") == 0)
			{
				if(args.length > 2 && args[2].compareTo(sender.getName()) != 0)
				{
					EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
					if(targetPlayer != null && eSender != null)
					{
						NetworkHelper.getArcaneArchivesNetwork(targetPlayer.getUniqueID()).Invite(sender.getName(), eSender.getUniqueID());
					}
				} else
				{
					sender.sendMessage(new TextComponentString("You cannot add yourself to your own network!"));
				}
			} else if(args.length > 1 && args[1].compareTo("accept") == 0)
			{
				if(eSender != null && NetworkHelper.getArcaneArchivesNetwork(eSender.getUniqueID()).Accept(args[2]))
				{
					sender.sendMessage(new TextComponentString("Joined " + args[2] + "'s Network!"));
				} else
				{
					sender.sendMessage(new TextComponentString("You have not received an invite to " + args[2] + "'s Network!"));
				}
			}
		}

	}
}

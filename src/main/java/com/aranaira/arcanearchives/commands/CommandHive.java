package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.HiveSaveData;
import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandHive extends CommandBase {
	public CommandHive () {
	}

	@Override
	public String getName () {
		return "hive";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/hive";
	}

	@Override
	public List<String> getAliases () {
		return Collections.singletonList("hive");
	}

	@Override
	public int getRequiredPermissionLevel () {
		return 0;
	}

	@Override
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			PlayerProfileCache cache = server.getPlayerProfileCache();
			EntityPlayer player = (EntityPlayer) sender;
			HiveSaveData saveData = DataHelper.getHiveData(player.world);
			Hive hive = saveData.getHiveByMember(player.getUniqueID());
			if (hive == null) {
				player.sendMessage(new TextComponentString("You are not in a hive."));
				return;
			}
			if (hive.owner.equals(player.getUniqueID())) {
				player.sendMessage(new TextComponentString("You own this hive network."));
			} else {
				GameProfile profile = cache.getProfileByUUID(hive.owner);
				if (profile == null) {
					player.sendMessage(new TextComponentString("Cannot resolve owner for hive network."));
				} else {
					player.sendMessage(new TextComponentString("You are in the Hive owned by: " + profile.getName()));
				}
			}
			player.sendMessage(new TextComponentString("UUID for network is: " + hive.owner.toString()));
			player.sendMessage(new TextComponentString("Hive has " + hive.members.size() + " members."));
			for (UUID member : hive.members) {
				GameProfile profile = cache.getProfileByUUID(member);
				String name = profile == null ? "Unknown name" : profile.getName();
				player.sendMessage(new TextComponentString("Hive member " + name + " and ID: " + member.toString()));
			}
		}
	}
}

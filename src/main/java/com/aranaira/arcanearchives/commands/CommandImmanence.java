package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.entity.EntityWeight;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.interfaces.IBrazierRouting;
import com.aranaira.arcanearchives.util.InventoryRoutingUtils;
import com.aranaira.arcanearchives.util.InventoryRoutingUtils.WeightedEntry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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

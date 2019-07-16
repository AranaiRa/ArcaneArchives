package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.IBrazierRouting;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import com.aranaira.arcanearchives.util.InventoryRouting;
import com.aranaira.arcanearchives.util.InventoryRouting.WeightedEntry;
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
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack item = player.getHeldItemMainhand();
			if (item.isEmpty()) {
				player.sendMessage(new TextComponentString("Can't trace an empty hand."));
				return;
			}
			BrazierTileEntity fake = new BrazierTileEntity(true);
			fake.setWorld(player.world);
			fake.setNetworkId(player.getUniqueID());
			fake.setPos(player.getPosition());
			List<WeightedEntry<IBrazierRouting>> weights = InventoryRouting.buildNetworkWeights(fake, item);
			player.sendMessage(new TextComponentString("Target is \"" + item.getTranslationKey() + "x" + item.getCount() + "\""));
			player.sendMessage(new TextComponentString("Total number of potential targets: " + weights.size()));
			int i = 1;
			for (WeightedEntry<IBrazierRouting> entry : weights) {
				ImmanenceTileEntity ite = (ImmanenceTileEntity) entry.entry;
				String description = entry.entry.getClass().toString().replace("com.aranaira.arcanearchives.tileentities.", "");
				player.sendMessage(new TextComponentString("Entry #" + i + " " + description + " weight: " + entry.weight + " pos: " + String.format("%d,%d,%d", ite.getPos().getX(), ite.getPos().getY(), ite.getPos().getZ())));
				i++;
			}
		}
	}
}

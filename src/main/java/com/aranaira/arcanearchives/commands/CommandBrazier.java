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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

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
		if (sender instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) sender;
			ItemStack item = player.getHeldItemMainhand();
			if (item.isEmpty()) {
				player.sendMessage(new StringTextComponent("Can't trace an empty hand."));
				return;
			}
			BrazierTileEntity fake = new BrazierTileEntity(true);
			fake.setWorld(player.world);
			fake.setNetworkId(player.getUniqueID());
			fake.setPos(player.getPosition());
			List<WeightedEntry<IBrazierRouting>> weights = InventoryRoutingUtils.buildNetworkWeights(fake, item, true);
			player.sendMessage(new StringTextComponent("Target is \"" + item.getTranslationKey() + "x" + item.getCount() + "\""));
			player.sendMessage(new StringTextComponent("Total number of potential targets: " + weights.size()));
			int i = 1;
			for (WeightedEntry<IBrazierRouting> entry : weights) {
				ImmanenceTileEntity ite = (ImmanenceTileEntity) entry.entry;
				String description = entry.entry.getClass().toString().replace("com.aranaira.arcanearchives.tileentities.", "");
				player.sendMessage(new StringTextComponent("Entry #" + i + " " + description + " weight: " + entry.weight + " pos: " + String.format("%d,%d,%d", ite.getPos().getX(), ite.getPos().getY(), ite.getPos().getZ())));
				i++;
				BlockPos pos = ite.getPos();
				World world = ite.getWorld();
				AxisAlignedBB bb = new AxisAlignedBB(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				world.getEntitiesWithinAABB(EntityWeight.class, bb.shrink(0.2)).forEach(Entity::setDead);
				EntityWeight weight = new EntityWeight(world, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5);
				weight.setWeight(entry.weight);
				world.spawnEntity(weight);
			}
		}
	}
}

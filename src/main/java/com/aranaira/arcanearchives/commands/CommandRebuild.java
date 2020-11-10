/*package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.types.ServerNetwork;
import com.aranaira.arcanearchives.events.ticks.ServerTickHandler;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.common.DimensionManager;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandRebuild extends CommandBase {
	public CommandRebuild () {
	}

	@Override
	public String getName () {
		return "rebuild_network";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/rebuild_network";
	}

	@Override
	public List<String> getAliases () {
		return Collections.singletonList("rebuild_network");
	}

	@Override
	public int getRequiredPermissionLevel () {
		return 0;
	}

	@Override
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof PlayerEntity) {
			PlayerProfileCache cache = server.getPlayerProfileCache();
			PlayerEntity player = (PlayerEntity) sender;
			ServerNetwork network = DataHelper.getServerNetwork(player.getUniqueID());
			if (network == null) {
				player.sendMessage(new StringTextComponent("Sorry, can't find a network for you?"));
				return;
			}

			HiveNetwork hive = network.getHiveNetwork();
			if (hive != null) {
				hive.clearTiles();
				player.sendMessage(new StringTextComponent("Cleared your and your hive member network tiles"));
			} else {
				network.clearTiles();
				player.sendMessage(new StringTextComponent("Cleared all your network tiles."));
			}

			AtomicInteger tileCount = new AtomicInteger();
			tileCount.set(0);
			Integer[] ids = DimensionManager.getIDs();
			for (int w : ids) {
				ServerWorld world = DimensionManager.getWorld(w);
				world.loadedTileEntityList.forEach((o) -> {
					if (o instanceof ImmanenceTileEntity) {
						ServerTickHandler.incomingITE((ImmanenceTileEntity) o);
						tileCount.getAndIncrement();
					}
				});
			}

			player.sendMessage(new StringTextComponent("Placed " + tileCount.get() + " Immanence Tile Entities back into the incoming queue."));
		}
	}
}*/

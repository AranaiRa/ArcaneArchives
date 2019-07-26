package com.aranaira.arcanearchives.commands;

import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.HiveNetwork;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketDebug.TrackPositions;
import com.aranaira.arcanearchives.tileentities.interfaces.IManifestTileEntity;
import com.aranaira.arcanearchives.util.TileUtils;
import com.aranaira.arcanearchives.types.lists.ITileList;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class CommandTiles extends CommandBase {
	public CommandTiles () {
	}

	@Override
	public String getName () {
		return "tiles";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/tiles | /tiles manifest | /tiles hive | /tiles mine | /tiles valid";
	}

	@Override
	public List<String> getAliases () {
		return Collections.singletonList("tiles");
	}

	@Override
	public int getRequiredPermissionLevel () {
		return 0;
	}

	@Override
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			World world = player.world;
			ServerNetwork network = DataHelper.getServerNetwork(player.getUniqueID(), world);
			if (network == null) {
				player.sendMessage(new TextComponentString("Sorry, can't find a network for you?"));
				return;
			}

			HiveNetwork hive = network.getHiveNetwork();

			ITileList tiles;
			if (args.length == 0) {
				player.sendMessage(new TextComponentString("Automatically deciding what tiles to highlight."));
				if (network.isHiveMember()) {
					player.sendMessage(new TextComponentString("You're part of a hive! Fetching every tile..."));
					tiles = hive.getTiles();
				} else {
					player.sendMessage(new TextComponentString("You're not part of a hive! Just fetching your tiles."));
					tiles = network.getTiles();
				}
			} else {
				switch (args[0].toLowerCase()) {
					case "hive":
						if (!network.isHiveMember() || hive == null) {
							player.sendMessage(new TextComponentString("You're not part of a hive!"));
							return;
						}
						tiles = hive.getTiles();
						break;
					case "mine":
						tiles = network.getTiles();
						break;
					case "valid":
					case "manifest":
					default:
						if (!network.isHiveMember() || hive == null) {
							tiles = network.getTiles();
						} else {
							tiles = hive.getTiles();
						}
						break;
				}
			}

			tiles.refresh(world);

			Int2ObjectOpenHashMap<List<BlockPos>> positions = new Int2ObjectOpenHashMap<>();

			TileListIterable iterable = null;

			if (args.length > 0 && args[0].toLowerCase().equals("valid")) {
				player.sendMessage(new TextComponentString("Filtering to valid tiles only, unsure of how many tiles total there are."));
				iterable = TileUtils.filterValid(tiles);
			} else if (args.length > 0 && args[0].toLowerCase().equals("manifest")) {
				player.sendMessage(new TextComponentString("Filtering to manifest tiles only, unsure of how many tiles total there are."));
				iterable = TileUtils.filterAssignableClass(tiles, IManifestTileEntity.class);
			} else {
				player.sendMessage(new TextComponentString("There are " + tiles.getSize() + " in your network."));
				iterable = tiles.iterable();
			}

			int i = 0;
			for (IteRef ref : iterable) {
				i++;
				String result = "Tile #" + i + " has UUID " + ref.uuid.toString() + " of " + ref.clazz.toString().replace("com.aranaira.arcanearchives.tileentities.", "") + " in dim: " + ref.dimension + "@" + String.format("%d,%d,%d", ref.pos.getX(), ref.pos.getY(), ref.pos.getZ());
				positions.computeIfAbsent(ref.dimension, k -> new ArrayList<>()).add(ref.pos);
				World w = DimensionManager.getWorld(ref.dimension);
				if (w.isBlockLoaded(ref.pos)) {
					result += " and loaded";
				} else {
					result += " but not loaded";
				}
				if (ref.tile != null) {
					result += " has valid reference";
				} else {
					result += " has invalid reference";
				}
				if (ref != null) {
					if (ref.getTile() != null) {
						result += " which resolves properly";
						if (ref.getTile().isInvalid()) {
							result += " but is invalid";
						} else {
							result += " and is valid";
						}
						if (ref.getTile().getNetworkId().equals(player.getUniqueID())) {
							result += " and is part of your subnetwork";
						} else {
							result += " and is owned by someone else in your network";
						}
					} else {
						result += " which does not resolve";
					}
				}
				player.sendMessage(new TextComponentString(result));
			}

			for (Entry<Integer, List<BlockPos>> entry : positions.entrySet()) {
				TrackPositions packet = new TrackPositions(entry.getValue(), entry.getKey());
				Networking.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
			}
		}
	}
}

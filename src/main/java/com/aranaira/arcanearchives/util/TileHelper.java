package com.aranaira.arcanearchives.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TileHelper {
    private static Map<BlockPos, UUID> placementMap = new HashMap<>();

    public static void cleanMap(World world) {
        placementMap.keySet().removeIf((blockPos) -> world.getTileEntity(blockPos) != null);
    }

    public static void markPosition(UUID networkId, BlockPos pos) {
        placementMap.put(pos, networkId);
    }

    public static UUID getNetworkIdForPos(BlockPos pos) {
        return placementMap.get(pos);
    }
}

package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ManifestTracking;
import com.aranaira.arcanearchives.client.render.RenderHelper;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, value = Side.CLIENT)
public class LineHandler {
	public static boolean mIsDrawingLine;
	private static Int2ObjectOpenHashMap<Set<Vec3d>> positionsByDimension = new Int2ObjectOpenHashMap<>();

	private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
	private static final int NUM_Z_BITS = NUM_X_BITS;
	private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
	private static final int Y_SHIFT = 0 + NUM_Z_BITS;
	private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
	private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
	private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
	private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void renderOverlay (RenderWorldLastEvent event) {
		Set<Vec3d> positions = getPositions(Minecraft.getMinecraft().player.dimension);
		if (!positions.isEmpty()) {
			RenderHelper.drawRays(Minecraft.getMinecraft().player.world.getTotalWorldTime(), Minecraft.getMinecraft().player.getPositionVector(), ImmutableSet.copyOf(positions));
		}
	}

	@Nullable
	public static Set<Vec3d> getPositions (int dimension) {
		return positionsByDimension.computeIfAbsent(dimension, k -> new HashSet<>());
	}

	public static void addLine (Vec3d line, int dimension) {
		Set<Vec3d> positions = getPositions(dimension);
		positions.add(line);
	}

	public static void addLine (BlockPos pos, int dimension) {
		addLine(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), dimension);
	}

	public static void addLines (List<BlockPos> positions, int dimension) {
		positions.forEach(k -> addLine(k, dimension));
	}

	public static void removeLine (BlockPos pos, int dimension) {
		Vec3d bpos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
		removeLine(bpos, dimension);
	}

	public static void removeLine (Vec3d line, int dimension) {
		Set<Vec3d> positions = getPositions(dimension);
		positions.remove(line);
	}

	public static void checkClear (int dimension) {
		if (getPositions(dimension).isEmpty()) {
			ManifestTracking.clear();
		}
	}

	@SubscribeEvent
	public static void playerLoggedIn (PlayerLoggedInEvent event) {
		positionsByDimension.clear();
		mIsDrawingLine = false;
	}

	public static void clearChests (int dimension) {
		Set<Vec3d> positions = getPositions(dimension);
		for (Vec3d pos : positions) {
			ManifestTracking.remove(dimension, vec3dToLong(pos));
		}
		positions.clear();
	}

	public static long vec3dToLong (Vec3d pos) {
		return ((long) pos.x & X_MASK) << X_SHIFT | ((long) pos.y & Y_MASK) << Y_SHIFT | ((long) pos.z & Z_MASK);
	}
}

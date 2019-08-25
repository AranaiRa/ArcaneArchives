package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ManifestTrackingUtils;
import com.aranaira.arcanearchives.util.MathUtils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, value = Side.CLIENT)
public class LineHandler {
	public static boolean mIsDrawingLine;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public static void renderOverlay(RenderWorldLastEvent event) {
	    Set<Vec3d> positions = ManifestTrackingUtils.getPositions(Minecraft.getMinecraft().player.dimension);

		if (!positions.isEmpty()) {
			RenderUtils.drawRays(
			        Minecraft.getMinecraft().player.world.getTotalWorldTime(),
                    RenderUtils.getPlayerPosAdjusted( Minecraft.getMinecraft().player, event.getPartialTicks() ),
                    ImmutableSet.copyOf(positions)
            );
		}
	}

	public static void addLine (Vec3d line, int dimension) {
		Set<Vec3d> positions = ManifestTrackingUtils.getPositions(dimension);
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
		Set<Vec3d> positions = ManifestTrackingUtils.getPositions(dimension);
		positions.remove(line);
	}

	public static void checkClear (int dimension) {
		if (ManifestTrackingUtils.getPositions(dimension).isEmpty()) {
			ManifestTrackingUtils.clear();
		}
	}

	@SubscribeEvent
	public static void playerLoggedIn (PlayerLoggedInEvent event) {
		mIsDrawingLine = false;
	}

	public static void clearChests (int dimension) {
		Set<Vec3d> positions = ManifestTrackingUtils.getPositions(dimension);
		for (Vec3d pos : positions) {
			ManifestTrackingUtils.remove(dimension, MathUtils.vec3dToLong(pos));
		}
		positions.clear();
	}

}

package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ManifestTracking;
import com.aranaira.arcanearchives.util.RenderHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, value = Side.CLIENT)
public class LineHandler
{
	public static boolean mIsDrawingLine;
	private static Set<Vec3d> mBlockPositions = new HashSet<>();

	private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
	private static final int NUM_Z_BITS = NUM_X_BITS;
	private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
	private static final int Y_SHIFT = 0 + NUM_Z_BITS;
	private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
	private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
	private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
	private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

	@SubscribeEvent
	public static void renderOverlay(RenderWorldLastEvent event)
	{
		if(mBlockPositions.size() > 0)
		{
			RenderHelper.drawRays(Minecraft.getMinecraft().player.getPositionVector(), ImmutableSet.copyOf(mBlockPositions), 15);
		}
	}

	public static void addLine(Vec3d line)
	{
		if(mBlockPositions.contains(line))
		{
			return;
		}

		mBlockPositions.add(line);
	}

	public static void removeLine(Vec3d line)
	{
		mBlockPositions.remove(line);
	}

	@SubscribeEvent
	public static void playerLoggedIn(PlayerLoggedInEvent event)
	{
		mBlockPositions.clear();
		mIsDrawingLine = false;
	}

	public static void clearChests(int dimension)
	{
		for(Vec3d pos : mBlockPositions)
		{
			ManifestTracking.remove(dimension, vec3dToLong(pos));
		}
		mBlockPositions.clear();
	}

	public static long vec3dToLong(Vec3d pos)
	{
		return ((long) pos.x & X_MASK) << X_SHIFT | ((long) pos.y & Y_MASK) << Y_SHIFT | ((long) pos.z & Z_MASK);
	}
}

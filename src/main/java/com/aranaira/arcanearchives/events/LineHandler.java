package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.RenderHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
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
	private static Set<Vec3d> mBlockPositions = new HashSet<>();
	public static boolean mIsDrawingLine;

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

	public static void clearChests()
	{
		mBlockPositions.clear();
	}
}

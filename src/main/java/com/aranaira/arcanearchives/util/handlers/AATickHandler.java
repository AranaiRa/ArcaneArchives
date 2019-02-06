package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, value = Side.CLIENT)
public class AATickHandler
{
	private static AATickHandler mInstance;
	public List<Vec3d> mBlockPositions = new ArrayList<>();
	public List<Vec3d> mBlockPositionsToRemove = new ArrayList<>();
	public boolean mIsDrawingLine;

	private AATickHandler()
	{

	}

	public static AATickHandler GetInstance()
	{
		if(mInstance == null) mInstance = new AATickHandler();
		return mInstance;
	}

	@SubscribeEvent
	public static void renderOverlay(RenderWorldLastEvent event)
	{
		if(mInstance != null && mInstance.mBlockPositions.size() > 0)
		{
			RenderHelper.drawRays(Minecraft.getMinecraft().player.getPositionVector(), mInstance.mBlockPositions, 15);

			mInstance.mBlockPositions.removeAll(mInstance.mBlockPositionsToRemove);
			mInstance.mBlockPositionsToRemove.clear();
		}
	}

	@SubscribeEvent
	public static void playerLoggedIn(PlayerLoggedInEvent event)
	{
		if(mInstance != null)
		{
			mInstance.mIsDrawingLine = false;
		}
	}

	public void clearChests()
	{
		mBlockPositionsToRemove.addAll(mBlockPositions);
	}
}

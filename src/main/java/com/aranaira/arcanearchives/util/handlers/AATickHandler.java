package com.aranaira.arcanearchives.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import com.aranaira.arcanearchives.*;
import com.aranaira.arcanearchives.util.RenderHelper;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, value = Side.CLIENT)
public class AATickHandler 
{
	private static AATickHandler mInstance;
	public Vec3d mBlockPosition;
	public boolean mIsDrawingLine;
	
	private AATickHandler()
	{
		
	}
	
	public static AATickHandler GetInstance()
	{
		if (mInstance == null)
			mInstance = new AATickHandler();
		return mInstance;
	}
	
	@SubscribeEvent
	public static void renderOverlay(RenderWorldLastEvent event)
	{
			if (mInstance != null && mInstance.mBlockPosition != null && mInstance.mIsDrawingLine == true)
			{
				RenderHelper.drawRay(Minecraft.getMinecraft().player.getPositionVector(), mInstance.mBlockPosition, 15);
			}
	}
	
	@SubscribeEvent
	public static void playerLoggedIn(PlayerLoggedInEvent event)
	{
		if (mInstance != null) 
		{
			mInstance.mIsDrawingLine = false;
		}
	}
}

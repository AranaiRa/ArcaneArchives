package com.aranaira.arcanearchives.entity.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.render.EntityOBJModel;
import com.aranaira.arcanearchives.entity.SpiritGeneric;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSpiritGeneric extends EntityOBJModel<SpiritGeneric> {
	
	float animTime;
	
	public RenderSpiritGeneric(RenderManager renderManager)
	{
		super(renderManager);
		ArcaneArchives.logger.info("beep boop, running the OBJ model model");
	}
	
	@Override
	protected ResourceLocation[] getEntityModels()
	{
		return new ResourceLocation[] {
			new ResourceLocation(ArcaneArchives.MODID, "entity/spiritmatter/mote/head.obj"),
			new ResourceLocation(ArcaneArchives.MODID, "entity/spiritmatter/mote/jaw.obj"),
			new ResourceLocation(ArcaneArchives.MODID, "entity/spiritmatter/mote/tail.obj"),
			new ResourceLocation(ArcaneArchives.MODID, "entity/spiritmatter/mote/arm_left.obj"),
			new ResourceLocation(ArcaneArchives.MODID, "entity/spiritmatter/mote/arm_right.obj"),
		};
	}
	
	@Override
	protected boolean preRender(SpiritGeneric entity, int model, BufferBuilder buffer, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.rotate(180, 1, 0, 0);
		
		animTime += partialTicks * 0.005f;
		
		if(model == 1)
		{
			GlStateManager.rotate(60*(float)Math.sin(animTime), 1, 0, 0);
		}
		
		return true;
	}
}

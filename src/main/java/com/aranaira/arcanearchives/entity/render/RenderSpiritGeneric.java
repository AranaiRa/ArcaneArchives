package com.aranaira.arcanearchives.entity.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.entity.SpiritGeneric;
import com.aranaira.arcanearchives.entity.model.ModelSpiritGeneric;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSpiritGeneric extends RenderLiving<SpiritGeneric>//EntityOBJModel<EntityGeneric>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(ArcaneArchives.MODID+":textures/blocks/block_storage_rawquartz.png");
	
	public RenderSpiritGeneric(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSpiritGeneric(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(SpiritGeneric entity) {
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(SpiritGeneric entity, float p_77043_2_, float yaw, float partialTicks)
	{
		super.applyRotations(entity, p_77043_2_, yaw, partialTicks);
	}
	
	//Below is for EntityOBJModel
	/*protected RenderEntityGeneric(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation[] getEntityModels() {
		return new ResourceLocation[] {
			new ResourceLocation(ArcaneArchives.MODID, "")
		};
	}

	@Override
	protected boolean preRender(EntityGeneric entity, int model, BufferBuilder buffer, double x, double y, double z,
			float entityYaw, float partialTicks) {
		// TODO Auto-generated method stub
		return false;
	}*/
}

package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.entity.SpiritGeneric;
import com.aranaira.arcanearchives.entity.render.RenderSpiritGeneric;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {
	public static void registerEntityRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(SpiritGeneric.class, new IRenderFactory<SpiritGeneric>()
			{
				@Override
				public Render<? super SpiritGeneric> createRenderFor(RenderManager manager)
				{
					return new RenderSpiritGeneric(manager);
				}
			});
	}
}

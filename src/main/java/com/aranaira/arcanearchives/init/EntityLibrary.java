package com.aranaira.arcanearchives.init;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.entity.SpiritGeneric;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityLibrary
{
	public static void registerEntities()
	{
		registerEntity("spiritGeneric", SpiritGeneric.class, 120, 50, 3451490, 16776924);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(ArcaneArchives.MODID+":"+name), entity, name, id, ArcaneArchives.Instance, range, 1, true, color1, color2);
	}
}

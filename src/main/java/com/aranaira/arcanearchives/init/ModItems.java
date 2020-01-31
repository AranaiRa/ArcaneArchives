package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid=ArcaneArchives.MODID)
public class ModItems {
	public static final List<Item> REGISTRY = new ArrayList<>();

	@SubscribeEvent
	public static void onRegister (Register<Item> event) {
		REGISTRY.forEach(event.getRegistry()::register);
	}

	public static <T extends Item> T register (String registryName, Supplier<T> supplier) {
		T item = supplier.get();
		item.setTranslationKey(registryName);
		item.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
		item.setCreativeTab(ArcaneArchives.TAB);
		REGISTRY.add(item);
		return item;
	}

	public static void load () {
	}
}

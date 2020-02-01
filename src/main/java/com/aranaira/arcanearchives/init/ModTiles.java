package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.CrystalWorkbenchBlock;
import com.aranaira.arcanearchives.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid=ArcaneArchives.MODID)
public class ModBlocks {
	public static final List<Block> REGISTRY = new ArrayList<>();

	@SubscribeEvent
	public static void onRegister (Register<Block> event) {
		REGISTRY.forEach(event.getRegistry()::register);
	}

	public static CrystalWorkbenchBlock CrystalWorkbench = register("crystal_workbench", CrystalWorkbenchBlock::new);
	public static MakeshiftResonatorBlock MakeshiftResonator = register("makeshift_resonator", MakeshiftResonatorBlock::new);

	public static <T extends TemplateBlock> T register (String registryName, Supplier<T> supplier) {
		return register(registryName, supplier, null);
	}

	public static <T extends TemplateBlock> T register (String registryName, Supplier<T> supplier, Supplier<? extends ItemBlock> itemBlock) {
		T block = supplier.get();
		block.setTranslationKey(registryName);
		block.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
		block.setCreativeTab(ArcaneArchives.TAB);
		ItemBlock item;
		if (itemBlock != null) {
			item = itemBlock.get();
		} else {
			item = new ItemBlock(block);
		}
		item.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
		block.setItemBlock(item);
		REGISTRY.add(block);
		ModItems.add(item);
		return block;
	}

	public static void load () {

	}
}

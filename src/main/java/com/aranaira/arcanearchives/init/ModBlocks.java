package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.*;
import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class ModBlocks {
	public static BrazierBlock Brazier = register("brazier", BrazierBlock::new);
	public static CelestialLotusEngineBlock CelestialLotusEngine = register("lotus_engine", CelestialLotusEngineBlock::new);
	public static ConformanceChamberBlock ConformanceChamber = register("conformance_chamber", ConformanceChamberBlock::new);
	public static FakeAirBlock FakeAir = register("fake_air", FakeAirBlock::new);
	public static com.aranaira.arcanearchives.blocks.GemCuttersTable GemCuttersTable = register("gem_table", GemCuttersTable::new);

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
		return block;
	}
}

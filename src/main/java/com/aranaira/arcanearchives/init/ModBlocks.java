package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.QuartzClusterBlock;
import com.aranaira.arcanearchives.blocks.RadiantResonatorBlock;
import javafx.scene.shape.Arc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRY;

public class ModBlocks {
  public static RegistryObject<RadiantResonatorBlock> RESONATOR = REGISTRY.registerBlock("radiant_resonator", REGISTRY.block(RadiantResonatorBlock::new, () -> Block.Properties.create(Material.IRON)), () -> new Item.Properties().group(ArcaneArchives.ITEM_GROUP));

  public static RegistryObject<QuartzClusterBlock> QUARTZ_CLUSTER = REGISTRY.registerBlock("quartz_cluster", REGISTRY.block(QuartzClusterBlock::new, () -> Block.Properties.create(Material.IRON)), () -> new Item.Properties().group(ArcaneArchives.ITEM_GROUP));

  public static void load () {

  }
}

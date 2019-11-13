package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRY;

public class ModBlocks {
  public static RegistryObject<Block> RESONATOR = REGISTRY.registerBlock("radiant_resonator", REGISTRY.block(Block::new, () -> Block.Properties.create(Material.IRON)), () -> new Item.Properties().group(ArcaneArchives.ITEM_GROUP));

  public static void load () {

  }
}

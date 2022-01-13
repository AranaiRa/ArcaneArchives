package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class AATags {
  public static class Blocks extends AATags {
    public static final Tags.IOptionalNamedTag<Block> CRYSTAL_WORKBENCH = modTag("crystal_workbench");

    static Tags.IOptionalNamedTag<Block> modTag(String name) {
      return BlockTags.createOptional(new ResourceLocation(ArcaneArchivesAPI.MODID, name));
    }

    static Tags.IOptionalNamedTag<Block> compatTag(String name) {
      return BlockTags.createOptional(new ResourceLocation("forge", name));
    }

    public static ITag<Block> resolve (ResourceLocation location) {
      return TagCollectionManager.getInstance().getBlocks().getTag(location);
    }
  }

  public static class Items extends AATags {
    public static Tags.IOptionalNamedTag<Item> CRYSTAL_WORKBENCH = modTag("crystal_workbench");

    static Tags.IOptionalNamedTag<Item> modTag(String name) {
      return ItemTags.createOptional(new ResourceLocation(ArcaneArchivesAPI.MODID, name));
    }

    static Tags.IOptionalNamedTag<Item> compatTag(String name) {
      return ItemTags.createOptional(new ResourceLocation("forge", name));
    }

    public static ITag<Item> resolve (ResourceLocation location) {
      return TagCollectionManager.getInstance().getItems().getTag(location);
    }
  }
}

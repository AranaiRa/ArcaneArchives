package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.init.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

@MethodsReturnNonnullByDefault
public class CreativeTabAA extends ItemGroup {
  public CreativeTabAA() {
    super(ArcaneArchives.MODID);
  }

  @Override
  public ItemStack createIcon() {
    return new ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ);
  }
}

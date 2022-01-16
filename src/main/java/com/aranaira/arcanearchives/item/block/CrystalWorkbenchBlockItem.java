package com.aranaira.arcanearchives.item.block;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.block.Block;

public class CrystalWorkbenchBlockItem extends AbstractDomainBlockItem {
  public CrystalWorkbenchBlockItem(Block pBlock, Properties pProperties) {
    super(pBlock, pProperties);
  }

  @Override
  protected String getDomainTag() {
    return Identifiers.entityId;
  }
}

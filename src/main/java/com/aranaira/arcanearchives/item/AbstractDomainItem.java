package com.aranaira.arcanearchives.item;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.client.NameStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class AbstractDomainItem extends Item {
  public AbstractDomainItem(Properties pProperties) {
    super(pProperties);
  }

  protected abstract String getDomainTag();

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    pTooltip.add(new StringTextComponent(""));
    CompoundNBT tag = pStack.getTagElement(Identifiers.BlockEntityTag);
    if (tag == null) {
      pTooltip.add(new TranslationTextComponent("arcanearchives.tooltip.domain_item.no_domain").setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)));
    } else if (tag.hasUUID(getDomainTag())) {
      UUID networkId = tag.getUUID(getDomainTag());
      UUIDNameData.Name name = NameStorage.getName(networkId);
      if (name == null) {
        NameStorage.update(networkId);
      } else {
        pTooltip.add(new TranslationTextComponent("arcanearchives.tooltip.domain_item.has_domain", name.component()));
      }
    }
  }
}

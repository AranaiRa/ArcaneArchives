package com.aranaira.arcanearchives.item.block;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.client.NameStorage;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.client.RequestNetworkNamesPacket;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class AbstractNetworkedBlockItem extends BlockItem {
  public AbstractNetworkedBlockItem(Block pBlock, Properties pProperties) {
    super(pBlock, pProperties);
  }

  protected abstract String getNetworkTag ();

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    pTooltip.add(new StringTextComponent(""));
    CompoundNBT tag = pStack.getTagElement(Identifiers.BlockEntityTag);
    if (tag == null) {
      pTooltip.add(new TranslationTextComponent("arcanearchives.tooltip.network_item.no_network").setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)));
    } else if (tag.hasUUID(getNetworkTag())) {
      UUID networkId = tag.getUUID(getNetworkTag());
      UUIDNameData.Name name = NameStorage.getName(networkId);
      if (name == null) {
        NameStorage.update(networkId);
      } else {
        pTooltip.add(new TranslationTextComponent("arcanearchives.tooltip.network_item.has_network", name.component()));
      }
    }

    pTooltip.add(new TranslationTextComponent("arcanearchives.tooltip.network_item.howto", new TranslationTextComponent("arcanearchives.tooltip.network_item.sneak").setStyle(Style.EMPTY.withBold(true).withColor(TextFormatting.GRAY))).setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)));
  }
}

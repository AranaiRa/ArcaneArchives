package com.aranaira.arcanearchives.item;

import com.aranaira.arcanearchives.AATags;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.block.entity.CrystalWorkbenchBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AdjustableDomainItem extends AbstractDomainItem {
  public AdjustableDomainItem(Properties pProperties) {
    super(pProperties);
  }

  protected String getDomainTag() {
    return Identifiers.domainId;
  }

  @Override
  public ActionResultType useOn(ItemUseContext pContext) {
    BlockPos pos = pContext.getClickedPos();
    World level = pContext.getLevel();
    PlayerEntity player = pContext.getPlayer();
    if (player != null && player.isCrouching()) {
      BlockState state = level.getBlockState(pos);
      if (state.is(AATags.Blocks.CRYSTAL_WORKBENCH)) {
        if (!level.isClientSide()) {
          TileEntity blockEntityAt = level.getBlockEntity(pos);
          if (blockEntityAt instanceof CrystalWorkbenchBlockEntity) {
            CrystalWorkbenchBlockEntity cwb = (CrystalWorkbenchBlockEntity) blockEntityAt;
            UUID id = cwb.getEntityId();
            if (id != null) {
              ItemStack stack = pContext.getItemInHand();
              CompoundNBT tag = new CompoundNBT();
              tag.putUUID(Identifiers.domainId, cwb.getEntityId());
              stack.addTagElement(Identifiers.BlockEntityTag, tag);
              UUIDNameData.Name name = cwb.getEntityName();
              player.sendMessage(new TranslationTextComponent("arcanearchives.message.item_attuned", stack.getDisplayName(), name.component()), Util.NIL_UUID);
              return ActionResultType.SUCCESS;
            }
          }
        } else {
          return ActionResultType.CONSUME; // defer to server?
        }
      }
    }

    return super.useOn(pContext);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
    super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    pTooltip.add(new TranslationTextComponent("arcanearchives.tooltip.network_item.howto", new TranslationTextComponent("arcanearchives.tooltip.network_item.sneak").setStyle(Style.EMPTY.withBold(true).withColor(TextFormatting.GRAY))).setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)));
  }
}

package com.aranaira.arcanearchives.item.block;

import com.aranaira.arcanearchives.AATags;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.block.entity.CrystalWorkbenchBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.UUID;

public class NetworkedBlockItem extends BlockItem {
  public NetworkedBlockItem(Block pBlock, Properties pProperties) {
    super(pBlock, pProperties);
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
              tag.putUUID(Identifiers.networkId, cwb.getEntityId());
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
}

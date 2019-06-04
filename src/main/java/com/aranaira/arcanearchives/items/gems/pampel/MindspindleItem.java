package com.aranaira.arcanearchives.items.gems.pampel;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class MindspindleItem extends ArcaneGemItem {
    public static final String NAME = "item_mindspindle";

    public MindspindleItem() { super(NAME, GemCut.PAMPEL, GemColor.GREEN, 800, 3600); }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " +getTooltipData(stack));
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.mindspindle"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumRarity getRarity (ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            ItemStack gem = player.getHeldItemMainhand();
            if(GemUtil.getCharge(gem) == 0) {
                for(int i=0; i<player.inventory.mainInventory.size(); i++) {
                    ItemStack stack = player.inventory.mainInventory.get(i);
                    if(stack.getItem() == Items.BOOK) {
                        GemUtil.restoreCharge(gem, -1);
                        stack.shrink(1);
                        //TODO: Play a particle effect
                        break;
                    }
                    else continue;
                }
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}

package com.aranaira.arcanearchives.items.gems.asscher;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketArcaneGem;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class MurdergleamItem extends ArcaneGemItem {
    public static final String NAME = "item_murdergleam";

    public MurdergleamItem() { super(NAME, GemCut.ASSCHER, GemColor.YELLOW, 30, 150); }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " +getTooltipData(stack));
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.murdergleam"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumRarity getRarity (ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public boolean hasToggleMode() {
        return true;
    }

    @Override
    public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    //@Override
    //public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
    //
    //    return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    //}
}

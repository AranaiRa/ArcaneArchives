package com.aranaira.arcanearchives.items.gems.pendeloque;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.darkhax.bookshelf.data.Blockstates;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class RivertearItem extends ArcaneGemItem {
    public static final String NAME = "item_rivertear";

    public RivertearItem () {
        super(NAME, GemCut.PENDELOQUE, GemColor.BLUE, 25, 100);
    }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " +getTooltipData(stack));
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.rivertear"));
    }

    @Override
    public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ArcaneArchives.logger.info(GemUtil.getCharge(player.getHeldItem(hand)) +" / "+ GemUtil.getMaxCharge(player.getHeldItem(hand)));
            if(GemUtil.getCharge(player.getHeldItem(hand)) > 0) {
                Block hit = world.getBlockState(pos).getBlock();

                FluidStack fs = new FluidStack(FluidRegistry.WATER, 1000);
                world.setBlockState(pos.offset(facing), fs.getFluid().getBlock().getDefaultState(), 11);
                GemUtil.consumeCharge(player.getHeldItem(hand), 1);
            }
        }
        return EnumActionResult.PASS;
    }
}

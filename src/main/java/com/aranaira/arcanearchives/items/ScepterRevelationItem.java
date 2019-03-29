package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ScepterRevelationItem extends ItemTemplate
{
	public static final String NAME = "item_scepterrevelation";

	public ScepterRevelationItem()
	{
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isAirBlock(pos)) return EnumActionResult.SUCCESS;

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (block == BlockRegistry.RADIANT_CHEST) {
		} else if (block == BlockRegistry.RADIANT_CRAFTING_TABLE) {
		} else if (block == BlockRegistry.RADIANT_TROVE) {
		} else if (block == BlockRegistry.RADIANT_RESONATOR) {
		} else if (block == BlockRegistry.MONITORING_CRYSTAL) {
		} else if (block == BlockRegistry.ACCESSOR) {
		} else if (block == BlockRegistry.GEMCUTTERS_TABLE)
		{

		}

		return EnumActionResult.SUCCESS;
	}

	@Override
    public EnumRarity getRarity(ItemStack stack)
    {
    	return EnumRarity.RARE;
    }

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(TextFormatting.GOLD + "Used to query information from devices and containers.");
	}
}

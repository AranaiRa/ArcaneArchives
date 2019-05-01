package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.lireherz.guidebook.guidebook.client.GuiGuidebook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TomeOfArcanaItem extends ItemTemplate
{
	public static final String NAME = "item_tomeofarcana";
	public static final ResourceLocation TOME_OF_ARCANA = new ResourceLocation(ArcaneArchives.MODID, "xml/tome.xml");

	public TomeOfArcanaItem() {
		super(NAME);
	}

	@Override
	public EnumActionResult onItemUse (EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return showBook(worldIn, playerIn.getHeldItem(hand));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		EnumActionResult result = showBook(worldIn, stack);
		return ActionResult.newResult(result, stack);
	}

	@SideOnly(Side.CLIENT)
	private EnumActionResult showBook (World worldIn, ItemStack stack) {
		if (!worldIn.isRemote) {
			return EnumActionResult.FAIL;
		}

		Minecraft.getMinecraft().displayGuiScreen(new GuiGuidebook(TOME_OF_ARCANA));

		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
	}
}

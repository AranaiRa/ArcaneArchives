/*package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import gigaherz.lirelent.guidebook.guidebook.ItemGuidebook;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TomeOfArcanaItem extends ItemGuidebook implements IHasModel {
	public static final String NAME = "tome_arcana";
	public static final ResourceLocation TOME_OF_ARCANA = new ResourceLocation(ArcaneArchives.MODID, "xml/tome.xml");

	public TomeOfArcanaItem () {
		setTranslationKey(NAME);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, NAME));
		setCreativeTab(ArcaneArchives.TAB);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems (CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (this.isInCreativeTab(tab)) {
			subItems.add(of(TOME_OF_ARCANA));
		}
	}

	@Override
	public void registerModels () {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.tome_arcana"));
	}
}*/

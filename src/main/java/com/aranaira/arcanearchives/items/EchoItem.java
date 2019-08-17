package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EchoItem extends ItemTemplate {
	public static final String NAME = "echo";

	public EchoItem() {
		super(NAME);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
	}

	// Does not destroy or modify the source item
	public static ItemStack echoFromItem (ItemStack source) {
		ItemStack copy = source.copy();
		copy.setCount(1);

		ItemStack echo = new ItemStack(ItemRegistry.ECHO);
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(echo);
		tag.setTag("source", copy.serializeNBT());
		return echo;
	}

	// Does not destroy the echo, that is presumed to be done
	// after this item stack is created.
	public static ItemStack itemFromEcho (ItemStack echo) {
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(echo);
		if (!tag.hasKey("source")) {
			return ItemStack.EMPTY;
		}

		return new ItemStack(tag.getCompoundTag("source"));
	}


}

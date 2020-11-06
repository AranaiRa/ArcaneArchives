package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EchoItem extends ItemTemplate {
	public static final String NAME = "echo";

	public EchoItem () {
		super(NAME);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		ItemStack contained = itemFromEcho(stack);
		if (contained.isEmpty()) {
			tooltip.add(TextFormatting.RED + I18n.format("arcanearchives.tooltip.invalid_echo"));
		} else {
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.echo_contained", contained.getDisplayName()));
		}
	}

	// Does not outgoing or modify the source item
	public static ItemStack echoFromItem (ItemStack source) {
		ItemStack copy = source.copy();
		copy.setCount(1);

		ItemStack echo = new ItemStack(ItemRegistry.ECHO);
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(echo);
		tag.setTag("source", copy.serializeNBT());
		return echo;
	}

	// Does not outgoing the echo, that is presumed to be done
	// after this item stack is created.
	public static ItemStack itemFromEcho (ItemStack echo) {
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(echo);
		if (!tag.hasKey("source")) {
			return ItemStack.EMPTY;
		}

		return new ItemStack(tag.getCompoundTag("source"));
	}

	@Override
	public String getItemStackDisplayName (ItemStack item) {
		ItemStack contained = itemFromEcho(item);
		if (contained.isEmpty()) {
			return super.getItemStackDisplayName(item);
		}

		return I18n.format("item.echo_typed.name", contained.getDisplayName());
	}
}

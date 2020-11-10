/*package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.init.ModItems;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class EchoItem extends ItemTemplate {
  public static final String NAME = "echo";

  public EchoItem(Item.Properties properties) {
    super(properties);
  }

*//*	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		ItemStack contained = itemFromEcho(stack);
		if (contained.isEmpty()) {
			tooltip.add(TextFormatting.RED + I18n.format("arcanearchives.tooltip.invalid_echo"));
		} else {
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.echo_contained", contained.getDisplayName()));
		}
	}*//*

  // Does not outgoing or modify the source item
  public static ItemStack echoFromItem(ItemStack source) {
    ItemStack copy = source.copy();
    copy.setCount(1);

    ItemStack echo = new ItemStack(ModItems.ECHO);
    CompoundNBT tag = ItemUtils.getOrCreateTagCompound(echo);
    tag.setTag("source", copy.serializeNBT());
    return echo;
  }

  // Does not outgoing the echo, that is presumed to be done
  // after this item stack is created.
  public static ItemStack itemFromEcho(ItemStack echo) {
    CompoundNBT tag = ItemUtils.getOrCreateTagCompound(echo);
    if (!tag.hasKey("source")) {
      return ItemStack.EMPTY;
    }

    return new ItemStack(tag.getCompoundTag("source"));
  }

  @Override
  public String getItemStackDisplayName(ItemStack item) {
    ItemStack contained = itemFromEcho(item);
    if (contained.isEmpty()) {
      return super.getItemStackDisplayName(item);
    }

    return I18n.format("item.echo_typed.name", contained.getDisplayName());
  }
}*/

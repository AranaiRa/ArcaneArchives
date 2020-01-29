package com.aranaira.arcanearchives.integration.patchouli;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class TestProcessor implements IComponentProcessor {
	@Override
	public void setup (IVariableProvider<String> iVariableProvider) {
		String recipeName = iVariableProvider.get("recipe");
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(recipeName));
		if (item == null) {
			this.stack = ItemStack.EMPTY;
		} else {
			this.stack = new ItemStack(item);
		}
	}

	protected ItemStack stack = ItemStack.EMPTY;

	@Override
	public String process (String s) {
		if (s.startsWith("item1")) {
			return ItemStackUtil.serializeStack(stack);
		}

		return null;
	}
}

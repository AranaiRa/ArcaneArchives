package com.aranaira.arcanearchives.items;

import baubles.api.BaubleType;
import baubles.api.cap.BaubleItem;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class BaubleGemSocket extends ItemTemplate implements baubles.api.IBauble {
    public static final String NAME = "bauble_gemsocket";

    public BaubleGemSocket() {
        super(NAME);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.bauble.gemsocket"));
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BODY;
    }
}

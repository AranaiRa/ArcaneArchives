package com.aranaira.arcanearchives.integration.astralsorcery;

import com.aranaira.arcanearchives.init.ItemRegistry;
import hellfirepvp.astralsorcery.common.base.WellLiquefaction;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Liquefaction {
	public static void init () {
		WellLiquefaction.registerLiquefaction(new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ), BlocksAS.fluidLiquidStarlight, 0.4f /* production multiplier, 0.4f = aquamarine */, 12 /* shatter multiplier: reduce this to make it shatter more often*/, new Color(0x00, 0x88, 0xDD) /* this is the default colour of aquamarine and resonating gems */);
	}
}

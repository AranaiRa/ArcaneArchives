package com.aranaira.arcanearchives.integration.astralsorcery;

import com.aranaira.arcanearchives.init.ItemRegistry;
import hellfirepvp.astralsorcery.common.base.WellLiquefaction;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Liquefaction {
	public static void init () {
		WellLiquefaction.registerLiquefaction(new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ), BlocksAS.fluidLiquidStarlight, 0.07f, 80, new Color(0xDD, 0xEE, 0xFF));
		WellLiquefaction.registerLiquefaction(new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ), BlocksAS.fluidLiquidStarlight, 0.12f, 72, new Color(0xDD, 0xEE, 0xFF));
	}
}

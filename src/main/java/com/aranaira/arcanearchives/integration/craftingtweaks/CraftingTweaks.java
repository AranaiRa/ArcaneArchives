package com.aranaira.arcanearchives.integration.craftingtweaks;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class CraftingTweaks {
	public static void init () {
		if (Loader.isModLoaded("craftingtweaks")) {
			CompoundNBT tag = new CompoundNBT();
			tag.setString("ContainerClass", ContainerRadiantCraftingTable.class.getName());
			tag.setString("AlignToGrid", "left");
			FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tag);
		}
	}
}

package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = ArcaneArchives.MODID)
public class AnvilHandler {
	@SubscribeEvent
	public static void onAnvilRepair (AnvilRepairEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.world.isRemote) {
			return;
		}

		ItemStack output = event.getItemResult();
		if (output.getItem() == ItemRegistry.WRIT_OF_EXPULSION && output.hasTagCompound()) {
			NBTTagCompound tag = output.getTagCompound();
			NBTTagCompound display = output.getOrCreateSubCompound("display");
			if (display.hasKey("Name")) {
				String name = display.getString("Name");
				PlayerProfileCache cache = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache();
				GameProfile profile = cache.getGameProfileForUsername(name);
				if (profile != null) {
					tag.setString("expel_name", name);
					tag.setUniqueId("expel", profile.getId());
				}
			}
		}
	}
}

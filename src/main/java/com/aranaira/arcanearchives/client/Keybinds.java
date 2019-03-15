package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.ManifestItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class Keybinds
{
	public static final String ARCARC_GROUP = "arcanearchives.gui.keygroup";
	public static final String ARCARC_BINDS = "arcanearchives.gui.keybinds";
	public static KeyBinding manifestKey = null;

	public static void initKeybinds()
	{
		KeyBinding kb = new KeyBinding(ARCARC_BINDS + ".manifest", 0, ARCARC_GROUP);
		ClientRegistry.registerKeyBinding(kb);
		manifestKey = kb;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyInputManifest(InputEvent.KeyInputEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(manifestKey.isKeyDown() && mc.inGameHasFocus)
		{
			boolean foundManifest = false;
			for(int i = 0; i < 36; i++)
			{
				ItemStack item = mc.player.inventory.getStackInSlot(i);
				if(item.getItem() == ItemRegistry.MANIFEST)
				{
					foundManifest = true;
					break;
				}
			}

			if(foundManifest)
			{
				if(mc.player.isSneaking())
				{
					LineHandler.clearChests(mc.player.dimension);
				} else
				{
					ManifestItem.openManifest(mc.player.world, mc.player);
				}
			} else
			{
				mc.player.sendMessage(new TextComponentTranslation("arcanearchives.gui.missing_manifest").setStyle(new Style().setColor(TextFormatting.YELLOW)));
			}
		}
	}
}

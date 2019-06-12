package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.GUIManifest;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketConfig.MaxDistance;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Config.LangKey("arcanearchives.config.general")
@Config(modid= ArcaneArchives.MODID)
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ConfigHandler {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(ArcaneArchives.MODID)) {
			ConfigManager.sync(ArcaneArchives.MODID, Config.Type.INSTANCE);
			parseColours();
			MaxDistance packet = new MaxDistance(ManifestConfig.MaxDistance);
			NetworkHandler.CHANNEL.sendToServer(packet);

			Minecraft minecraft = Minecraft.getMinecraft();
			if (minecraft.currentScreen instanceof GUIManifest) {
				((GUIManifest) minecraft.currentScreen).doRefresh();
			}
		}
	}

	@Config.Ignore
	public static int MANIFEST_HIGHLIGHT = 0x991922c4;

	public static void parseColours () {
		try {
			MANIFEST_HIGHLIGHT = Long.decode(ItemTrackingConfig.ChestHighlight.toLowerCase()).intValue();
		} catch (NumberFormatException event) {
			ArcaneArchives.logger.error("Invalid manifest highlight colour: " + ItemTrackingConfig.ChestHighlight, event);
		}
	}

	@Config.Comment("Limit of resonators per player's network")
	@Config.Name("Resonator Limit")
	public static int ResonatorLimit = 3;

	@Config.Comment("Number of ticks it takes to create raw quartz")
	@Config.Name("Resonator Time")
	public static int ResonatorTickTime = 6000;

	@Config.Comment("Causes Radiant Chests, Troves, etc, to be unbreakable when not empty")
	@Config.Name("Unbreakable Radiant Chests & Troves")
	public static boolean UnbreakableContainers = false;

	@Config.Comment("Disable to use default Minecraft-style GUI elements. (Client Only)")
	@Config.Name("Use Pretty GUIs")
	public static boolean UsePrettyGUIs = true;

	//public static boolean bJarvisModeEnabled = false;

	/*public static int iRadiantResonatorDrain = 20;
	public static int iRepositoryMatrixDrain = 80;
	public static int iStorageMatrixDrain = 80;*/

	/*@Config.Comment("The Amount of Bonus Ticks Blocks Get if Their Drain is Met")
	public static int iRadiantResonatorBonusTicks = 1;*/

	/*@Config.Comment("The Amount of Items Each Repository Can Hold")
	public static int iRepositoryMatrixItemCap = 256;
	public static int iStorageMatrixItemCap = 256;*/
}

package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ArcaneArchives.MODID)
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ConfigHandler
{
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(ArcaneArchives.MODID))
		{
		}
	}

	@Config.Comment("Limit of resonators per player")
	@Config.Name("Resonator Limit")
	public static int ResonatorLimit = 3;

	@Config.Comment("Number of ticks it takes to create raw quartz")
	@Config.Name("Resonator Time")
	public static int ResonatorTickTime = 6000;

	@Config.Comment("Whether having a manifest in your inventory is required to open the screen")
	@Config.Name("Manifest Presence")
	public static boolean ManifestPresence = true;

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

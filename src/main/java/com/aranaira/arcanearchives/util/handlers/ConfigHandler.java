package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ArcaneArchives.MODID, name = ArcaneArchives.MODID + "/" + ArcaneArchives.MODID, category = "")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ConfigHandler 
{
	@Config.Comment({ "General Settings" })
	public static ConfigGeneral general = new ConfigGeneral();
	public static ConfigValues values = new ConfigValues();
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(ArcaneArchives.MODID))
		{
			ConfigManager.sync(event.getModID(), Config.Type.INSTANCE);
			//What blood magic does
			//BloodMagic.RITUAL_MANAGER.syncConfig();
		}
	}
	
	public static class ConfigGeneral
	{
		@Config.Comment({ "General Config" })
		public static boolean bJarvisModeEnabled = false;
		
		@Config.Comment({ "Whether Gems of Enchainment require you to do a minigame to capture a spirit or not. If this option is set to false, Spirit Yokes will be disabled." })
		public static boolean bDoSpiritCaptureMinigame = false;
	}
	
	public static class ConfigValues
	{

		@Config.Comment({ "Amount of Drain Each Block Takes From The Network" })
		public static int iRadiantResonatorDrain = 20;
		public static int iRepositoryMatrixDrain = 80;
		
		@Config.Comment({ "The Amount of Bonus Ticks Blocks Get if Their Drain is Met" })
		public static int iRadiantResonatorBonusTicks = 1;
		
		@Config.Comment({ "The Amount of Items Each Repository Can Hold" })
		public static int iRepositoryMatrixItemCap = 256;
	}
}

package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.common.config.Config;

@Config.LangKey("arcanearchives.config.server_side_settings")
@Config(modid= ArcaneArchives.MODID, name="arcanearchives/server")
public class ServerSideConfig {
	@Config.LangKey("arcanearchives.config.server_side_settings.limit")
	public static int ResonatorLimit = 3;

	@Config.LangKey("arcanearchives.config.server_side_settings.time")
	public static int ResonatorTickTime = 6000;

	@Config.LangKey("arcanearchives.config.server_side_settings.multiplier")
	@Config.RangeInt(min = 1, max = 8)
	public static int RadiantMultiplier = 4;
}

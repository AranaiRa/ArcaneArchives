package com.aranaira.arcanearchives.config;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.common.config.Config;

@Config.LangKey("arcanearchives.config.manifest")
@Config(modid = ArcaneArchives.MODID, name = "arcanearchives/manifest")
public class ManifestConfig {
	@Config.LangKey("arcanearchives.config.manifest.presence")
	public static boolean ManifestPresence = true;

	@Config.LangKey("arcanearchives.config.manifest.grid")
	public static boolean DisableManifestGrid = true;

	@Config.LangKey("arcanearchives.config.manifest.distance")
	public static int MaxDistance = 100;

	@Config.LangKey("arcanearchives.config.manifest.jei")
	public static boolean jeiSynchronise = false;

	@Config.Comment("Whether or not the previous search term persists between 'sessions' (i.e., each time you open the manifest, your previous search term is filled in for you)")
	@Config.Name("Search Term Persistence")
	public static boolean searchTermPersistence = false;

	@Config.Comment("Set to true to require holding shift to *close* the manifest after clicking on an item. Otherwise, holding shift will keep the manifest open.")
	@Config.Name("Hold Shift To Keep Manifest Open")
	public static boolean holdShift = true;
}

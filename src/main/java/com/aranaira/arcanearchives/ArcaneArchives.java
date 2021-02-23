package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.core.config.ConfigManager;
import com.aranaira.arcanearchives.core.init.*;
import com.aranaira.arcanearchives.client.setup.ClientInit;
import com.aranaira.arcanearchives.core.setup.CommonSetup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("arcanearchives")
public class ArcaneArchives {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "arcanearchives";
  public static CustomRegistrate REGISTRATE;

  public static ItemGroup GROUP = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(Items.ACACIA_BUTTON);
    }
  };

  public ArcaneArchives() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));

    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(CommonSetup::setup);
    modBus.addListener(ConfigManager::configReloaded);

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);

    REGISTRATE = CustomRegistrate.create(MODID);
    REGISTRATE.itemGroup(() -> GROUP);

    ModEntities.load();
    ModLang.load();
    ModSounds.load();
    ModRecipes.load();
    ModBlocks.load();
    ModTiles.load();
    ModItems.load();
    ModEffects.load();
    ModContainers.load();
  }
}

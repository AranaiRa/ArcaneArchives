package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.crafting.registry.IRecipeManagerAccessor;
import com.aranaira.arcanearchives.client.impl.ClientRecipeAccessor;
import com.aranaira.arcanearchives.client.setup.ClientInit;
import com.aranaira.arcanearchives.core.config.ConfigManager;
import com.aranaira.arcanearchives.core.impl.ServerRecipeAccessor;
import com.aranaira.arcanearchives.core.init.*;
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

@Mod(ArcaneArchives.MODID)
public class ArcaneArchives {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = ArcaneArchivesAPI.MODID;
  public static CustomRegistrate REGISTRATE;

  public static ItemGroup GROUP = new ItemGroup(MODID) {
    @Override
    public ItemStack makeIcon() {
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
    ArcaneArchivesAPI.INSTANCE = new ArcaneArchivesAPI() {
      private final IRecipeManagerAccessor accessor = DistExecutor.safeRunForDist(() -> ClientRecipeAccessor::new, () -> ServerRecipeAccessor::new);

      @Override
      public IRecipeManagerAccessor getRecipeAccessor() {
        return accessor;
      }
    };

    REGISTRATE = CustomRegistrate.create(MODID);
    REGISTRATE.itemGroup(() -> GROUP);

    ModBlocks.load();
    ModContainers.load();
    ModEffects.load();
    ModEntities.load();
    ModItems.load();
    ModLang.load();
    ModProcessors.load();
    ModRecipes.load();
    ModRegistries.load();
    ModSounds.load();
    ModTiles.load();
  }
}

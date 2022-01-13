package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.crafting.registry.IRecipeManagerAccessor;
import com.aranaira.arcanearchives.client.impl.ClientRecipeAccessor;
import com.aranaira.arcanearchives.config.ConfigManager;
import com.aranaira.arcanearchives.impl.ServerRecipeAccessor;
import com.aranaira.arcanearchives.init.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArcaneArchivesAPI.MODID)
public class ArcaneArchives {
  static {
    ModData.setIdAndIdentifier(ArcaneArchivesAPI.MODID, ArcaneArchivesAPI.MOD_IDENTIFIER);
  }

  public static final Logger LOG = LogManager.getLogger();
  public static CustomRegistrate REGISTRATE;

  public static ItemGroup GROUP = new ItemGroup(ArcaneArchivesAPI.MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(Items.ACACIA_BUTTON);
    }
  };

  public ArcaneArchives() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(ArcaneArchivesAPI.MODID + "-common.toml"));

    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(ConfigManager::configReloaded);

    ArcaneArchivesAPI.INSTANCE = new ArcaneArchivesAPI() {
      private final IRecipeManagerAccessor accessor = DistExecutor.safeRunForDist(() -> ClientRecipeAccessor::new, () -> ServerRecipeAccessor::new);

      @Override
      public IRecipeManagerAccessor getRecipeAccessor() {
        return accessor;
      }
    };

    REGISTRATE = CustomRegistrate.create(ArcaneArchivesAPI.MODID);
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
    ModBlockEntities.load();
  }
}

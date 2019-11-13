package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.config.ConfigManager;
import com.aranaira.arcanearchives.init.ModBlocks;
import com.aranaira.arcanearchives.init.ModEntities;
import com.aranaira.arcanearchives.init.ModItems;
import com.aranaira.arcanearchives.setup.ClientSetup;
import com.aranaira.arcanearchives.setup.ModSetup;
import epicsquid.mysticallib.registry.ModRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("arcanearchives")
public class ArcaneArchives {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "arcanearchives";

  public static final ItemGroup ITEM_GROUP = new ItemGroup("glimmering") {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(Items.QUARTZ);
    }
  };

  public static final ModRegistry REGISTRY = new ModRegistry(MODID);

  public static ModSetup setup = new ModSetup();

  public ArcaneArchives() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

    modBus.addListener(setup::init);
    modBus.addListener(setup::gatherData);
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(ClientSetup::init));

    ModItems.load();
    ModBlocks.load();
    ModEntities.load();

    modBus.addGenericListener(EntityType.class, ModEntities::registerEntities);

    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
    REGISTRY.registerEventBus(modBus);
  }
}

package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.init.ModBlocks;
import com.aranaira.arcanearchives.init.ModItems;
import com.aranaira.arcanearchives.init.ModRecipes;
import com.aranaira.arcanearchives.init.ModTiles;
import com.aranaira.arcanearchives.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ArcaneArchives.MODID, name = ArcaneArchives.NAME, version = ArcaneArchives.VERSION, dependencies = "after:baubles;required-before:mysticallib;after:thaumcraft")
public class ArcaneArchives {
  public static final String MODID = "arcanearchives";
  public static final String NAME = "Arcane Archives";
  public static final String VERSION = "GRADLE:VERSION";
  public static final CreativeTabs TAB = new CreativeTabs(MODID) {
    @Override
    public ItemStack createIcon() {
      return ItemStack.EMPTY;
    }
  };

  public static Logger logger;
  @Mod.Instance(MODID)
  public static ArcaneArchives instance;
  @SidedProxy(clientSide = "com.aranaira.arcanearchives.proxy.ClientProxy", serverSide = "com.aranaira.arcanearchives.proxy.CommonProxy")
  public static CommonProxy proxy;

  public static File configDirectory;

  public ArcaneArchives() {
    ModBlocks.load();
    ModItems.load();
    ModTiles.load();
    ModRecipes.load();
  }

  @EventHandler
  public static void preInit(FMLPreInitializationEvent event) {
    proxy.preInit(event);
    configDirectory = event.getModConfigurationDirectory();
  }

  @EventHandler
  public static void init(FMLInitializationEvent event) {
    proxy.init(event);
  }

  @EventHandler
  public static void postInit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }

  @EventHandler
  public static void serverStarting(FMLServerStartingEvent event) {
    proxy.serverStarting(event);
  }

  @EventHandler
  public static void serverStarted(FMLServerStartedEvent event) {
    proxy.serverStarted(event);
  }

  @EventHandler
  public static void loadComplete(FMLLoadCompleteEvent event) {
    proxy.loadComplete(event);
  }

  public static ResourceLocation location(String string) {
    return new ResourceLocation(MODID, string);
  }
}

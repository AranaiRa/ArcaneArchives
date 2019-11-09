package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArcaneArchives.MODID, name = ArcaneArchives.NAME, version = ArcaneArchives.VERSION, dependencies = "required-after:gbook_snapshot;after:baubles;required-before:mysticallib;after:thaumcraft")
public class ArcaneArchives {
  public static final String MODID = "arcanearchives";
  public static final String NAME = "Arcane Archives";
  public static final String VERSION = "GRADLE:VERSION";
  public static final CreativeTabAA TAB = new CreativeTabAA();

  public static Logger logger;
  @Mod.Instance(MODID)
  public static ArcaneArchives instance;
  @SidedProxy(clientSide = "com.aranaira.arcanearchives.proxy.ClientProxy", serverSide = "com.aranaira.arcanearchives.proxy.CommonProxy")
  public static CommonProxy proxy;

  @EventHandler
  public static void preInit(FMLPreInitializationEvent event) {
    proxy.preInit(event);
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

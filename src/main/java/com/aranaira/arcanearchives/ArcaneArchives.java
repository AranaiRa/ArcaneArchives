package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArcaneArchives.MODID, name = ArcaneArchives.NAME, version = ArcaneArchives.VERSION)
public class ArcaneArchives
{
	public static final String MODID = "arcanearchives";
	public static final String NAME = "Arcane Archives";
	public static final String VERSION = "0.1";
	public static final CreativeTabAA TAB = new CreativeTabAA();

	public static Logger logger;
	@Mod.Instance(MODID)
	public static ArcaneArchives instance;
	@SidedProxy(clientSide = "com.aranaira.arcanearchives.proxy.ClientProxy", serverSide = "com.aranaira.arcanearchives.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}

	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}

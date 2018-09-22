package com.aranaira.arcanearchives;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Logger;

import com.aranaira.arcanearchives.blocks.RadiantResonator;

@Mod(modid = ArcaneArchives.MODID, name = ArcaneArchives.NAME, version = ArcaneArchives.VERSION)
public class ArcaneArchives
{
    public static final String MODID = "arcanearchives";
    public static final String NAME = "Arcane Archives";
    public static final String VERSION = "0.1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
    
    //Blocks to be Registered
    static Block radiantResonator = new RadiantResonator();
    
    //Items to be Registered
    
    @Mod.EventBusSubscriber(modid = MODID)
    public static class Registration
    {
    	@SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event)
        {
        	event.getRegistry().register(radiantResonator);
        	logger.info("Registered Blocks!");
        }
        
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
        	event.getRegistry().register(new ItemBlock(radiantResonator).setRegistryName(radiantResonator.getRegistryName()).setUnlocalizedName(radiantResonator.getUnlocalizedName()));

        	logger.info("Registered Items!");
        }
        
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
        	//B3DLoader.INSTANCE.addDomain(MODID.toLowerCase());
        	OBJLoader.INSTANCE.addDomain(MODID.toLowerCase());
        	
        	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(radiantResonator), 0, new ModelResourceLocation(new ResourceLocation(MODID, "crystalmatrixcore"), "inventory"));
        }
    }
}

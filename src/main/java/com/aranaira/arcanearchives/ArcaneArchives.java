package com.aranaira.arcanearchives;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Logger;

import com.aranaira.arcanearchives.blocks.MatrixCrystalCore;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.blocks.RawQuartz;
import com.aranaira.arcanearchives.commands.ArcaneArchivesCommand;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.items.RawQuartzItem;
import com.aranaira.arcanearchives.proxy.CommonProxy;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.MatrixCoreTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;

@Mod(modid = ArcaneArchives.MODID, name = ArcaneArchives.NAME, version = ArcaneArchives.VERSION)
public class ArcaneArchives
{
    public static final String MODID = "arcanearchives";
    public static final String NAME = "Arcane Archives";
    public static final String VERSION = "0.1";
    public static final String COMMON_PROXY = "com.aranaira.arcanearchives.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "com.aranaira.arcanearchives.proxy.ClientProxy";

    public static Logger logger;
    
    @Mod.Instance(MODID)
    public static ArcaneArchives Instance;
    
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }
    
    @EventHandler
    public static void init(FMLInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new ArcaneArchivesCommand());
    }
    
    @EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
        
    }
    
    private void clientPreInit()
    {
    	OBJLoader.INSTANCE.addDomain(MODID);
    }

    //Creative Tab to register
    public static final CreativeTabs TAB_AA = new CreativeTabs(MODID + ".creativeTab") {
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemLibrary.RAW_RADIANT_QUARTZ);
			}
	};
	/*
    //Blocks to be Registered
    public static Block radiantResonator = new RadiantResonator();
    public static Block rawQuartz = new RawQuartz();
    public static Block matrixCore = new MatrixCore();
    public static TileEntity radiantResonatorTileEntity = new RadiantResonatorTileEntity();
    public static TileEntity matrixCoreTileEntity = new MatrixCoreTileEntity();
    
    
    
    @Mod.EventBusSubscriber(modid = MODID)
    public static class Registration
    {
    	@SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event)
        {
        	event.getRegistry().register(radiantResonator);
        	event.getRegistry().register(rawQuartz);
        	event.getRegistry().register(matrixCore);
        	

        	//event.getRegistry().register(radiantResonatorTileEntity);
        	TileEntity.register("radiantResonatorTileEntity", radiantResonatorTileEntity.getClass());
        	TileEntity.register("matrixCrystalTileEntity", matrixCoreTileEntity.getClass());
        	
        	logger.info("Registered Blocks!");
        }
        
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
        	RegisterBlockItem(rawQuartz, event);
        	RegisterBlockItem(radiantResonator, event);
        	RegisterBlockItem(matrixCore, event);

        	logger.info("Registered Items!");
        }
        
        
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
        	//B3DLoader.INSTANCE.addDomain(MODID.toLowerCase());
        	OBJLoader.INSTANCE.addDomain(MODID.toLowerCase());
        	
        	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(radiantResonator), 0, new ModelResourceLocation(new ResourceLocation(MODID, "radiantresonator.obj"), "inventory"));
        	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(rawQuartz), 0, new ModelResourceLocation(new ResourceLocation(MODID, "rawquartz.obj"), "inventory"));
        	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(matrixCore), 0, new ModelResourceLocation(new ResourceLocation(MODID, "crystalmatrixcore.obj"), "inventory"));
        }
    }
    
    //Shorter method for registering block items.
    public static void RegisterBlockItem(Block block, RegistryEvent.Register<Item> event)
    {
    	event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()).setUnlocalizedName(block.getUnlocalizedName()));
    }*/
}

/*package com.aranaira.arcanearchives.proxy;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.Keybinds;
import com.aranaira.arcanearchives.client.render.entity.RenderWeight;
import com.aranaira.arcanearchives.entity.EntityWeight;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
*//*	@OnlyIn(Dist.CLIENT)
	public static RadiantTankTESR tankTESR;
	@OnlyIn(Dist.CLIENT)
	public static RadiantTankTEISR itemTESR;
	@OnlyIn(Dist.CLIENT)
	public static RadiantChestTESR chestTESR;
	@OnlyIn(Dist.CLIENT)
	public static RadiantTroveTESR troveTESR;
	@OnlyIn(Dist.CLIENT)
	public static BrazierTESR brazierTESR;*//*

  @SubscribeEvent
  public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    *//*		DataHelper.clearClientCache();*//*
  }

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void modelRegister(ModelRegistryEvent event) {
*//*		tankTESR = new RadiantTankTESR();
		ClientRegistry.bindTileEntitySpecialRenderer(RadiantTankTileEntity.class, tankTESR);
		//
		RadiantTankItem item = (RadiantTankItem) BlockRegistry.RADIANT_TANK.getItemBlock();
		ModelResourceLocation mrl = new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, "dummy_builtin_blocktransforms"), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
		itemTESR = new RadiantTankTEISR(tankTESR);
		item.setTileEntityItemStackRenderer(itemTESR);
		//
		chestTESR = new RadiantChestTESR();
		ClientRegistry.bindTileEntitySpecialRenderer(RadiantChestTileEntity.class, chestTESR);
		//
		//troveTESR = new RadiantTroveTESR();
		//ClientRegistry.bindTileEntitySpecialRenderer(RadiantTroveTileEntity.class, troveTESR);
		//
		brazierTESR = new BrazierTESR();
		ClientRegistry.bindTileEntitySpecialRenderer(BrazierTileEntity.class, brazierTESR);*//*
  }

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);
    OBJLoader.INSTANCE.addDomain(ArcaneArchives.MODID);

    Keybinds.initKeybinds();
    RenderingRegistry.registerEntityRenderingHandler(EntityWeight.class, RenderWeight::new);
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);

*//*		ItemColors colours = Minecraft.getInstance().getItemColors();

		colours.registerItemColorHandler((stack, tintIndex) -> {
			if (tintIndex != 1) {
				return -1;
			}

			ItemStack contained = EchoItem.itemFromEcho(stack);
			if (contained.isEmpty()) {
				return -1;
			}

			for (int i = 0; i < 3; i++) {
				int tint = colours.colorMultiplier(contained, i);
				if (tint != -1) {
					return tint;
				}
			}

			return TintUtils.getColor(contained);
		}, ItemRegistry.ECHO);*//*
  }

  @Override
  public void loadComplete(FMLLoadCompleteEvent event) {
    super.loadComplete(event);
    //TintUtils.init();
  }

  @Override
  public void registerItemRenderer(@Nonnull Item item, int meta, String id) {
  }
}*/

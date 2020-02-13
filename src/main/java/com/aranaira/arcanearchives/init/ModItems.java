package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.RadiantDustItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ModItems {
  public static final List<Item> REGISTRY = new ArrayList<>();

  public static final RadiantDustItem RadiantDust = register("radiant_dust", RadiantDustItem::new);
  public static final Item RadiantQuartz = register("radiant_quartz", Item::new);

  @SubscribeEvent
  public static void onRegister(Register<Item> event) {
    REGISTRY.forEach(event.getRegistry()::register);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void onModelRegister(ModelRegistryEvent event) {
    REGISTRY.forEach(o -> ModelLoader.setCustomModelResourceLocation(o, 0, new ModelResourceLocation(Objects.requireNonNull(o.getRegistryName()), "inventory")));
  }

  public static <T extends Item> T register(String registryName, Supplier<T> supplier) {
    T item = supplier.get();
    item.setTranslationKey(registryName);
    item.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
    item.setCreativeTab(ArcaneArchives.TAB);
    REGISTRY.add(item);
    return item;
  }

  public static <T extends Item> void add(T item) {
    REGISTRY.add(item);
  }

  public static void load() {
  }
}

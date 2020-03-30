package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.items.upgrades.UpgradeInfo;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ModItems {
  public static final List<Item> REGISTRY = new ArrayList<>();

  public static final ItemTemplate RadiantDust = register("radiant_dust", ItemTemplate::new, (o) -> o.setTooltip("arcanearchives.tooltip.item.radiant_dust", TextFormatting.GOLD));
  public static final ItemTemplate ShapedQuartz = register("shaped_quartz", ItemTemplate::new, (o) -> o.setTooltip("arcanearchives.tooltip.item.shaped_quartz", TextFormatting.GOLD));
  public static final ItemTemplate ScintillatingInlay = register("scintillating_inlay", ItemTemplate::new, (o) -> o.setTooltip("arcanearchives.tooltip.item.scintillating_inlay", TextFormatting.GOLD));
  public static final ItemTemplate MatrixBrace = register("matrix_brace", ItemTemplate::new, (o) -> o.setTooltip("arcanearchives.tooltip.item.matrix_brace", TextFormatting.GOLD).setUpgradeInfo(ModUpgrades.MATRIX_BRACE));
  public static final ItemTemplate ContainmentField = register("containment_field", ItemTemplate::new, (o) -> o.setTooltip("arcanearchives.tooltip.item.containment_field", TextFormatting.GOLD).setUpgradeInfo(ModUpgrades.CONTAINMENT_FIELD));
  public static final ItemTemplate MaterialInterface = register("material_interface", ItemTemplate::new, (o) -> o.setTooltip("arcanearchives.tooltip.item.material_interface", TextFormatting.GOLD).setUpgradeInfo(ModUpgrades.MATERIAL_INTERFACE));
  public static final ItemTemplate RadiantKey = register("radiant_key", ItemTemplate::new, (o) -> o.setTooltip("arcanearchives.tooltip.item.radiant_key", TextFormatting.GOLD).setUpgradeInfo(ModUpgrades.RADIANT_KEY));

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
    return register(registryName, supplier, null);
  }

  public static <T extends Item> T register(String registryName, Supplier<T> supplier, @Nullable Consumer<T> consumer) {
    T item = supplier.get();
    item.setTranslationKey(registryName);
    item.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
    item.setCreativeTab(ArcaneArchives.TAB);
    REGISTRY.add(item);
    if (consumer != null) {
      consumer.accept(item);
    }
    return item;
  }

  public static <T extends Item> void add(T item) {
    REGISTRY.add(item);
  }

  public static void load() {
  }
}

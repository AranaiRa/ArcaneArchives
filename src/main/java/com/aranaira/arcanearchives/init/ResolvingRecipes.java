package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.crafting.recipes.ResolvingRecipeType;
import com.aranaira.arcanearchives.recipe.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.recipe.inventory.CrystalWorkbenchCrafting;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.Comparator;
@Mod.EventBusSubscriber(modid= ArcaneArchivesAPI.MODID)
public class ResolvingRecipes {
  public static final ResolvingRecipeType<CrystalWorkbenchCrafting, CrystalWorkbenchRecipe> CRYSTAL_WORKBENCH = new ResolvingRecipeType<>(new LazySupplier<>(() -> ModRecipes.Types.CRYSTAL_WORKBENCH), Comparator.comparing(o -> o.getId().getPath()));

  @SubscribeEvent
  public static void onReloadListeners (AddReloadListenerEvent event) {
    event.addListener(CRYSTAL_WORKBENCH);
  }
}

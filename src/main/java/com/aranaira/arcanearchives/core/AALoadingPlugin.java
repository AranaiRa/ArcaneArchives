package com.aranaira.arcanearchives.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@MCVersion("1.12.2")
@SortingIndex(-5000)
@TransformerExclusions("com.aranaira.arcanearchives.core")
public class AALoadingPlugin implements IFMLLoadingPlugin {
  public static Logger log = LogManager.getLogger("Arcane Archives Core");

  public AALoadingPlugin() {
    log.info("Attempting to bootstrap Mixins and plugins.");
    MixinBootstrap.init();
    Mixins.addConfiguration("mixins.arcanearchives.json");
  }

  @Override
  public String[] getASMTransformerClass() {
    return new String[0];
  }

  @Override
  public String getModContainerClass() {
    return null;
  }

  @Nullable
  @Override
  public String getSetupClass() {
    return null;
  }

  @Override
  public void injectData(Map<String, Object> data) {

  }

  @Override
  public String getAccessTransformerClass() {
    return null;
  }
}

package com.aranaira.arcanearchives.mixins;

import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class AAMixinPlugin implements IMixinConfigPlugin {
  @Override
  public void onLoad(String mixinPackage) {

  }

  @Override
  public String getRefMapperConfig() {
    if (Launch.blackboard.get("fml.deobfuscatedEnvironment") == Boolean.TRUE) {
      return null;
    }

    return "mixins.arcanearchives.refmap.json";
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    return targetClassName.equals("net.minecraft.client.gui.inventory.GuiContainer") && MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT;
  }

  @Override
  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

  }

  @Override
  public List<String> getMixins() {
    return null;
  }

  @Override
  public void preApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }

  @Override
  public void postApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }
}


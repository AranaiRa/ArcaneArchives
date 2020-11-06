package com.aranaira.arcanearchives.mixins;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerScreen.class)
@SuppressWarnings("unused")
public abstract class MixinGuiContainer {
/*	@SideOnly(Side.CLIENT)
	private static List<Class<? extends GuiContainer>> CONTAINER_IGNORE_LIST = Arrays.asList(GUIManifest.class, GUIGemCuttersTable.class, GUIUpgrades.class, GUIRadiantChest.class, GUIGemSocket.class);*/

  @Inject(method = "drawSlot", at = @At(value = "HEAD"))
  @SideOnly(Side.CLIENT)
  private void onDrawSlot(Slot slot, CallbackInfo callbackInfo) {
/*		if (NonModTrackingConfig.DisableMixinHighlight || NonModTrackingConfig.getContainerClasses().contains(((GuiContainer) (Object) this).getClass())) {
			return;
		}

		if (CONTAINER_IGNORE_LIST.contains(((GuiContainer) (Object) this).getClass())) {
			return;
		}

		ItemStack stack = slot.getStack();
		if (!stack.isEmpty()) {
			if (ManifestTrackingUtils.matches(stack)) {
				GlStateManager.disableDepth();
				long worldTime = ((GuiContainer) (Object) this).mc.player.world.getWorldTime();
				Color c = ColorUtils.getColorFromTime(worldTime);
				GuiContainer.drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, c.toInteger());
				GlStateManager.enableDepth();
			}
		}*/
  }

  @Inject(method = "onGuiClosed", at = @At(value = "RETURN"))
  @SideOnly(Side.CLIENT)
  private void onGuiClosed(CallbackInfo callbackInfo) {
/*		Minecraft _mc = ((GuiContainer) (Object) this).mc;
		if (_mc != null && _mc.player != null) {
			LineHandler.checkClear(_mc.player.dimension);
		}*/
  }
}

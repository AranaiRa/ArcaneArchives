package com.aranaira.arcanearchives.mixins;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.config.ItemTrackingConfig;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.util.ManifestTracking;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public abstract class MixinGuiContainer {
	@Inject(method = "drawSlot", at = @At(value = "HEAD"))
	private void onDrawSlot (Slot slot, CallbackInfo callbackInfo) {
		if (ItemTrackingConfig.DisableMixinHighlight) return;

		ItemStack stack = slot.getStack();
		if (!stack.isEmpty()) {
			if (ManifestTracking.matches(stack)) {
				GlStateManager.disableDepth();
				float partialTicks = ((GuiContainer) (Object) this).mc.getTickLength();
				GuiContainer.drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, ConfigHandler.MANIFEST_HIGHLIGHT);
				GlStateManager.enableDepth();
			}
		}
	}

	@Inject(method = "onGuiClosed", at = @At(value = "RETURN"))
	private void onGuiClosed (CallbackInfo callbackInfo) {
		LineHandler.checkClear();
	}
}


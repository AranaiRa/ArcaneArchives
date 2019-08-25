package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import com.aranaira.arcanearchives.util.MathUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ArcaneArchives.MODID)
public class RenderHUD {
	private RenderHUD () {
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onDrawScreenPost (RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();

		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			RayTraceResult pos = mc.objectMouseOver;

			if (pos != null && pos.typeOfHit == RayTraceResult.Type.BLOCK) {
				IBlockState state = mc.world.getBlockState(pos.getBlockPos());
				Block block = state.getBlock();
				if (block == BlockRegistry.RADIANT_TROVE) {
					RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, mc.world, pos.getBlockPos());
					if (te != null) {
						ScaledResolution res = event.getResolution();
						int x = res.getScaledWidth() / 2 - 40;
						int y = res.getScaledHeight() / 2;

						TroveItemHandler handler = te.getInventory();

						ItemStack item = handler.getItemCurrent();
						if (handler.isLocked()) {
							item = handler.getItem();
						}
						if (!handler.isEmpty() && item.getItem() != Items.AIR) {
							RenderHelper.enableGUIStandardItemLighting();
							mc.getRenderItem().renderItemIntoGUI(item, x - 40, y);
							RenderHelper.disableStandardItemLighting();
							String s = "x " + MathUtils.format(handler.getCount());
							mc.fontRenderer.drawStringWithShadow(item.getDisplayName(), (float) (x - 19 - mc.fontRenderer.getStringWidth(item.getDisplayName()) / 2), (float) (y - 11), 16777215);
							mc.fontRenderer.drawStringWithShadow(s, (float) (x - 20), (float) (y + 3), 16777215);

							int storage = handler.getTotalUpgradesCount().x;
							int optional = handler.getTotalUpgradesCount().y;
							if (storage + optional != 0) {
								if (storage + optional == 1) {
									String n = "" + storage;
									if (optional > 0) {
										n += "+" + optional;
									}

									s = I18n.format("arcanearchives.data.gui.radiant_trove.upgrade", n);
								} else {
									String n = "" + storage;
									if (optional > 0) {
										n += "+" + optional;
									}

									s = I18n.format("arcanearchives.data.gui.radiant_trove.upgrades", n);
								}
								mc.fontRenderer.drawStringWithShadow(TextFormatting.GOLD + s, (float) (x - 19 - mc.fontRenderer.getStringWidth(s) / 2), (float) (y + 20), 16777215);
							}
						} else if (handler.getUpgrades() != 0) {
							String s;
							if (handler.getUpgrades() == 1) {
								s = I18n.format("arcanearchives.data.gui.radiant_trove.upgrade", handler.getUpgrades());
							} else {
								s = I18n.format("arcanearchives.data.gui.radiant_trove.upgrades", handler.getUpgrades());
							}
							mc.fontRenderer.drawStringWithShadow(TextFormatting.GOLD + s, (float) (x - 19 - mc.fontRenderer.getStringWidth(s) / 2), (float) (y), 16777215);
						}
					}
				}
			}
		}

		GlStateManager.color(1f, 1f, 1f);
	}
}

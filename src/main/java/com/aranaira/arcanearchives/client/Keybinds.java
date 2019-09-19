package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.render.LineHandler;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.config.ManifestConfig;
import com.aranaira.arcanearchives.data.types.ClientNetwork;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.integration.jei.JEIUnderMouse;
import com.aranaira.arcanearchives.items.ManifestItem;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketArcaneGems.OpenSocket;
import com.aranaira.arcanearchives.util.ManifestTrackingUtils;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class Keybinds {
	public static final String ARCARC_GROUP = "arcanearchives.gui.keygroup";
	public static final String ARCARC_BINDS = "arcanearchives.gui.keybinds";
	public static KeyBinding manifestKey = null;
	public static KeyBinding socketKey = null;


	public static void initKeybinds () {
		KeyBinding kb = new KeyBinding(ARCARC_BINDS + ".manifest", 0, ARCARC_GROUP);
		ClientRegistry.registerKeyBinding(kb);
		manifestKey = kb;
		if (ConfigHandler.ArsenalConfig.EnableArsenal) {
			kb = new KeyBinding(ARCARC_BINDS + ".socket", 0, ARCARC_GROUP);
			ClientRegistry.registerKeyBinding(kb);
			socketKey = kb;
		}
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	private static ItemStack underMouse (Minecraft mc) {
		ItemStack jei = JEIUnderMouse.underMouse();
		if (jei != null) {
			return jei;
		}

		if (mc.currentScreen instanceof GuiContainer) {
			GuiContainer container = (GuiContainer) mc.currentScreen;
			Slot underMouse = container.getSlotUnderMouse();
			if (underMouse != null) {
				return underMouse.getStack();
			}
		}

		return null;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyInputManifest (InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (manifestKey.isKeyDown() && mc.inGameHasFocus) {
			boolean foundManifest = false;
			if (ManifestConfig.ManifestPresence) {
				for (int i = 0; i < 36; i++) {
					ItemStack item = mc.player.inventory.getStackInSlot(i);
					if (item.getItem() == ItemRegistry.MANIFEST) {
						foundManifest = true;
						break;
					}
				}
			}

			if (foundManifest || !ManifestConfig.ManifestPresence) {
				if (mc.player.isSneaking()) {
					LineHandler.clearChests(mc.player.dimension);
				} else {
					ManifestItem.openManifest(mc.player.world, mc.player);
				}
			} else {
				mc.player.sendMessage(new TextComponentTranslation("arcanearchives.gui.missing_manifest").setStyle(new Style().setColor(TextFormatting.YELLOW)));
			}
		} else if (socketKey != null && socketKey.isKeyDown() && mc.inGameHasFocus) {
			OpenSocket packet = new OpenSocket();
			Networking.CHANNEL.sendToServer(packet);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeypress (GuiScreenEvent.KeyboardInputEvent.Pre event) {
		if (Keyboard.getEventKeyState() && manifestKey.isActiveAndMatches(Keyboard.getEventKey())) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.currentScreen != null) {
				ItemStack stack = underMouse(mc);
				if (stack != null && !stack.isEmpty()) {
					ClientNetwork network = DataHelper.getClientNetwork();
					network.synchroniseManifest(handler -> {
						handler.setSearchItem(stack);
						boolean addedValues = false;
						for (int i = 0; i < handler.getSlots(); i++) {
							CollatedEntry entry = handler.getManifestEntryInSlot(i);
							if (entry == null || entry.outOfDimension || entry.outOfRange) {
								continue;
							}

							//List<Vec3d> visPositions = entry.getVecPositions();
							//isPositions.forEach(l -> LineHandler.addLine(l, mc.player.dimension));
							ManifestTrackingUtils.add(entry);

							addedValues = true;
						}
						if (!GuiScreen.isShiftKeyDown() && addedValues) {
							mc.player.closeScreen();
						}
					});
				}
			}
		}
	}
}

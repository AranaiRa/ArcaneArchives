package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.GUIGemcasting;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.entity.AIResonatorSit;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.TankMode;
import com.aranaira.arcanearchives.items.TomeOfArcanaItem;
import com.aranaira.arcanearchives.items.TomeOfArcanaItemBackground;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketRadiantAmphora;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import gigaherz.lirelent.guidebook.client.BookRegistryEvent;
import gigaherz.lirelent.guidebook.guidebook.client.BookRendering;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Random;

@Mod.EventBusSubscriber
public class AAEventHandler {
	@SubscribeEvent
	public static void onBlockBreakEvent (BreakEvent event) {
		if (!ConfigHandler.UnbreakableContainers) {
			return;
		}

		World w = event.getWorld();
		Block block = event.getState().getBlock();

		if (!w.isRemote && block == BlockRegistry.RADIANT_CHEST) {
			RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, w, event.getPos());
			if (te == null) {
				return;
			}

			// null everything
			IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

			if (handler == null) {
				return;
			}

			boolean allSlotsEmpty = true;

			// null everything
			for (int i = 0; i < handler.getSlots(); i++) {
				if (!handler.getStackInSlot(i).isEmpty()) {
					allSlotsEmpty = false;
					break;
				}
			}

			if (!allSlotsEmpty) {
				event.setCanceled(true);
			}
		} else if (!w.isRemote && block == BlockRegistry.RADIANT_TROVE) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, w, event.getPos());
			if (te != null && !te.isEmpty()) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDestroyBlockEvent (LivingDestroyBlockEvent event) {
		if (!ConfigHandler.UnbreakableContainers) {
			return;
		}

		Block block = event.getState().getBlock();
		if (block == BlockRegistry.RADIANT_CHEST || block == BlockRegistry.RADIANT_TROVE) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBlockActivated (PlayerInteractEvent.RightClickBlock event) {
		LineHandler.removeLine(event.getPos());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event)
	{
		Item item = event.getEntityPlayer().inventory.getCurrentItem().getItem();
		if(item == ItemRegistry.RADIANT_AMPHORA) {
			PacketRadiantAmphora packet = new PacketRadiantAmphora();
			NetworkHandler.CHANNEL.sendToServer(packet);
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
	{
		if(!event.getWorld().isRemote) {
			Item item = event.getEntityPlayer().inventory.getCurrentItem().getItem();
			if (item == ItemRegistry.RADIANT_AMPHORA && event.getEntityPlayer().isSneaking()) {
				ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
				AmphoraUtil util = new AmphoraUtil(stack);
				util.toggleMode();
			}
			if (item == ItemRegistry.RAW_RADIANT_QUARTZ) {
				ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
				Random rng = new Random();
				int num = rng.nextInt(5);
				if(num == 0) {
					stack.shrink(1);
					num = rng.nextInt(16) + 8;
					ItemStack shards = new ItemStack(BlockRegistry.QUARTZ_SLIVER, num);
					Vec3d pos = event.getHitVec();
					EntityItem ei = new EntityItem(event.getWorld(), pos.x, pos.y, pos.z, shards);
					ei.motionX = rng.nextFloat() * 0.4f - 0.2f;
					ei.motionZ = rng.nextFloat() * 0.4f - 0.2f;
					ei.motionY = rng.nextFloat() * 0.2f + 0.2f;
					event.getWorld().spawnEntity(ei);
				}
				else if(num == 1 || num == 2){
					ItemStack shards = new ItemStack(BlockRegistry.QUARTZ_SLIVER, 1);
					Vec3d pos = event.getHitVec();
					EntityItem ei = new EntityItem(event.getWorld(), pos.x, pos.y, pos.z, shards);
					ei.motionX = rng.nextFloat() * 0.4f - 0.2f;
					ei.motionZ = rng.nextFloat() * 0.4f - 0.2f;
					ei.motionY = rng.nextFloat() * 0.2f + 0.2f;
					event.getWorld().spawnEntity(ei);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void renderGemHUD(RenderGameOverlayEvent.Post event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.player;
		if(player == null) return;

		if(event.getType() == RenderGameOverlayEvent.ElementType.VIGNETTE) {
			if(player.getHeldItemMainhand().getItem() instanceof ArcaneGemItem) {
				GUIGemcasting.draw(minecraft, player.getHeldItemMainhand(), event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), false);
			}
			if(player.getHeldItemOffhand().getItem() instanceof ArcaneGemItem) {
				GUIGemcasting.draw(minecraft, player.getHeldItemOffhand(), event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), true);
			}
		}
	}

	@Optional.Method(modid = "gbook")
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unused")
	public static void registerBook (BookRegistryEvent event) {
		event.register(TomeOfArcanaItem.TOME_OF_ARCANA, true);
	}

	@SubscribeEvent
	public static void onEntityJoinedWorld (EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityOcelot) {
			EntityOcelot ocelot = (EntityOcelot) event.getEntity();
			ocelot.tasks.addTask(6, new AIResonatorSit(ocelot, 0.8D));
		}
	}
}
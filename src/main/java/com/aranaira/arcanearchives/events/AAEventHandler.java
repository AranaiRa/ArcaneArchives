package com.aranaira.arcanearchives.events;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.GUIGemcasting;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.entity.AIResonatorSit;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.items.BaubleGemSocket;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.TomeOfArcanaItem;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.asscher.*;
import com.aranaira.arcanearchives.items.gems.oval.*;
import com.aranaira.arcanearchives.items.gems.pampel.*;
import com.aranaira.arcanearchives.items.gems.pendeloque.*;
import com.aranaira.arcanearchives.items.gems.trillion.*;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketArcaneGemToggle;
import com.aranaira.arcanearchives.network.PacketConfig.RequestMaxDistance;
import com.aranaira.arcanearchives.network.PacketRadiantAmphora;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import gigaherz.lirelent.guidebook.client.BookRegistryEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.common.network.PacketHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber
public class AAEventHandler {
	@SubscribeEvent
	public static void onPlayerJoined (PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.world.isRemote) {
			RequestMaxDistance packet = new RequestMaxDistance();
			NetworkHandler.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
		}
	}

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
	public static void onLeftClickEmpty (PlayerInteractEvent.LeftClickEmpty event) {
		Item item = event.getEntityPlayer().inventory.getCurrentItem().getItem();
		if (item == ItemRegistry.RADIANT_AMPHORA) {
			PacketRadiantAmphora packet = new PacketRadiantAmphora();
			NetworkHandler.CHANNEL.sendToServer(packet);
		}
		if (item instanceof ArcaneGemItem) {
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			ArcaneGemItem agi = (ArcaneGemItem)stack.getItem();

			if(agi.hasToggleMode()) {
				PacketArcaneGemToggle packet = new PacketArcaneGemToggle();
				NetworkHandler.CHANNEL.sendToServer(packet);
			}
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock (PlayerInteractEvent.LeftClickBlock event) {
		if (!event.getWorld().isRemote) {
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
				if (num == 0) {
					stack.shrink(1);
					num = rng.nextInt(16) + 8;
					ItemStack shards = new ItemStack(BlockRegistry.QUARTZ_SLIVER, num);
					Vec3d pos = event.getHitVec();
					EntityItem ei = new EntityItem(event.getWorld(), pos.x, pos.y, pos.z, shards);
					ei.motionX = rng.nextFloat() * 0.4f - 0.2f;
					ei.motionZ = rng.nextFloat() * 0.4f - 0.2f;
					ei.motionY = rng.nextFloat() * 0.2f + 0.2f;
					event.getWorld().spawnEntity(ei);
				} else if (num == 1 || num == 2) {
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void renderGemHUD(RenderGameOverlayEvent.Post event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.player;
		if (player == null) {
			return;
		}

		if(event.getType() == RenderGameOverlayEvent.ElementType.VIGNETTE) {
			if(player.getHeldItemMainhand().getItem() instanceof ArcaneGemItem) {
				GUIGemcasting.draw(minecraft, player.getHeldItemMainhand(), event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), GUIGemcasting.EnumGemGuiMode.RIGHT);
			}
			if(player.getHeldItemOffhand().getItem() instanceof ArcaneGemItem) {
				GUIGemcasting.draw(minecraft, player.getHeldItemOffhand(), event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), GUIGemcasting.EnumGemGuiMode.LEFT);
			}

			IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
			for(int i : BaubleType.BODY.getValidSlots()) {
				if(handler.getStackInSlot(i).getItem() instanceof BaubleGemSocket) {
					if(handler.getStackInSlot(i).getTagCompound().hasKey("gem")) {
						ItemStack containedStack = GemSocketHandler.getHandler(handler.getStackInSlot(i)).getInventory().getStackInSlot(0);
						GUIGemcasting.draw(minecraft, containedStack, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), GUIGemcasting.EnumGemGuiMode.SOCKET);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void playerPickupXP(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player == null) return;

		ArrayList<ItemStack> held = ArcaneGemItem.GemUtil.getAvailableGems(event.getEntityPlayer());

		if(held.size() > 0) {
			for (ItemStack stack : held) {
				if (stack.getItem() == ItemRegistry.MINDSPINDLE) {
					if (ArcaneGemItem.GemUtil.getCharge(stack) > 0) {
						int chargeReduction = event.getOrb().xpValue;
						event.getOrb().xpValue = Math.round(event.getOrb().xpValue * 1.5f);
						event.getOrb().delayBeforeCanPickup = 0;
						ArcaneGemItem.GemUtil.consumeCharge(stack, chargeReduction);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityHurt(LivingAttackEvent event) {
		if(!event.getEntity().world.isRemote) {
			if (event.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntity();

				if (!player.world.isRemote) {
					ArrayList<ItemStack> held = ArcaneGemItem.GemUtil.getAvailableGems(player);
					if (held.size() > 0) {
						for (ItemStack stack : held) {
							if (stack.getItem() == ItemRegistry.PHOENIXWAY) {
								if (player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
									continue;
								}
								if (ArcaneGemItem.GemUtil.getCharge(stack) >= 0) {
									if (event.getSource() == DamageSource.ON_FIRE || event.getSource() == DamageSource.IN_FIRE) {
										Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
										player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
										ArcaneGemItem.GemUtil.consumeCharge(stack, 12);
										event.setCanceled(true);
									}
								}
							}
						}
					}
				}
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

	@SubscribeEvent
	public static void onCriticalHitLanded (CriticalHitEvent event) {
		if(!event.getEntityPlayer().world.isRemote) {
			for (ItemStack stack : ArcaneGemItem.GemUtil.getAvailableGems(event.getEntityPlayer())) {
				if(stack.getItem() instanceof MurdergleamItem) {
					if(ArcaneGemItem.GemUtil.isToggledOn(stack) && ArcaneGemItem.GemUtil.getCharge(stack) > 0) {
						event.setDamageModifier(1.5f);
						event.setResult(Event.Result.ALLOW);
						ArcaneGemItem.GemUtil.consumeCharge(stack, 1);
						if(ArcaneGemItem.GemUtil.getCharge(stack) == 0) {
							ArcaneGemItem.GemUtil.swapToggle(stack);
						}
					}
					else if (event.isVanillaCritical()) {
						ArcaneGemItem.GemUtil.restoreCharge(stack, 3);
						if(ArcaneGemItem.GemUtil.getCharge(stack) == ArcaneGemItem.GemUtil.getMaxCharge(stack)) {
							ArcaneGemItem.GemUtil.swapToggle(stack);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if(!event.getEntityLiving().world.isRemote) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				if (event.getSource().getTrueSource() instanceof EntityLiving) {
					EntityLivingBase source = (EntityLivingBase) event.getSource().getTrueSource();
					EntityPlayer player = (EntityPlayer) event.getEntityLiving();
					for (ItemStack gem : ArcaneGemItem.GemUtil.getAvailableGems(player)) {
						if(gem.getItem() instanceof StormwayItem) {
							if(ArcaneGemItem.GemUtil.isToggledOn(gem) && ArcaneGemItem.GemUtil.getCharge(gem) > 0 && event.getSource().isProjectile()) {
								source.world.spawnEntity(new EntityLightningBolt(source.world, source.posX, source.posY + 0.5, source.posZ, false));

								if(source.isEntityUndead())
									source.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 10));
								else
									source.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 10));

								ArcaneGemItem.GemUtil.consumeCharge(gem, 1);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityStruckbyLightning(EntityStruckByLightningEvent event) {
		if(!event.getEntity().world.isRemote) {
			if(event.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntity();
				for (ItemStack gem : ArcaneGemItem.GemUtil.getAvailableGems(player)) {
					if(gem.getItem() instanceof StormwayItem) {
						if(ArcaneGemItem.GemUtil.getCharge(gem) > 0) {
							player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 2));
							player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 0));
							player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1200, 0));

							ArcaneGemItem.GemUtil.consumeCharge(gem, 6);
							ArcaneArchives.logger.info("fired lightning event");

							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			for(ItemStack gem : ArcaneGemItem.GemUtil.getAvailableGems(player)) {
				if(gem.getItem() instanceof StormwayItem) {
					if(ArcaneGemItem.GemUtil.getCharge(gem) < ArcaneGemItem.GemUtil.getMaxCharge(gem) && player.world.isRaining()) {
						ArcaneGemItem.GemUtil.restoreCharge(gem, -1);
						player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
					}
				}
			}
		}
	}
}
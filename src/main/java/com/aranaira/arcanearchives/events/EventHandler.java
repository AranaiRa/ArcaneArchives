package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.client.render.LineHandler;
import com.aranaira.arcanearchives.client.render.RenderGemcasting;
import com.aranaira.arcanearchives.client.render.RenderGemcasting.EnumGemGuiMode;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.PlayerSaveData;
import com.aranaira.arcanearchives.entity.EntityItemMountaintear;
import com.aranaira.arcanearchives.entity.ai.AIResonatorSit;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.integration.baubles.BaubleBodyCapabilityHandler;
import com.aranaira.arcanearchives.inventory.handlers.DevouringCharmHandler;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.TomeOfArcanaItem;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import com.aranaira.arcanearchives.items.gems.GemUtil.GemStack;
import com.aranaira.arcanearchives.items.gems.asscher.AgegleamItem;
import com.aranaira.arcanearchives.items.gems.asscher.MurdergleamItem;
import com.aranaira.arcanearchives.items.gems.asscher.SalvegleamItem;
import com.aranaira.arcanearchives.items.gems.asscher.Slaughtergleam;
import com.aranaira.arcanearchives.items.gems.pampel.Elixirspindle;
import com.aranaira.arcanearchives.items.gems.trillion.StormwayItem;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketConfig.RequestDefaultRoutingType;
import com.aranaira.arcanearchives.network.PacketConfig.RequestMaxDistance;
import com.aranaira.arcanearchives.network.PacketRadiantAmphora.Toggle;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.types.iterators.SlotIterable;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import epicsquid.mysticallib.util.Util;
import gigaherz.lirelent.guidebook.client.BookRegistryEvent;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thaumcraft.common.tiles.crafting.TileCrucible;
import vazkii.botania.api.item.IPetalApothecary;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber
public class EventHandler {
	@SubscribeEvent
	public static void onPlayerJoined (PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.world.isRemote) {
			RequestMaxDistance packet = new RequestMaxDistance();
			Networking.CHANNEL.sendTo(packet, (EntityPlayerMP) player);
			RequestDefaultRoutingType packet2 = new RequestDefaultRoutingType();
			Networking.CHANNEL.sendTo(packet2, (EntityPlayerMP) player);
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
		LineHandler.removeLine(event.getPos(), event.getEntity().dimension);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onLeftClickEmpty (PlayerInteractEvent.LeftClickEmpty event) {
		Item item = event.getEntityPlayer().inventory.getCurrentItem().getItem();
		if (item == ItemRegistry.RADIANT_AMPHORA) {
			Toggle packet = new Toggle();
			Networking.CHANNEL.sendToServer(packet);
		} else if (item instanceof ArcaneGemItem) {
			ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
			ArcaneGemItem agi = (ArcaneGemItem) stack.getItem();

			if (agi.hasToggleMode()) {
				com.aranaira.arcanearchives.network.PacketArcaneGems.Toggle packet = new com.aranaira.arcanearchives.network.PacketArcaneGems.Toggle();
				Networking.CHANNEL.sendToServer(packet);
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
			} else if (item == ItemRegistry.RAW_RADIANT_QUARTZ) {
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

	@Optional.Method(modid = "botania")
	public static void tryPetalApothecary (RightClickBlock event) {
		IPetalApothecary ipate = WorldUtil.getTileEntity(IPetalApothecary.class, event.getEntity().dimension, event.getPos());
		if (ipate != null) {
			boolean hasRivertear = GemUtil.getHasRivertear(event.getEntityPlayer());

			if (hasRivertear) {
				if (!ipate.hasWater()) {
					ipate.setWater(true);
				}

				//TODO: Decide whether to use charge
			}
		}
	}

	@Optional.Method(modid = "thaumcraft")
	public static void tryThaumcraftCrucible (RightClickBlock event) {
		TileCrucible tc = WorldUtil.getTileEntity(TileCrucible.class, event.getEntity().dimension, event.getPos());
		if (tc != null && tc.tank.getFluidAmount() == 0) {
			boolean hasRivertear = GemUtil.getHasRivertear(event.getEntityPlayer());

			if (hasRivertear) {
				int amount = tc.fill(new FluidStack(FluidRegistry.WATER, 1000), false);
				tc.fill(new FluidStack(FluidRegistry.WATER, amount), true);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock (PlayerInteractEvent.RightClickBlock event) {
		if (!event.getWorld().isRemote) {
			if (Loader.isModLoaded("botania")) {
				tryPetalApothecary(event);
			}

			if (Loader.isModLoaded("thaumcraft")) {
				tryThaumcraftCrucible(event);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void renderGemHUD (RenderGameOverlayEvent.Post event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.player;
		if (player == null) {
			return;
		}

		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			for (GemStack gem : GemUtil.getAvailableGems(player)) {
				EnumGemGuiMode mode = EnumGemGuiMode.SOCKET;
				if (gem.getStack().equals(player.getHeldItemMainhand())) {
					mode = EnumGemGuiMode.RIGHT;
				} else if (gem.getStack().equals(player.getHeldItemOffhand())) {
					mode = EnumGemGuiMode.LEFT;
				}

				RenderGemcasting.draw(minecraft, gem.getStack(), event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), mode);
			}
		}
	}

	@SubscribeEvent
	public static void playerPickupXP (PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player == null) {
			return;
		}

		AvailableGemsHandler held = GemUtil.getAvailableGems(event.getEntityPlayer());

		for (GemStack stack : held) {
			if (stack.getItem() == ItemRegistry.ORDERSTONE) {
				GemUtil.restoreCharge(stack, event.getOrb().xpValue);
			}
			if (stack.getItem() == ItemRegistry.MINDSPINDLE) {
				if (GemUtil.getCharge(stack) > 0) {
					int chargeReduction = event.getOrb().xpValue;
					event.getOrb().xpValue = Math.round(event.getOrb().xpValue * 1.5f);
					event.getOrb().delayBeforeCanPickup = 0;
					GemUtil.consumeCharge(stack, chargeReduction);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityHurt (LivingAttackEvent event) {
		if (!event.getEntity().world.isRemote) {
			if (event.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntity();
				AvailableGemsHandler held = GemUtil.getAvailableGems(player);
				for (GemStack stack : held) {
					if (stack.getItem() == ItemRegistry.PHOENIXWAY) {
						if (player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
							continue;
						}
						if (GemUtil.getCharge(stack) >= 0) {
							if (event.getSource() == DamageSource.ON_FIRE || event.getSource() == DamageSource.IN_FIRE) {
								player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
								player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
								GemUtil.consumeCharge(stack, 12);
								event.setCanceled(true);
							}
						}
					}
				}
			}
		}
	}

	@Optional.Method(modid = "gbook_snapshot")
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unused")
	public static void registerBook (BookRegistryEvent event) {
		event.register(TomeOfArcanaItem.TOME_OF_ARCANA, true);
	}

	@SubscribeEvent
	public static void onEntityJoinedWorld (EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityOcelot) {
			EntityOcelot ocelot = (EntityOcelot) entity;
			ocelot.tasks.addTask(6, new AIResonatorSit(ocelot, 0.8D));
		} else if (entity instanceof EntityItem && !(entity instanceof EntityItemMountaintear)) {
			ItemStack stack = ((EntityItem) entity).getItem();
			if (stack.getItem() == ItemRegistry.MOUNTAINTEAR) {
				if (!event.getWorld().isRemote) {
					EntityItemMountaintear mountaintear = new EntityItemMountaintear(event.getWorld(), entity.posX, entity.posY, entity.posZ, stack);
					mountaintear.setDefaultPickupDelay();
					mountaintear.motionX = entity.motionX;
					mountaintear.motionY = entity.motionY;
					mountaintear.motionZ = entity.motionZ;
					event.getWorld().spawnEntity(mountaintear);
				}
				entity.setDead();
			}
		}
	}

	@SubscribeEvent
	public static void onCriticalHitLanded (CriticalHitEvent event) {
		if (!event.getEntityPlayer().world.isRemote) {
			for (GemStack stack : GemUtil.getAvailableGems(event.getEntityPlayer())) {
				if (stack.getItem() instanceof MurdergleamItem) {
					if (GemUtil.isToggledOn(stack) && GemUtil.getCharge(stack) > 0) {
						event.setDamageModifier(1.5f);
						event.setResult(Event.Result.ALLOW);
						GemUtil.consumeCharge(stack, 1);
						if (GemUtil.getCharge(stack) == 0) {
							GemUtil.swapToggle(stack);
						}
					} else if (event.isVanillaCritical()) {
						GemUtil.restoreCharge(stack, 3);
						if (GemUtil.getCharge(stack) == GemUtil.getMaxCharge(stack)) {
							GemUtil.swapToggle(stack);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttacked (LivingAttackEvent event) {
		if (!event.getEntityLiving().world.isRemote) {
			if (event.getEntityLiving() instanceof EntityPlayer) {
				if (event.getSource().getTrueSource() instanceof EntityLiving) {
					EntityLivingBase source = (EntityLivingBase) event.getSource().getTrueSource();
					EntityPlayer player = (EntityPlayer) event.getEntityLiving();
					for (GemStack gem : GemUtil.getAvailableGems(player)) {
						if (gem.getItem() instanceof StormwayItem) {
							if (GemUtil.isToggledOn(gem) && GemUtil.getCharge(gem) > 0 && event.getSource().isProjectile()) {
								source.world.spawnEntity(new EntityLightningBolt(source.world, source.posX, source.posY + 0.5, source.posZ, false));

								if (source.isEntityUndead()) {
									source.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 10));
								} else {
									source.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 10));
								}

								GemUtil.consumeCharge(gem, 1);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityStruckbyLightning (EntityStruckByLightningEvent event) {
		if (!event.getEntity().world.isRemote) {
			if (event.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntity();
				for (GemStack gem : GemUtil.getAvailableGems(player)) {
					if (gem.getItem() instanceof StormwayItem) {
						if (GemUtil.getCharge(gem) > 0 && StormwayItem.canBeStruck(gem)) {
							player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 2));
							player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1200, 0));
							player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1200, 0));

							GemUtil.consumeCharge(gem, 6);
							StormwayItem.setStrikeCooldownTimer(gem);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityUpdate (LivingEvent.LivingUpdateEvent event) {
		if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			for (GemStack gem : GemUtil.getAvailableGems(player)) {
				/**
				 * Salvegleam
				 */
				if (gem.getItem() == ItemRegistry.SALVEGLEAM) {
					if (GemUtil.getCharge(gem) > 0 && GemUtil.isToggledOn(gem)) {
						if (player.getHealth() < player.getMaxHealth()) {
							if (SalvegleamItem.canDoHealingPulse(gem)) {
								player.heal(1.0f);
								GemUtil.consumeCharge(gem, 1);
								if (GemUtil.getCharge(gem) == 0) {
									GemUtil.setToggle(gem, false);
								}
							}
						}
					}
				}
				/**
				 * Stormway
				 */
				else if (gem.getItem() == ItemRegistry.STORMWAY) {
					if (GemUtil.getCharge(gem) < GemUtil.getMaxCharge(gem) && player.world.isRaining()) {
						GemUtil.restoreCharge(gem, -1);
						player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
					}
				}
				/**
				 * Agegleam
				 */
				else if (gem.getItem() == ItemRegistry.AGEGLEAM) {
					if (GemUtil.getCharge(gem) < GemUtil.getMaxCharge(gem)) {
						AgegleamItem.processRechargeTime(gem);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerAttackEntity (LivingDeathEvent event) {
		if (!event.getEntityLiving().world.isRemote) {
			if (event.getEntityLiving() instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) event.getEntityLiving();
				//ArcaneArchives.logger.info("i like the part where it stopped moving");
				if (event.getSource().getTrueSource() instanceof EntityPlayer) {
					for (GemStack gem : GemUtil.getAvailableGems((EntityPlayer) event.getSource().getTrueSource())) {
						if (gem.getItem() instanceof SalvegleamItem) {
							GemUtil.restoreCharge(gem, (int) event.getEntityLiving().getMaxHealth() / 2);
							if (GemUtil.getCharge(gem) == GemUtil.getMaxCharge(gem)) {
								GemUtil.setToggle(gem, true);
							}
						}
					}
				}
			}
		}
	}

	private static void givePlayerBookMaybe (EntityPlayer player, World world, boolean bookshelf) {
		PlayerSaveData save = DataHelper.getPlayerData(world, player);
		if (save.receivedBook) {
			return;
		}
		save.receivedBook = true;
		save.markDirty();
		ItemStack tome = new ItemStack(ItemRegistry.TOME_OF_ARCANA);
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(tome);
		tag.setString("Book", TomeOfArcanaItem.TOME_OF_ARCANA.toString());
		world.getMapStorage().saveAllData();
		EntityItem tomeEntity = new EntityItem(world, player.posX, player.posY, player.posZ, tome);
		tomeEntity.setPickupDelay(0);
		if (bookshelf) {
			player.sendMessage(new TextComponentTranslation("arcanearchives.message.book_received.bookshelf").setStyle(new Style().setColor(TextFormatting.GOLD)));
		} else {
			player.sendMessage(new TextComponentTranslation("arcanearchives.message.book_received.resonator").setStyle(new Style().setColor(TextFormatting.GOLD)));
		}
		world.spawnEntity(tomeEntity);
		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_CLOTH_FALL, SoundCategory.PLAYERS, 1f, 1f);
	}

	@SubscribeEvent
	public static void onPlayerBreakBlock (BreakEvent event) {
		if (!event.getWorld().isRemote && event.getState().getBlock() instanceof BlockBookshelf) {
			givePlayerBookMaybe(event.getPlayer(), event.getWorld(), true);
		}
	}

	@SubscribeEvent
	public static void onPlayerCrafted (ItemCraftedEvent event) {
		if (!event.player.world.isRemote) {
			Item item = event.crafting.getItem();
			if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() == BlockRegistry.RADIANT_RESONATOR) {
				givePlayerBookMaybe(event.player, event.player.world, false);
			} else if (item == ItemRegistry.TOME_OF_ARCANA) {
				World world = event.player.world;
				EntityPlayer player = event.player;
				PlayerSaveData save = DataHelper.getPlayerData(world, player);
				save.receivedBook = true;
				save.markDirty();
				world.getMapStorage().saveAllData();
			}
		}
	}

	@SubscribeEvent
	public static void onItemUse (LivingEntityUseItemEvent event) {
		/**
		 * Serverside
		 */
		if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack stack = player.getHeldItemMainhand();
			for (GemStack gem : GemUtil.getAvailableGems(player)) {
				/**
				 * Elixirspindle
				 */
				if (gem.getItem() instanceof Elixirspindle && GemUtil.getCharge(gem) > 0) {
					if (stack.getItem() instanceof ItemPotion) {
						PotionType potion = PotionUtils.getPotionFromItem(player.getHeldItemMainhand());
						boolean potionAlreadyActive = false;
						for (PotionEffect effect : potion.getEffects()) {
							if (!effect.getPotion().isInstant() && player.getActivePotionMap().containsKey(effect.getPotion())) {
								potionAlreadyActive = true;
								break;
							}
						}
						if (!potionAlreadyActive) {
							for (PotionEffect effect : PotionUtils.getEffectsFromStack(player.getHeldItemMainhand())) {
								player.addPotionEffect(new PotionEffect(effect));
							}
							GemUtil.consumeCharge(gem, 1);
						}
						event.setCanceled(true);
					}
				}
			}
		}

		/**
		 * Clientside
		 */
		if (event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack stack = player.getHeldItemMainhand();
			for (GemStack gem : GemUtil.getAvailableGems(player)) {
				/**
				 * Elixirspindle
				 */
				if (gem.getItem() instanceof Elixirspindle && GemUtil.getCharge(gem) > 0) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLooting (LootingLevelEvent event) {
		if (!event.getEntityLiving().world.isRemote) {
			if (event.getDamageSource().getTrueSource() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getDamageSource().getTrueSource();
				for (GemStack gem : GemUtil.getAvailableGems(player)) {
					if (gem.getItem() instanceof Slaughtergleam) {
						if (GemUtil.getCharge(gem) > 0) {
							event.setLootingLevel(event.getLootingLevel() + 1);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityLivingDeath (LivingDeathEvent event) {
		if (!event.getEntityLiving().world.isRemote) {
			if (event.getSource().getTrueSource() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
				for (GemStack gem : GemUtil.getAvailableGems(player)) {
					if (gem.getItem() instanceof Slaughtergleam) {
						if (GemUtil.getCharge(gem) > 0) {
							GemUtil.consumeCharge(gem, 1);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEndermanTeleport (EnderTeleportEvent event) {
		if (!event.getEntityLiving().world.isRemote) {
			EntityLivingBase target = event.getEntityLiving();
			double cubeRadius = 10.5;
			AxisAlignedBB aabb = new AxisAlignedBB(target.posX - cubeRadius, target.posY - cubeRadius, target.posZ - cubeRadius, target.posX + cubeRadius, target.posY + cubeRadius, target.posZ + cubeRadius);
			for (EntityPlayer player : target.world.getEntitiesWithinAABB(EntityPlayer.class, aabb)) {
				AvailableGemsHandler handler = GemUtil.getAvailableGems(player);
				for (Iterator<GemStack> it = handler.iterator(); it.hasNext(); ) {
					GemStack gem = it.next();
					if (gem.getItem() == ItemRegistry.SWITCHGLEAM) {
						GemUtil.restoreCharge(gem, 3);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	public static Object2LongOpenHashMap<EntityPlayer> shouldPlaySound = new Object2LongOpenHashMap<>();

	@SubscribeEvent
	public static void onItemPickup (EntityItemPickupEvent event) {
		if (event.getEntityPlayer() != null) {
			ArrayList<ItemStack> devouringCharms = new ArrayList<>();
			EntityPlayer player = event.getEntityPlayer();
			IItemHandler cap = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			for (ItemStack stack : new SlotIterable(cap)) {
				if (stack.getItem() == ItemRegistry.DEVOURING_CHARM) {
					devouringCharms.add(stack);
				}
			}

			long lastPlayed = shouldPlaySound.getOrDefault(player, -1L);
			boolean playSound = true;
			if ((System.currentTimeMillis() - lastPlayed) < 500 && lastPlayed != -1) {
				playSound = false;
			}
			EntityItem item = event.getItem();
			ItemStack stack = item.getItem();

			for (ItemStack dCharm : devouringCharms) {
				DevouringCharmHandler handler = DevouringCharmHandler.getHandler(dCharm);
				if (handler.shouldVoidItem(stack)) {
					stack.shrink(stack.getCount());
					item.setDead();
					event.setResult(Event.Result.DENY);
					World world = event.getEntityPlayer().world;

					if (!world.isRemote && playSound) {
						world.playSound(null, item.posX, item.posY, item.posZ, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.4f, 0.7f + Util.rand.nextFloat() * 0.6f);
						shouldPlaySound.put(player, System.currentTimeMillis());
					}
					break;
				}
			}
		}
	}

	@SubscribeEvent
	@Optional.Method(modid = "baubles")
	public static void onAttachCapabilities (AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() == ItemRegistry.BAUBLE_GEMSOCKET) {
			event.addCapability(BaubleBodyCapabilityHandler.NAME, BaubleBodyCapabilityHandler.instance);
		}
	}
}
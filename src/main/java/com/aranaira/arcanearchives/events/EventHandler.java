package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.client.render.RenderGemcasting;
import com.aranaira.arcanearchives.client.render.RenderGemcasting.EnumGemGuiMode;
import com.aranaira.arcanearchives.client.tracking.LineHandler;
import com.aranaira.arcanearchives.config.ServerSideConfig;
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
import com.aranaira.arcanearchives.network.PacketConfig.RequestTrovesDispense;
import com.aranaira.arcanearchives.network.PacketRadiantAmphora.Toggle;
import com.aranaira.arcanearchives.types.SlotIterable;
import com.aranaira.arcanearchives.util.ItemUtils;
import epicsquid.mysticallib.util.Util;
import gigaherz.lirelent.guidebook.client.BookRegistryEvent;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Mod;
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
  // TODO: Make into one packet
  @SubscribeEvent
  public static void onPlayerJoined(PlayerLoggedInEvent event) {
    PlayerEntity player = event.player;
    if (!player.world.isRemote) {
      RequestMaxDistance packet = new RequestMaxDistance();
      Networking.CHANNEL.sendTo(packet, (ServerPlayerEntity) player);
      RequestDefaultRoutingType packet2 = new RequestDefaultRoutingType();
      Networking.CHANNEL.sendTo(packet2, (ServerPlayerEntity) player);
      RequestTrovesDispense packet3 = new RequestTrovesDispense();
      Networking.CHANNEL.sendTo(packet3, (ServerPlayerEntity) player);
    }
  }

  // TODO: Handle this better
  @SubscribeEvent
  public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
    LineHandler.removeLine(event.getPos(), event.getEntity().dimension);
  }

  // TODO: AAAA
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
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

  // TODO: Pull in configuration update from 0.2.0.16
  @SubscribeEvent
  public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
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
          ItemEntity ei = new ItemEntity(event.getWorld(), pos.x, pos.y, pos.z, shards);
          ei.motionX = rng.nextFloat() * 0.4f - 0.2f;
          ei.motionZ = rng.nextFloat() * 0.4f - 0.2f;
          ei.motionY = rng.nextFloat() * 0.2f + 0.2f;
          event.getWorld().spawnEntity(ei);
        } else if (num == 1 || num == 2) {
          ItemStack shards = new ItemStack(BlockRegistry.QUARTZ_SLIVER, 1);
          Vec3d pos = event.getHitVec();
          ItemEntity ei = new ItemEntity(event.getWorld(), pos.x, pos.y, pos.z, shards);
          ei.motionX = rng.nextFloat() * 0.4f - 0.2f;
          ei.motionZ = rng.nextFloat() * 0.4f - 0.2f;
          ei.motionY = rng.nextFloat() * 0.2f + 0.2f;
          event.getWorld().spawnEntity(ei);
        }
      }
    }
  }

  @SubscribeEvent
  public static void onEntityJoinedWorld(EntityJoinWorldEvent event) {
    Entity entity = event.getEntity();
    if (entity instanceof OcelotEntity) {
      OcelotEntity ocelot = (OcelotEntity) entity;
      ocelot.tasks.addTask(6, new AIResonatorSit(ocelot, 0.8D));
    }
  }

  private static void givePlayerBookMaybe(PlayerEntity player, World world, boolean bookshelf) {
    PlayerSaveData save = DataHelper.getPlayerData(player);
    if (save.receivedBook) {
      return;
    }
    save.receivedBook = true;
    save.markDirty();
    ItemStack tome = new ItemStack(ItemRegistry.TOME_OF_ARCANA);
    CompoundNBT tag = ItemUtils.getOrCreateTagCompound(tome);
    tag.setString("Book", TomeOfArcanaItem.TOME_OF_ARCANA.toString());
    Objects.requireNonNull(world.getMapStorage()).saveAllData();
    ItemEntity tomeEntity = new ItemEntity(world, player.posX, player.posY, player.posZ, tome);
    tomeEntity.setPickupDelay(0);
    if (bookshelf) {
      player.sendMessage(new TranslationTextComponent("arcanearchives.message.book_received.bookshelf").setStyle(new Style().setColor(TextFormatting.GOLD)));
    } else {
      player.sendMessage(new TranslationTextComponent("arcanearchives.message.book_received.resonator").setStyle(new Style().setColor(TextFormatting.GOLD)));
    }
    world.spawnEntity(tomeEntity);
    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_CLOTH_FALL, SoundCategory.PLAYERS, 1f, 1f);
  }

  @SubscribeEvent
  public static void onPlayerBreakBlock(BreakEvent event) {
    if (ServerSideConfig.BookFromBookshelf && !event.getWorld().isRemote && event.getState().getBlock() instanceof BlockBookshelf) {
      givePlayerBookMaybe(event.getPlayer(), event.getWorld(), true);
    }
  }

  @SubscribeEvent
  public static void onPlayerCrafted(ItemCraftedEvent event) {
    if (!event.player.world.isRemote && ServerSideConfig.BookFromResonator) {
      Item item = event.crafting.getItem();
      if (item instanceof BlockItem && ((BlockItem) item).getBlock() == BlockRegistry.RADIANT_RESONATOR) {
        givePlayerBookMaybe(event.player, event.player.world, false);
      } else if (item == ItemRegistry.TOME_OF_ARCANA) {
        World world = event.player.world;
        PlayerEntity player = event.player;
        PlayerSaveData save = DataHelper.getPlayerData(player);
        save.receivedBook = true;
        save.markDirty();
        Objects.requireNonNull(world.getMapStorage()).saveAllData();
      }
    }
  }

  public static Object2LongOpenHashMap<PlayerEntity> shouldPlaySound = new Object2LongOpenHashMap<>();

  @SubscribeEvent
  public static void onItemPickup(EntityItemPickupEvent event) {
    if (event.getEntityPlayer() != null) {
      ArrayList<ItemStack> devouringCharms = new ArrayList<>();
      PlayerEntity player = event.getEntityPlayer();
      IItemHandler cap = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
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
      ItemEntity item = event.getItem();
      ItemStack stack = item.getItem();

      for (ItemStack dCharm : devouringCharms) {
        DevouringCharmHandler handler = DevouringCharmHandler.getHandler(dCharm);
        if (handler.shouldVoidItem(stack)) {
          stack.shrink(stack.getCount());
          item.setDead();
          event.setResult(Event.Result.DENY);
          World world = event.getEntityPlayer().world;

          if (!world.isRemote && playSound) {
            world.playSound(null, item.posX, item.posY, item.posZ, net.minecraft.util.SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.4f, 0.7f + Util.rand.nextFloat() * 0.6f);
            shouldPlaySound.put(player, System.currentTimeMillis());
          }
          break;
        }
      }
    }
  }
}

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
import com.aranaira.arcanearchives.network.PacketConfig.RequestTrovesDispense;
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
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
  public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
    PlayerEntity player = event.getPlayer();
    if (!player.world.isRemote) {
      RequestMaxDistance packet = new RequestMaxDistance();
      Networking.CHANNEL.sendTo(packet, (ServerPlayerEntity) player);
      RequestDefaultRoutingType packet2 = new RequestDefaultRoutingType();
      Networking.CHANNEL.sendTo(packet2, (ServerPlayerEntity) player);
      RequestTrovesDispense packet3 = new RequestTrovesDispense();
      Networking.CHANNEL.sendTo(packet3, (ServerPlayerEntity) player);
    }
  }

  @SubscribeEvent
  public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
    LineHandler.removeLine(event.getPos(), event.getEntity().dimension);
  }

  @SubscribeEvent
  public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
    if (!event.getWorld().isRemote) {
      Item item = event.getEntityPlayer().inventory.getCurrentItem().getItem();
      if (item == ItemRegistry.RAW_RADIANT_QUARTZ) {
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
}
package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.RadiantChest;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.common.AAItemStackHandler;
import com.aranaira.arcanearchives.items.TomeOfArcanaItem;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.typesafe.config.Config;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class ForgeEventHandler
{
	@SubscribeEvent
	public static void onBlockBreakEvent(BreakEvent event)
	{
		World world = event.getWorld();

		if (world.isRemote) return;

		IBlockState state = event.getState();

		if(state.getBlock() instanceof RadiantChest)
		{
			RadiantChestTileEntity rcte = (RadiantChestTileEntity)world.getTileEntity(event.getPos());

			if (rcte == null) return; // Probably should generate an error message in the event that there's an unlinked tile entity
			
			AAItemStackHandler handler = (AAItemStackHandler) rcte.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

			if (handler == null) return; // As above
			
			boolean allSlotsEmpty = true;
			
			for(int i=0; i<handler.getSlots(); i++)
			{
				if(!handler.getStackInSlot(i).isEmpty())
				{
					allSlotsEmpty = false;
					break;
				}
			}
			
			if(!allSlotsEmpty)
				event.setCanceled(true);
		}
		else if (state.getBlock() instanceof RadiantResonator)
		{
			((RadiantResonator) state.getBlock()).onBlockDestroyed(world, event.getPos(), state, event);
		}
	}

	@SubscribeEvent
	public static void onLivingDestroyBlockEvent(LivingDestroyBlockEvent event)
	{
		IBlockState state = event.getState();
		Entity entity = event.getEntityLiving();

		if(state.getBlock() instanceof RadiantChest)
		{
			event.setCanceled(true);
		}
		else if (!entity.world.isRemote && state.getBlock() instanceof RadiantResonator)
		{
			((RadiantResonator) state.getBlock()).onBlockDestroyed(entity.world, event.getPos(), state, event);
		}
	}

	@SubscribeEvent
	public static void onExplosionDetonateEvent(ExplosionEvent.Detonate event)
	{
		World world = event.getWorld();

		if (world.isRemote) return;

		List<BlockPos> positions = event.getAffectedBlocks();

		Map<BlockPos, IBlockState> resonators = new HashMap<>();

		for (BlockPos pos : positions)
		{
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof RadiantResonator)
			{
				resonators.put(pos, state);
			}
		}

		if (resonators.size() == 0) return;

		RadiantResonator res = null;

		for (Map.Entry<BlockPos, IBlockState> entry : resonators.entrySet())
		{
			IBlockState state = entry.getValue();
			BlockPos pos = entry.getKey();

			if (res == null) res = (RadiantResonator) state.getBlock();

			res.onBlockDestroyed(world, pos, state, event);
		}
	}
}
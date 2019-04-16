package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber
public class AAEventHandler
{
	@SubscribeEvent
	public static void onBlockBreakEvent(BreakEvent event)
	{
		if (!ConfigHandler.UnbreakableContainers) return;

		World w = event.getWorld();
		Block block = event.getState().getBlock();

		if(!w.isRemote && block == BlockRegistry.RADIANT_CHEST)
		{
			RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, w, event.getPos());
			if(te == null) return;

			// null everything
			IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

			if(handler == null) return;

			boolean allSlotsEmpty = true;

			// null everything
			for(int i = 0; i < handler.getSlots(); i++)
			{
				if(!handler.getStackInSlot(i).isEmpty())
				{
					allSlotsEmpty = false;
					break;
				}
			}

			if(!allSlotsEmpty) event.setCanceled(true);
		} else if(!w.isRemote && block == BlockRegistry.RADIANT_TROVE)
		{
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, w, event.getPos());
			if(te != null && !te.isEmpty())
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDestroyBlockEvent(LivingDestroyBlockEvent event)
	{
		if (!ConfigHandler.UnbreakableContainers) return;

		Block block = event.getState().getBlock();
		if(block == BlockRegistry.RADIANT_CHEST || block == BlockRegistry.RADIANT_TROVE)
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBlockActivated (PlayerInteractEvent.RightClickBlock event) {
		LineHandler.removeLine(event.getPos());
	}
}
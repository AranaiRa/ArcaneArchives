package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.entity.EntityGeneric;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventHandler
{
	@SubscribeEvent
	public static void onEntityHurt(LivingAttackEvent event)
	{
		ArcaneArchives.logger.info("DEBUG: bring the owwies");
		if(event.getEntityLiving() instanceof EntityGeneric)
		{
			if(event.isCancelable())
			{
				ArcaneArchives.logger.info("DEBUG: lol no.");
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemUseOnEntity(PlayerInteractEvent.EntityInteractSpecific event)
	{
		ArcaneArchives.logger.info("DEBUG: clicky times with "+event.getEntityPlayer().getHeldItemMainhand().getItem().getRegistryName());
		if(event.getEntityLiving() instanceof EntityGeneric)
		{
			ArcaneArchives.logger.info("DEBUG: omg is a thing");
		}
	}
}
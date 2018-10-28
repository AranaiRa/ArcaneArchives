package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.SpiritData;
import com.aranaira.arcanearchives.entity.SpiritGeneric;
import com.aranaira.arcanearchives.items.EnchainmentLatticeItem;
import com.aranaira.arcanearchives.items.TomeOfArcanaItem;
import com.typesafe.config.Config;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.Int;

@Mod.EventBusSubscriber
public class ForgeEventHandler
{
	@SubscribeEvent
	public static void onEntityHurt(LivingAttackEvent event)
	{
		if(event.getEntityLiving() instanceof SpiritGeneric)
		{
			if(event.getSource() == DamageSource.OUT_OF_WORLD)
			{
				
			}
			else if(event.isCancelable())
			{
				event.setCanceled(true);
			}
		}
		
		
	}
	
	@SubscribeEvent
	public static void onItemUseOnEntity(PlayerInteractEvent.EntityInteractSpecific event)
	{
		ArcaneArchives.logger.info("DEBUG: clicky times");
		
		if(event.getTarget() instanceof SpiritGeneric)
		{
			SpiritGeneric target = (SpiritGeneric)event.getTarget();
			if(!target.getHasBeenProcessed())
			{
				if(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof EnchainmentLatticeItem)
				{
					if(ConfigHandler.ConfigGeneral.bDoSpiritCaptureMinigame)
					{
						ArcaneArchives.logger.info("minigame not implemented yet!");
					}
					else
					{
						target.setProcessed(true);
						SpiritData data = new SpiritData();
						String log = "spirit {type="+data.getType().toString()+" "+data.getAge().toString()+", "+data.getPersonality().getName()+"}";
						event.getTarget().attackEntityFrom(DamageSource.OUT_OF_WORLD, Int.MaxValue());
						ArcaneArchives.logger.info(log);
					}
				}
			}
		}
	}
}
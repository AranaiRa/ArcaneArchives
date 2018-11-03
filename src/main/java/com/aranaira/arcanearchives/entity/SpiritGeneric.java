package com.aranaira.arcanearchives.entity;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import scala.Int;

public class SpiritGeneric extends EntityCreature {

	private float animTime = 0;
	public SpiritAnimState animState = SpiritAnimState.IDLE_IN;
	public boolean hasBeenProcessed = false;
	
	public SpiritGeneric(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 1.0F);
		setEntityInvulnerable(true);
	}

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }
	
	public void setProcessed(boolean state)
	{
		hasBeenProcessed = state;
	}
	
	public boolean getHasBeenProcessed()
	{
		return hasBeenProcessed;
	}
	
	public SpiritAnimState getAnimState()
	{
		return animState;
	}
	
	public void setAnimState(SpiritAnimState state)
	{
		animState = state;
	}
	
	public float getAnimTime()
	{
		return animTime;
	}
	
	public void addAnimTime(float delta)
	{
		animTime += delta;
	}
	
	public void resetAnimTime()
	{
		animTime = 0;
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Int.MaxValue());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.625D);
	}
	
	public enum SpiritAnimState
	{
		IDLE_IN, IDLE_OUT
	}
}

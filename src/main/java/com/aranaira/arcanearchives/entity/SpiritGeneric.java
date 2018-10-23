package com.aranaira.arcanearchives.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import scala.Int;

public class SpiritGeneric extends EntityCreature {

	public boolean hasBeenProcessed = false;
	
	public SpiritGeneric(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 1.0F);
		setEntityInvulnerable(true);
	}
	
	public void setProcessed(boolean state)
	{
		hasBeenProcessed = state;
	}
	
	public boolean getHasBeenProcessed()
	{
		return hasBeenProcessed;
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Int.MaxValue());
	}
}

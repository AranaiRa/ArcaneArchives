package com.aranaira.arcanearchives.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EntityGeneric extends EntityCreature {

	public EntityGeneric(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 1.0F);
		setEntityInvulnerable(true);
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
	}
}

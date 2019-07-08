package com.aranaira.arcanearchives.advancements;

import com.google.gson.JsonElement;
import epicsquid.mysticallib.advancement.IGenericPredicate;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;

public class JoinPredicate implements IGenericPredicate<Void> {
	@Override
	public boolean test (EntityPlayerMP entityPlayerMP, Void aVoid) {
		return true;
	}

	@Override
	public IGenericPredicate<Void> deserialize (@Nullable JsonElement jsonElement) {
		return new JoinPredicate();
	}
}

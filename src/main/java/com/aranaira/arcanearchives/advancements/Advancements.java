package com.aranaira.arcanearchives.advancements;

import com.aranaira.arcanearchives.ArcaneArchives;
import epicsquid.mysticallib.advancement.GenericTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.UUID;

public class Advancements {
	public static final ResourceLocation JOIN_NETWORK_ID = new ResourceLocation(ArcaneArchives.MODID, "join_network");
	public static final ResourceLocation LEAVE_NETWORK_ID = new ResourceLocation(ArcaneArchives.MODID, "leave_network");

	public static GenericTrigger<Void> JOIN_NETWORK_TRIGGER;
	public static GenericTrigger<Void> LEAVE_NETWORK_TRIGGER;

	public static void init () {
		JOIN_NETWORK_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(JOIN_NETWORK_ID, new JoinPredicate()));
		LEAVE_NETWORK_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(LEAVE_NETWORK_ID, new LeavePredicate()));
	}
}

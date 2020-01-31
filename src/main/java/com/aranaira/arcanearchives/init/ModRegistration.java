package com.aranaira.arcanearchives.init;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class ModRegistration<T extends IForgeRegistryEntry<T>> {
	public ModRegistration () {
	}

	protected List<T> toRegister = new ArrayList<>();

	public void add (T entry) {
		if (entry != null) {
			toRegister.add(entry);
		}
	}

	public void onRegister (Register<T> event) {
		toRegister.forEach(event.getRegistry()::register);
	}
}

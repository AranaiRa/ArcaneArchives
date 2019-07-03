package com.aranaira.arcanearchives.tileentities;

import net.minecraft.util.ITickable;

public class FakeAirTileEntity extends AATileEntity implements ITickable {
	// Time in ticks it takes for this to decay.
	public static int FAKE_AIR_COOLDOWN = 200;

	private int ticker;

	public FakeAirTileEntity () {
		super();
		setName("fake_air");
		this.ticker = FAKE_AIR_COOLDOWN;
	}

	@Override
	public void update () {
		this.ticker--;

		if (this.ticker <= 0) {
			world.setBlockToAir(getPos());
		}
	}
}

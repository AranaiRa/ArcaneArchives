package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.api.immanence.*;
import com.aranaira.arcanearchives.data.IHiveBase;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.IteRef;

import java.util.ArrayList;
import java.util.List;

public class ImmanenceBus implements IImmanenceBus {
	private List<IImmanenceConsumer> consumers = new ArrayList<>();
	private List<IImmanenceGenerator> generators = new ArrayList<>();
	private final IHiveBase owner;
	private float multiplier = -1;
	private int base, leftover, total = -1;
	private int lastTick = -1;
	private long lastTickTime = -1;

	public ImmanenceBus (IHiveBase owner) {
		this.owner = owner;
	}

	private void buildTemporaryList (int currentTick) {
		if (lastTick != currentTick) {
			lastTickTime = System.currentTimeMillis();
			lastTick = currentTick;

			generators.clear();
			consumers.clear();
			for (IteRef ref : owner.getImmananceTiles()) {
				ImmanenceTileEntity te = ref.getTile();
				if (te instanceof IImmanenceConsumer) {
					consumers.add((IImmanenceConsumer) te);
				} else if (te instanceof IImmanenceGenerator) {
					generators.add((IImmanenceGenerator) te);
				}
			}
		}
	}

	@Override
	public void generateImmanence (int currentTick) {
		buildTemporaryList(currentTick);
		List<IImmanenceSource> sources = new ArrayList<>();
		base = 0;
		total = 0;
		multiplier = 1f;

		for (IImmanenceGenerator generator : generators) {
			IImmanenceSource source = generator.generateImmanence();
			if (source != null) {
				sources.add(source);
			}
		}

		for (IImmanenceSource source : sources) {
			if (source.getType() == ImmanenceBonusType.ADDITIVE) {
				base += source.getAmount();
			} else if (source.getType() == ImmanenceBonusType.MULTIPLICATIVE) {
				multiplier *= (1.0f + source.getAmount());
			}
		}

		total = Math.round(base * multiplier);
	}

	@Override
	public void consumeImmanence (int currentTick) {
		if (total == -1) {
			generateImmanence(currentTick);
		}

		int curTotal = total;

		for (IImmanenceConsumer consumer : consumers) {
			int wanted = consumer.requiredImmanence();
			if (wanted > curTotal) {
				consumer.acceptImmanence(0);
			} else {
				curTotal -= wanted;
				consumer.acceptImmanence(wanted);
			}
		}

		if (curTotal > 0) {
			leftover = curTotal;
		}
	}

	@Override
	public int getTotalImmanence () {
		return total;
	}

	@Override
	public int getLeftoverImmanence () {
		return leftover;
	}

	@Override
	public int getBaseImmanence () {
		return base;
	}

	@Override
	public float getMultiplier () {
		return multiplier;
	}

	@Override
	public long getLastTickTime () {
		return lastTickTime;
	}
}

package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.data.IHiveBase;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.IteRef;

import java.util.ArrayList;
import java.util.List;

public class ImmanenceBus implements IImmanenceBus {
	private List<IImmanenceConsumer> consumers = new ArrayList<>();
	private List<IImmanenceGenerator> generators = new ArrayList<>();
	private final IHiveBase owner;
	private int lastTotalImmanence = -1;
	private int lastLeftoverImmanence = -1;
	private int lastTick = -1;

	public ImmanenceBus (IHiveBase owner) {
		this.owner = owner;
	}

	private void buildTemporaryList (int currentTick) {
		if (lastTick != currentTick) {
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
		lastTotalImmanence = -1;
		int incoming = 0;

		for (IImmanenceGenerator generator : generators) {
			if (!generator.canGenerateImmanence()) {
				continue;
			}

			incoming += generator.generateImmanence();
		}

		if (incoming > 0) {
			lastTotalImmanence = incoming;
		}
	}

	@Override
	public void consumeImmanence (int currentTick) {
		if (lastTotalImmanence == -1) {
			generateImmanence(currentTick);
		}

		if (lastTotalImmanence == -1) {
			return;
		}

		int currentImmanence = lastTotalImmanence;
		lastLeftoverImmanence = -1;

		for (IImmanenceConsumer consumer : consumers) {
			int wanted = consumer.requiredImmanence();
			if (wanted > currentImmanence) {
				consumer.acceptImmanence(0);
			} else {
				currentImmanence -= wanted;
				consumer.acceptImmanence(wanted);
			}
		}

		if (currentImmanence > 0) {
			lastLeftoverImmanence = currentImmanence;
		}
	}

	@Override
	public int totalNetworkImmanence () {
		return lastTotalImmanence;
	}

	@Override
	public int leftoverImmanence () {
		return lastLeftoverImmanence;
	}
}

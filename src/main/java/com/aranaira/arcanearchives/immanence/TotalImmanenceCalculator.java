package com.aranaira.arcanearchives.immanence;

import java.util.HashMap;

public class TotalImmanenceCalculator {

	private HashMap<String, ImmanenceSource> sources = new HashMap<>();

	public TotalImmanenceCalculator () {

	}

	public int getTotalImmanence () {
		float baseImmanence = 0;
		float immanenceMultiplier = 1.0f;
		for (ImmanenceSource source : sources.values()) {
			if (source.getType() == ImmanenceBonusType.ADDITIVE) {
				baseImmanence += source.getAmount();
			} else if (source.getType() == ImmanenceBonusType.MULTIPLICATIVE) {
				immanenceMultiplier *= (1.0f + source.getAmount());
			}
		}

		return Math.round(baseImmanence * immanenceMultiplier);
	}

	public void addSource (String category, ImmanenceSource source) {
		if (sources.containsKey(category)) {
			if (sources.get(category).getAmount() < source.getAmount()) {
				sources.put(category, source);
			}
		}
	}

	public void addSource (String category, float amount, ImmanenceBonusType type) {
		addSource(category, new ImmanenceSource(amount, type));
	}

	public void removeSource (String category) {
		sources.remove(category);
	}

	public class ImmanenceSource {
		private float bonusAmount;
		private ImmanenceBonusType bonusType;

		public ImmanenceSource (float amount, ImmanenceBonusType type) {
			bonusAmount = amount;
			bonusType = type;
		}

		public float getAmount () {
			return bonusAmount;
		}

		public ImmanenceBonusType getType () {
			return bonusType;
		}
	}

	public enum ImmanenceBonusType {
		ADDITIVE, MULTIPLICATIVE
	}
}
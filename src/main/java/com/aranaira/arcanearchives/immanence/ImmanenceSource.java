package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.api.immanence.IImmanenceSource;
import com.aranaira.arcanearchives.api.immanence.ImmanenceBonusType;

public class ImmanenceSource implements IImmanenceSource {
  private float amount;
  private ImmanenceBonusType type;
  private String category;

  public ImmanenceSource(String category, float amount, ImmanenceBonusType type) {
    this.amount = amount;
    this.type = type;
    this.category = category;
  }

  @Override
  public float getAmount() {
    return amount;
  }

  @Override
  public ImmanenceBonusType getType() {
    return type;
  }

  @Override
  public String getCategory() {
    return category;
  }
}

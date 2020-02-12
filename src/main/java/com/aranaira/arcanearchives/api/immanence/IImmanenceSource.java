package com.aranaira.arcanearchives.api.immanence;

public interface IImmanenceSource {
  float getAmount();

  ImmanenceBonusType getType();

  String getCategory();
}

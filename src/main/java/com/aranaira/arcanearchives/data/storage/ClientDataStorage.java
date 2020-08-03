package com.aranaira.arcanearchives.data.storage;

import com.aranaira.arcanearchives.data.client.ClientNameData;

import javax.annotation.Nullable;

public class ClientDataStorage {
  private static ClientNameData data = null;

  @Nullable
  public static ClientNameData getData() {
    return data;
  }

  public static void setNameData(ClientNameData data) {
    ClientDataStorage.data = data;
  }
}

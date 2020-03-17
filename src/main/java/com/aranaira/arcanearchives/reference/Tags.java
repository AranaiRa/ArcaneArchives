package com.aranaira.arcanearchives.reference;

public interface Tags {
  String recipe = "Rec";
  String inventory = "Inv";
  String tileId = "TileId";
  String networkId = "NetId";

  interface CrystalWorkbench {
    String inputInventory = "InvIn";
    String outputInventory = "InvOut";
  }

  interface MakeshiftResonator {
    String currentTick = "CurTick";
  }

  interface PlayerSaveData {
    String receivedBook = "RecBook";
  }
}

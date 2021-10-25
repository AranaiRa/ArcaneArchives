package com.aranaira.arcanearchives.api.reference;

public interface Identifiers {
  String recipe = "Rec";
  String inventory = "Inv";
  String inventoryName = "InvName";
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

  interface RadiantChest {
    String customName = "CusName";
    String displayItem = "DisItem";
    String displayFacing = "DisFacing";
  }

  interface Resonator {
    String progress = "Prog";
  }

  interface NetworkReference {
    String networkIds = "NetIds";
  }
}

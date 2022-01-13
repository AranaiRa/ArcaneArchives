package com.aranaira.arcanearchives.api.reference;

public interface Identifiers {
  String recipe = "Rec";
  String inventory = "Inv";
  String inventoryName = "InvName";
  String tileId = "TileId";
  String tileName = "TileName";
  String networkId = "NetId";
  String networkName = "NetworkName";

  String BlockEntityTag = "BlockEntityTag";

  interface Data {
    String uniqueNames = "UniqueNames";
    String slotDataList = "SlotData";
    String slotUUID = "SlotUUID";
    String slotValue = "SlotValue";
    String slotPlayerId = "SlotPlayerUUID";
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
    String countdown = "Countdown";
  }

  interface MakeshiftResonator extends Resonator{
    String filled = "Filled";
  }

  interface NetworkReference {
    String networkIds = "NetIds";
  }

  static String blockEntityTag (String tag) {
    return BlockEntityTag + "." + tag;
  }
}

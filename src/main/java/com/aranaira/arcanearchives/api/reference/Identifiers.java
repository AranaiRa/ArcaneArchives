package com.aranaira.arcanearchives.api.reference;

public interface Identifiers {
  String recipe = "Rec";
  String inventory = "Inv";
  String inventoryName = "InvName";
  String entityId = "EntId";
  String entityName = "EntName";
  String domainId = "DomId";
  String domainName = "DomName";

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

  interface DomainReference {
    String domainIds = "DomIds";
  }

  static String blockEntityTag (String tag) {
    return BlockEntityTag + "." + tag;
  }
}

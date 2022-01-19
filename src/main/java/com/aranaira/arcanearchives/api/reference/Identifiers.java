package com.aranaira.arcanearchives.api.reference;

public interface Identifiers {
  String recipe = "Rec";
  String inventory = "Inv";
  String inventoryName = "InvName";
  String entityId = "EntId";
  String entityName = "EntName";
  String entityType = "EntType";
  String domainId = "DomId";
  String domainName = "DomName";

  String BlockEntityTag = "BlockEntityTag";

  interface Data {
    String uniqueNames = "UniqueNames";
    String slotDataList = "SlotData";
    String slotUUID = "SlotUUID";
    String slotValue = "SlotValue";
    String slotPlayerId = "SlotPlayerUUID";
    String position = "Pos";
    String dimension = "Dim";
    String priority = "Prio";
    String lastUpdated = "LastU";
    String clazz = "Clz";
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

  interface DomainEntryData {
    String domainEntries = "DomEntries";
  }

  static String blockEntityTag (String tag) {
    return BlockEntityTag + "." + tag;
  }
}

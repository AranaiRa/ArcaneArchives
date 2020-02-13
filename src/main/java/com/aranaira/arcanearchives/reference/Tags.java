package com.aranaira.arcanearchives.reference;

public interface Tags {
  String RECIPE = "Recipe";
  String INVENTORY = "Inventory";

  interface CrystalWorkbench {
    String INPUT_INVENTORY = "Input Inventory";
    String OUTPUT_INVENTORY = "Output Inventory";
  }

  interface MakeshiftResonator {
    String CURRENT_TICK = "Current Tick";
  }

  interface PlayerSaveData {
    String RECEIVED_BOOK = "received_book";
  }
}

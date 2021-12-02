package com.aranaira.arcanearchives.api.reference;

public interface Constants {
  interface CrystalWorkbench {
    int RecipeSlots = 7;

    interface DataArray {
      int SlotOffset = 0;
      int SlotSelected = 1;
      int DeattuneProgress = 2;
      int AttuneProgress = 3;
      int DimSlot1 = 4;
      int DimSlot2 = 5;
      int DimSlot3 = 6;
      int DimSlot4 = 7;
      int DimSlot5 = 8;
      int DimSlot6 = 9;
      int DimSlot7 = 10;
      int DimSlotStart = DimSlot1;
      int DimSlotStop = DimSlot7;
      int Count = DimSlot7;
    }

    interface UI {
      int Overlay = 0xaa1e3340;
      int OverlaySimple = 0x80808080;
    }
  }
}

package com.aranaira.arcanearchives.api.reference;

public interface Constants {
  interface CrystalWorkbench {
    int RecipeSlots = 7;

    interface DataArray {
      int SlotOffset = 0;
      int SlotSelected = 1;
      int DeattuneProgress = 2;
      int AttuneProgress = 3;
      int Count = AttuneProgress;
    }

    interface UI {
      int Overlay = 0xaa1e3340;
      int OverlaySimple = 0x80808080;
    }
  }

  interface Interface {
    static int getCycleTimer () {
      return (int) ((Math.random() * 10000) % Integer.MAX_VALUE);
    }
  }
}

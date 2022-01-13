package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.tterrag.registrate.providers.ProviderType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModLang {
  private static final char[] NETWORK_SYMBOLS = new char[]{'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z', '?'};

  static {
    REGISTRATE.addDataGenerator(ProviderType.LANG, (ctx) -> {
      ctx.add(UUIDNameData.Name.NAME_PREFIX + "name", "%s%s%s");

      for (int i = 0; i < 20; i++) {
        char c = NETWORK_SYMBOLS[i];
        ctx.add(UUIDNameData.Name.NAME_PREFIX + i, String.valueOf(c));
      }

      ctx.add("arcanearchives.tooltip.crystal_workbench.more_info", "[Hold Shift to cycle eligible items.]");
      ctx.add("arcanearchives.tooltip.crystal_workbench.info", "%s: %s/%s");

      ctx.add("arcanearchives.message.item_attuned", "%s was attuned to the network, %s.");
    });
  }

  public static void load () {

  }
}

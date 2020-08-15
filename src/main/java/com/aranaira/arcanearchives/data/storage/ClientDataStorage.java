package com.aranaira.arcanearchives.data.storage;

import com.aranaira.arcanearchives.tilenetwork.NetworkName;
import com.aranaira.arcanearchives.data.client.ClientNameData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

public class ClientDataStorage {
  private static ClientNameData nameData = null;

  @Nullable
  public static ClientNameData getNameData() {
    return nameData;
  }

  public static void setNameData(ClientNameData data) {
    ClientDataStorage.nameData = data;
  }

  @Nullable
  public static NetworkName getNameFor (UUID uuid) {
    if (nameData == null) {
      return null;
    }
    return nameData.getNameFor(uuid);
  }

  @SideOnly(Side.CLIENT)
  public static String invalidName = null;

  @SideOnly(Side.CLIENT)
  public static String getStringFor (UUID uuid) {
    NetworkName name = getNameFor(uuid);
    if (name == null) {
      if (invalidName == null) {
        invalidName = I18n.format("arcanearchives.network.name.invalid");
      }

      return invalidName;
    } else {
      return name.getName();
    }
  }
}

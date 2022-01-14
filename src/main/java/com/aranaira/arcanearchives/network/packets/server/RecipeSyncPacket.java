package com.aranaira.arcanearchives.network.packets.server;

import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.inventory.container.CrystalWorkbenchContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.noobutil.getter.Getter;

public class RecipeSyncPacket implements IPacket {
  private final ResourceLocation recipe;
  private final int containerId;

  public RecipeSyncPacket(ResourceLocation recipe, int containerId) {
    this.recipe = recipe;
    this.containerId = containerId;
  }

  public RecipeSyncPacket(PacketBuffer buffer) {
    this.recipe = new ResourceLocation(buffer.readUtf());
    this.containerId = buffer.readInt();
  }

  public void encode(PacketBuffer buffer) {
    buffer.writeUtf(this.recipe.toString());
    buffer.writeInt(this.containerId);
  }

  public void handle(NetworkEvent.Context ctx) {
    PlayerEntity player = Getter.getPlayer();
    if (player != null && player.containerMenu.containerId == this.containerId && player.containerMenu instanceof CrystalWorkbenchContainer) {
      ((CrystalWorkbenchContainer) player.containerMenu).setRecipe(this.recipe);
    }
  }
}

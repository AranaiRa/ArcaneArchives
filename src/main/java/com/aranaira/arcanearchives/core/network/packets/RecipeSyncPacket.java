package com.aranaira.arcanearchives.core.network.packets;

import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.noobutil.getter.Getter;

import java.util.function.Supplier;

public class RecipeSyncPacket {
  private final ResourceLocation recipe;
  private final int containerId;

  public RecipeSyncPacket(ResourceLocation recipe, int containerId) {
    this.recipe = recipe;
    this.containerId = containerId;
  }

  public RecipeSyncPacket (PacketBuffer buffer) {
    this.recipe = new ResourceLocation(buffer.readUtf());
    this.containerId = buffer.readInt();
  }

  public void encode (PacketBuffer buffer){
    buffer.writeUtf(this.recipe.toString());
    buffer.writeInt(this.containerId);
  }

  public void handle (Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      PlayerEntity player = Getter.getPlayer();
      if (player != null && player.containerMenu.containerId == this.containerId && player.containerMenu instanceof CrystalWorkbenchContainer) {
        ((CrystalWorkbenchContainer)player.containerMenu).setRecipe(this.recipe);
      }
    });
    ctx.get().setPacketHandled(true);
  }
}

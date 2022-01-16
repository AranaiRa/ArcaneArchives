package com.aranaira.arcanearchives.block.entity;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.block.entity.IDomainBlockEntity;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.domain.DomainManager;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class DomainIdentifiedBlockEntity extends IdentifiedBlockEntity implements IDomainBlockEntity {
  protected UUID domainId = null;
  protected UUIDNameData.Name domainName = null;

  public DomainIdentifiedBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public void onLoad() {
    if (getLevel() == null) {
      throw new IllegalStateException("level should never be null in `TileEntity::onLoad`");
    }
    if (getLevel().isClientSide()) {
      return;
    }
    DomainManager.register(this);
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    if (getDomainId() != null && domainId != UNKNOWN) {
      compound.putUUID(Identifiers.domainId, getDomainId());
    }
    return super.save(compound);
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    if (compound.hasUUID(Identifiers.domainId)) {
      this.domainId = compound.getUUID(Identifiers.domainId);
    }
    super.load(state, compound);
    ArcaneArchives.LOG.info("Tile entity info loaded for " + this + " at " + getBlockPos());
  }

  @Override
  public CompoundNBT getUpdateTag() {
    CompoundNBT updateTag = super.getUpdateTag();
    if (getDomainId() == null) {
      return updateTag;
    }

    //noinspection ConstantConditions
    UUID networkId = getDomainId();
    updateTag.putUUID(Identifiers.domainId, networkId);
    UUIDNameData.Name name = getDomainName();
    if (name != null && !name.isEmpty()) {
      updateTag.put(Identifiers.domainName, name.serializeNBT());
    }
    return updateTag;
  }

  @Nullable
  @Override
  public UUID getDomainId() {
    return domainId;
  }

  public void setDomainId(UUID id) {
    this.domainId = id;
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    if (pkt.getType() == 9) {
      CompoundNBT tag = pkt.getTag();
      if (tag.hasUUID(Identifiers.domainId)) {
        this.domainId = pkt.getTag().getUUID(Identifiers.domainId);
      }
      if (tag.contains(Identifiers.domainName)) {
        this.domainName = UUIDNameData.Name.fromNBT(tag.getCompound(Identifiers.domainName));
      }
    }

    super.onDataPacket(net, pkt);
  }

  @Override
  public UUIDNameData.Name getDomainName() {
    if (domainName == null) {
      LogicalSide side = EffectiveSide.get();
      if (side.isClient()) {
        return UUIDNameData.Name.EMPTY;
      } else {
        if (getDomainId() == null) {
          return UUIDNameData.Name.EMPTY;
        }
        domainName = DataStorage.getEntityName(getDomainId());
      }
    }

    return domainName;
  }

  @Override
  public boolean isDomainUnknown() {
    return entityId == null || entityId == UNKNOWN;
  }
}

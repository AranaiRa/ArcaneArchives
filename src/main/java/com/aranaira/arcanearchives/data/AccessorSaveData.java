package com.aranaira.arcanearchives.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccessorSaveData extends WorldSavedData {
	public static final String ID = "Arcane-Archives-Accessors";

	public Int2ObjectOpenHashMap<Long2LongOpenHashMap> accessorToParent = new Int2ObjectOpenHashMap<>();
	public Int2ObjectOpenHashMap<Long2ObjectOpenHashMap<LongArrayList>> parentToAccesors = new Int2ObjectOpenHashMap<>();

	private Long2LongOpenHashMap getDimensionATP (int dimension) {
		return accessorToParent.computeIfAbsent(dimension, (o) -> new Long2LongOpenHashMap());
	}

	private Long2ObjectOpenHashMap<LongArrayList> getDimensionTAP (int dimension) {
		return parentToAccesors.computeIfAbsent(dimension, (o) -> new Long2ObjectOpenHashMap<>());
	}

	@Nullable
	public BlockPos getParent (int dimension, BlockPos pos) {
		long result = getDimensionATP(dimension).get(pos.toLong());
		if (result == 0) {
			return null;
		}
		return BlockPos.fromLong(result);
	}

	public List<BlockPos> getAccessors (int dimension, BlockPos pos) {
		List<BlockPos> result = new ArrayList<>();
		LongArrayList data = getDimensionTAP(dimension).get(pos.toLong());
		return data.stream().map(BlockPos::fromLong).collect(Collectors.toList());
	}

	public void setParent (int dimension, BlockPos accessor, BlockPos parent) {
		Long2LongOpenHashMap dimMap = getDimensionATP(dimension);
		dimMap.put(accessor.toLong(), parent.toLong());
		this.markDirty();
	}

	public void setAccessors (int dimension, BlockPos parent, List<BlockPos> accessors) {
		Long2ObjectOpenHashMap<LongArrayList> dimMap = getDimensionTAP(dimension);
		dimMap.put(parent.toLong(), new LongArrayList(accessors.stream().map(BlockPos::toLong).collect(Collectors.toList())));
		this.markDirty();
	}

	public void addAccessor (int dimension, BlockPos parent, BlockPos accessor) {
		Long2ObjectOpenHashMap<LongArrayList> dimMap = getDimensionTAP(dimension);
		LongArrayList storage = dimMap.computeIfAbsent(parent.toLong(), (o) -> new LongArrayList());
		storage.add(accessor.toLong());
		this.markDirty();
	}

	public AccessorSaveData () {
		super(ID);
	}

	public AccessorSaveData (String name) {
		super(name);
	}

	@Override
	public void readFromNBT (NBTTagCompound nbt) {

	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		return null;
	}
}

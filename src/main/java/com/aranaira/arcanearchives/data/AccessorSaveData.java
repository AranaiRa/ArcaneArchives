package com.aranaira.arcanearchives.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

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
		Long2LongOpenHashMap reverseMap = getDimensionATP(dimension);
		long parentLong = parent.toLong();
		for (BlockPos accessor : accessors) {
			reverseMap.put(accessor.toLong(), parentLong);
		}
		this.markDirty();
	}

	public void addAccessor (int dimension, BlockPos parent, BlockPos accessor) {
		Long2ObjectOpenHashMap<LongArrayList> dimMap = getDimensionTAP(dimension);
		LongArrayList storage = dimMap.computeIfAbsent(parent.toLong(), (o) -> new LongArrayList());
		storage.add(accessor.toLong());
		Long2LongOpenHashMap reverseMap = getDimensionATP(dimension);
		reverseMap.put(parent.toLong(), accessor.toLong());
		this.markDirty();
	}

	public AccessorSaveData () {
		super(ID);
	}

	public AccessorSaveData (String name) {
		super(name);
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		accessorToParent.clear();
		parentToAccesors.clear();

		NBTTagCompound accessorMap = compound.getCompoundTag(Tags.ACCESSOR);
		for (String sdim : accessorMap.getKeySet()) {
			int dim = Integer.parseInt(sdim);
			NBTTagList pairs = accessorMap.getTagList(sdim, NBT.TAG_LIST);
			for (NBTBase base : pairs.tagList) {
				NBTTagList thisEntry = (NBTTagList) base;
				BlockPos accessor = NBTUtil.getPosFromTag(thisEntry.getCompoundTagAt(0));
				BlockPos parent = NBTUtil.getPosFromTag(thisEntry.getCompoundTagAt(1));
				setParent(dim, accessor, parent);
			}
		}

		NBTTagCompound parentMap = compound.getCompoundTag(Tags.PARENT);
		for (String sdim : parentMap.getKeySet()) {
			int dim = Integer.parseInt(sdim);
			NBTTagList pairs = parentMap.getTagList(sdim, NBT.TAG_LIST);
			for (NBTBase base : pairs.tagList) {
				NBTTagList thisEntry = (NBTTagList) base;
				NBTTagList parentTag = (NBTTagList) thisEntry.tagList.get(0);
				NBTTagList accessorsTag = (NBTTagList) thisEntry.tagList.get(1);

				BlockPos parent = NBTUtil.getPosFromTag(parentTag.getCompoundTagAt(0));
				List<BlockPos> accessors = new ArrayList<>();

				for (int i = 0; i < accessorsTag.tagCount(); i++) {
					accessors.add(NBTUtil.getPosFromTag(accessorsTag.getCompoundTagAt(i)));
				}

				setAccessors(dim, parent, accessors);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		NBTTagCompound dimensionMap = new NBTTagCompound();
		for (Entry<Long2LongOpenHashMap> entry : accessorToParent.int2ObjectEntrySet()) {
			int dim = entry.getIntKey();
			Long2LongOpenHashMap map = entry.getValue();
			NBTTagList thisDimension = new NBTTagList();
			for (Long2LongMap.Entry mapEntry : map.long2LongEntrySet()) {
				NBTTagCompound accessor = NBTUtil.createPosTag(BlockPos.fromLong(mapEntry.getLongKey()));
				NBTTagCompound parent = NBTUtil.createPosTag(BlockPos.fromLong(mapEntry.getLongValue()));
				NBTTagList thisEntry = new NBTTagList();
				thisEntry.appendTag(accessor);
				thisEntry.appendTag(parent);
				thisDimension.appendTag(thisEntry);
			}
			dimensionMap.setTag(String.valueOf(dim), thisDimension);
		}

		compound.setTag(Tags.ACCESSOR, dimensionMap);

		NBTTagCompound parentMap = new NBTTagCompound();
		for (Entry<Long2ObjectOpenHashMap<LongArrayList>> entry : parentToAccesors.int2ObjectEntrySet()) {
			int dim = entry.getIntKey();
			NBTTagList dimEntry = new NBTTagList();
			Long2ObjectOpenHashMap<LongArrayList> map = entry.getValue();
			for (Long2ObjectMap.Entry<LongArrayList> mapEntry : map.long2ObjectEntrySet()) {
				NBTTagList parent = new NBTTagList();
				parent.appendTag(NBTUtil.createPosTag(BlockPos.fromLong(mapEntry.getLongKey())));

				NBTTagList accessors = new NBTTagList();
				for (long acc : mapEntry.getValue()) {
					accessors.appendTag(NBTUtil.createPosTag(BlockPos.fromLong(acc)));
				}
				NBTTagList thisEntry = new NBTTagList();
				thisEntry.appendTag(parent);
				thisEntry.appendTag(accessors);
				dimEntry.appendTag(thisEntry);
			}
			parentMap.setTag(String.valueOf(dim), dimEntry);
		}

		compound.setTag(Tags.PARENT, parentMap);
		return compound;
	}

	public static class Tags {
		public static final String PARENT = "PARENT";
		public static final String ACCESSOR = "ACCESSOR";
	}
}

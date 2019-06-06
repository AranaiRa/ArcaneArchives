package com.aranaira.arcanearchives.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nullable;
import java.util.*;

public class HiveSaveData extends WorldSavedData {
	public static final String ID = "Archane-Archives-Hives";

	private Map<UUID, Hive> ownerToHive = new HashMap<>();
	private Map<UUID, UUID> memberToOwner = new HashMap<>();

	public HiveSaveData (String name) {
		super(name);
	}

	public HiveSaveData () {
		super(ID);
	}

	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("hive_data", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++) {
			Hive hive = Hive.fromNBT(list.getCompoundTagAt(i));
			ownerToHive.put(hive.getOwner(), hive);
			for (UUID member : hive.getMembers()) {
				memberToOwner.put(member, hive.getOwner());
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (Hive hive : ownerToHive.values()) {
			list.appendTag(hive.writeToNBT());
		}
		compound.setTag("hive_data", list);
		return compound;
	}

	public Hive getHiveByOwner (UUID owner) {
		if (ownerToHive.get(owner) == null) {
			Hive hive = new Hive(owner);
			ownerToHive.put(owner, hive);
		}

		return ownerToHive.get(owner);
	}

	public boolean addHive (Hive hive) {
		if (ownerToHive.containsKey(hive.getOwner())) return false;

		ownerToHive.put(hive.getOwner(), hive);
		return true;
	}

	public void addMember (Hive hive, UUID newMember) {
		if (hive.getOwner().equals(newMember)) return;

		UUID oldOwner = memberToOwner.get(newMember);
		if (oldOwner != null || !hive.getOwner().equals(oldOwner)) {
			memberToOwner.remove(newMember);
		}

		memberToOwner.put(newMember, hive.getOwner());
	}

	public void removeMember (Hive hive, UUID memberToRemove) {
		assert !hive.getOwner().equals(memberToRemove); // This should be handled with Change Owner
		memberToOwner.remove(memberToRemove);
		hive.removeMember(memberToRemove);
	}

	public void changeOwner (Hive hive) {
		UUID oldOwner = hive.getOwner();
		UUID newOwner = hive.getOldestMember();
		hive.removeMember(newOwner);
		hive.setOwner(newOwner);

		ownerToHive.remove(oldOwner);
		ownerToHive.put(newOwner, hive);
		memberToOwner.remove(newOwner);

		for (UUID member : hive.getMembers()) {
			assert !member.equals(newOwner);
			memberToOwner.remove(member);
			memberToOwner.put(member, newOwner);
		}
	}

	@Nullable
	public Hive getHiveByMember (UUID member) {
		// Check to see if they have an owner first
		Hive ownerHive = ownerToHive.get(member);
		if (ownerHive != null) {
			return ownerHive;
		}

		UUID owner = memberToOwner.get(member);
		if (owner != null) {
			return getHiveByOwner(owner);
		}

		return null;
	}

	public static class Hive {
		public UUID owner;
		public List<UUID> members;

		public Hive (UUID owner) {
			this.owner = owner;
			this.members = new ArrayList<>();
		}

		public UUID getOwner () {
			return owner;
		}

		public void setOwner (UUID owner) {
			this.owner = owner;
		}

		public List<UUID> getMembers () {
			return members;
		}

		public void setMembers (List<UUID> members) {
			this.members = members;
		}

		public void addMember (UUID member) {
			this.members.add(member);
		}

		public boolean removeMember (UUID member) {
			return this.members.remove(member);
		}

		@Nullable
		public UUID getOldestMember () {
			if (!members.isEmpty()) {
				return members.get(0);
			}

			return null;
		}

		public NBTTagCompound writeToNBT () {
			NBTTagCompound result = new NBTTagCompound();
			result.setUniqueId("owner", getOwner());
			NBTTagList members = new NBTTagList();
			for (UUID member : this.members) {
				NBTTagCompound m = new NBTTagCompound();
				m.setUniqueId("uuid", member);
				members.appendTag(m);
			}
			result.setTag("members", members);
			return result;
		}

		public void readFromNBT (NBTTagCompound tag) {
			this.owner = tag.getUniqueId("owner");
			this.members.clear();
			NBTTagList members = tag.getTagList("members", NBT.TAG_COMPOUND);
			for (int i = 0; i < members.tagCount(); i++) {
				this.members.add(members.getCompoundTagAt(i).getUniqueId("uuid"));
			}
		}

		public static Hive fromNBT (NBTTagCompound tag) {
			Hive hive = new Hive(null);
			hive.readFromNBT(tag);
			return hive;
		}
	}
}

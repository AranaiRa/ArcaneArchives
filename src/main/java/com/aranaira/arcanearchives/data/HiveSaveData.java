package com.aranaira.arcanearchives.data;

import com.sun.istack.internal.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

import java.rmi.server.UID;
import java.util.*;

public class HiveSaveData extends WorldSavedData {
	public static final String ID = "Archane-Archives-Network";

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

	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		return null;
	}

	public Hive getHiveByOwner (UUID owner) {
		if (ownerToHive.get(owner) == null) {
			Hive hive = new Hive(owner);
			ownerToHive.put(owner, hive);
		}

		return ownerToHive.get(owner);
	}

	public Hive getHiveByMember (UUID member) {
		// Check to see if they have an owner first
		if (ownerToHive.get(member) != null) {
			return ownerToHive.get(member);
		}

		UUID owner = memberToOwner.get(member);
		if (owner == null) {
			// Create a new hive with this member as the owner
			return getHiveByOwner(member);
		}

		return getHiveByOwner(owner);
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
	}
}

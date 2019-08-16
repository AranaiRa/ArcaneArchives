package com.aranaira.arcanearchives.data;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nullable;
import java.util.*;

public class HiveSaveData extends WorldSavedData {
	public static final String ID = "Archane-Archives-Hives";

	public Map<UUID, Hive> ownerToHive = new HashMap<>();

	public HiveSaveData (String name) {
		super(name);
	}

	public HiveSaveData () {
		super(ID);
	}

	public Set<UUID> getHiveOwners () {
		return Sets.newHashSet(ownerToHive.keySet());
	}

	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("hive_data", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++) {
			Hive hive = Hive.fromNBT(list.getCompoundTagAt(i));
			if (hive != null) {
				ownerToHive.put(hive.owner, hive);
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
			markDirty();
		}

		return ownerToHive.get(owner);
	}

	// Validates that the hive has the specific owner or member contained
	public boolean validateHive (Hive hive, @Nullable UUID owner, @Nullable UUID member) {
		if (hive.members.isEmpty()) {
			ownerToHive.remove(hive.owner);
			if (owner != null) {
				ownerToHive.remove(owner);
			}
			return false;
		} else if (owner != null && !hive.owner.equals(owner)) {
			ownerToHive.remove(owner);
			ownerToHive.put(hive.owner, hive);
			return member == null || hive.members.contains(member);
		}

		return true;
	}

	@Nullable
	public Hive getHiveByMember (UUID member) {
		// Check to see if they have an owner first
		Hive ownerHive = ownerToHive.get(member);
		if (ownerHive != null) {
			if (validateHive(ownerHive, member, null)) {
				return ownerHive;
			}
		}

		// Else check the hives
		for (Hive hive : ownerToHive.values()) {
			if (hive.members.contains(member)) {
				if (validateHive(hive, null, member)) {
					return hive;
				}
			}
		}

		return null;
	}

	/// ABOVE FUNCTIONS ARE SAFE

	public boolean addMember (Hive hive, UUID newMember) {
		if (hive.owner.equals(newMember)) {
			return false;
		}

		if (hive.members.contains(newMember)) {
			return false;
		}

		if (getHiveByMember(newMember) != null) {
			return false;
		}

		hive.members.add(newMember);

		markDirty();
		return true;
	}

	public boolean removeMember (Hive hive, UUID memberToRemove) {
		boolean result = false;

		if (hive.owner.equals(memberToRemove)) {
			result = true;
			UUID oldest = hive.getOldestMember();
			hive.members.remove(oldest);
			hive.owner = oldest;
			ownerToHive.remove(memberToRemove);
			ownerToHive.put(oldest, hive);
		} else {
			if (hive.members.remove(memberToRemove)) {
				result = true;
			}
		}
		handlePotentialDisband(hive);
		markDirty();
		return result;
	}

	public void handlePotentialDisband (Hive hive) {
		boolean doDisband;
		if (hive.members.isEmpty()) {
			doDisband = true;
		} else {
			doDisband = true;
			for (UUID member : hive.members) {
				if (!member.equals(hive.owner)) {
					doDisband = false;
				}
			}
		}
		if (doDisband) {
			ownerToHive.remove(hive.owner);
			hive.disbanded = true;
		}
	}

	public void alertMembers (World world, Hive hive, UUID newMember, boolean joined) {
		List<UUID> members = new ArrayList<>(hive.members);

		PlayerProfileCache cache = world.getMinecraftServer().getPlayerProfileCache();
		GameProfile profile = cache.getProfileByUUID(newMember);
		String name = profile == null ? "Unknown" : profile.getName();

		if (members.isEmpty()) {
			EntityPlayer player = world.getPlayerEntityByUUID(hive.owner);
			if (player != null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.network.hive.disbanded", name).setStyle(new Style().setColor(TextFormatting.GOLD)));
			}
			return;
		}

		members.add(hive.owner);

		for (UUID uuid : members) {
			if (uuid.equals(newMember)) {
				continue;
			}
			EntityPlayer player = world.getPlayerEntityByUUID(uuid);
			if (player == null) {
				continue; // Not online
			}
			if (joined) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.network.hive.joined_your_network", name).setStyle(new Style().setColor(TextFormatting.GOLD)));
			} else {
				player.sendMessage(new TextComponentTranslation("arcanearchives.network.hive.left_your_network", name).setStyle(new Style().setColor(TextFormatting.GOLD)));
			}
		}
	}

	public static class Hive {
		public UUID owner;
		public LinkedHashSet<UUID> members = new LinkedHashSet<>();
		public boolean disbanded = false;

		public Hive (UUID owner) {
			this.owner = owner;
		}

		public void setOwner (UUID owner) {
			this.owner = owner;
		}

		@Nullable
		public UUID getOldestMember () {
			if (members.isEmpty()) {
				return null;
			}
			return members.iterator().next();
		}

		public NBTTagCompound writeToNBT () {
			NBTTagCompound result = new NBTTagCompound();
			result.setUniqueId("owner", owner);
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
				UUID incoming = members.getCompoundTagAt(i).getUniqueId("uuid");
				if (this.owner.equals(incoming)) {
					continue;
				}
				this.members.add(incoming);
			}
		}

		@Nullable
		public static Hive fromNBT (NBTTagCompound tag) {
			Hive hive = new Hive(null);
			hive.readFromNBT(tag);
			if (hive.members.isEmpty()) {
				return null;
			}
			return hive;
		}
	}
}

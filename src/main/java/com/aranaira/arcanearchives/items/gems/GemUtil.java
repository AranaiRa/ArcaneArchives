package com.aranaira.arcanearchives.items.gems;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem.GemCut;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem.GemColor;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem.GemUpgrades;
import com.aranaira.arcanearchives.items.BaubleGemSocket;
import com.aranaira.arcanearchives.util.NBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;

/**
 * Helper class that handles all of the NBT lookups
 */
public class GemUtil {

    /**
     * Sets the upgrades on a gem by applying a bitmask.
     * Matter=1, Power=2, Space=4, Time=8
     *
     * @param stack    The ItemStack to upgrade
     * @param upgrades The new upgrade bitmask to apply
     */
    public static void setUpgrades (ItemStack stack, byte upgrades) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        nbt.setByte("upgrades", upgrades);
    }

    /**
     * A human readable way to set upgrades on a gem.
     *
     * @param stack         The ItemStack to upgrade
     * @param matterUpgrade Whether to apply a Matter upgrade
     * @param powerUpgrade  Whether to apply a Power upgrade
     * @param spaceUpgrade  Whether to apply a Space upgrade
     * @param timeUpgrade   Whether to apply a Time upgrade
     */
    public static void setUpgrades (ItemStack stack, boolean matterUpgrade, boolean powerUpgrade, boolean spaceUpgrade, boolean timeUpgrade) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        byte upgrades = 0;
        if (matterUpgrade) {
            upgrades = (byte) (upgrades | ArcaneGemItem.UPGRADE_MATTER);
        }
        if (powerUpgrade) {
            upgrades = (byte) (upgrades | ArcaneGemItem.UPGRADE_POWER);
        }
        if (spaceUpgrade) {
            upgrades = (byte) (upgrades | ArcaneGemItem.UPGRADE_SPACE);
        }
        if (timeUpgrade) {
            upgrades = (byte) (upgrades | ArcaneGemItem.UPGRADE_TIME);
        }
        nbt.setByte("upgrades", upgrades);
    }

    /**
     * Get a byte representing the upgrades on this gem as a bit mask
     *
     * @param stack The ItemStack to check
     * @return
     */
    public static byte getUpgrades (ItemStack stack) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        if (nbt.hasKey("upgrades")) {
            return nbt.getByte("upgrades");
        }
        return 0;
    }

    /**
     * Check whether a particular gem has a specific upgrade type
     *
     * @param stack The ItemStack to check
     * @param query The upgrade type to check for
     * @return
     */
    public static boolean hasUpgrade (ItemStack stack, ArcaneGemItem.GemUpgrades query) {
        byte upgrades = getUpgrades(stack);
        switch (query) {
            case MATTER:
                return (upgrades & ArcaneGemItem.UPGRADE_MATTER) == ArcaneGemItem.UPGRADE_MATTER;
            case POWER:
                return (upgrades & ArcaneGemItem.UPGRADE_POWER) == ArcaneGemItem.UPGRADE_POWER;
            case SPACE:
                return (upgrades & ArcaneGemItem.UPGRADE_SPACE) == ArcaneGemItem.UPGRADE_SPACE;
            case TIME:
                return (upgrades & ArcaneGemItem.UPGRADE_TIME) == ArcaneGemItem.UPGRADE_TIME;
            default:
                return false;
        }
    }

    /**
     * Get the maximum charge for this gem type, taking into account Power upgrades
     *
     * @param stack The ItemStack to check
     * @return The gem's maximum charge
     */
    public static int getMaxCharge (ItemStack stack) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        boolean hasPowerUpgrade = hasUpgrade(stack, ArcaneGemItem.GemUpgrades.POWER);
        ArcaneGemItem item = (ArcaneGemItem) stack.getItem();
        if (hasPowerUpgrade) {
            return item.getMaxChargeUpgraded();
        } else {
            return item.getMaxChargeNormal();
        }
    }

    /**
     * Gets the current charge amount of a gem.
     *
     * @param stack The ItemStack to check.
     * @return The amount of charge.
     */
    public static int getCharge (ItemStack stack) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        if (nbt.hasKey("charge")) {
            return nbt.getInteger("charge");
        } else {
            int maximum = getMaxCharge(stack);
            nbt.setInteger("charge", maximum);
            return maximum;
        }
    }

    /**
     * Gets the current charge amount of a gem as a value between 0..1
     *
     * @param stack The ItemStack to check
     * @return The amount of charge.
     */
    public static float getChargePercent (ItemStack stack) {
        int current = getCharge(stack);
        int maximum = getMaxCharge(stack);

        return (float) current / (float) maximum;
    }

    /**
     * Restore the charge on the gem by a set amount.
     *
     * @param amount How much to increase charge by. -1 to fill the gem outright.
     * @return true if the gem is full
     */
    public static boolean restoreCharge (ItemStack stack, int amount) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        int maximum = getMaxCharge(stack);
        int currentCharge;
        if (nbt.hasKey("charge")) {
            currentCharge = nbt.getInteger("charge");
        } else {
            currentCharge = maximum;
            nbt.setInteger("charge", currentCharge);
        }

        if (amount == -1) {
            currentCharge = maximum;
        } else {
            currentCharge += amount;
            if (currentCharge > maximum) {
                currentCharge = maximum;
            }
        }
        nbt.setInteger("charge", currentCharge);

        return currentCharge >= maximum;
    }

    /**
     * Reduce the charge on the gem by a set amount.
     *
     * @param amount How much to reduce charge by. -1 to empty the gem outright.
     * @return true if charge remains, false if gem is now empty
     */
    public static boolean consumeCharge (ItemStack stack, int amount) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        int currentCharge;
        if (nbt.hasKey("charge")) {
            currentCharge = nbt.getInteger("charge");
        } else {
            currentCharge = getMaxCharge(stack);
            nbt.setInteger("charge", currentCharge);
        }
        if (amount < -1) {
            return currentCharge < 0;
        } else if (amount == -1) {
            currentCharge = 0;
        } else {
            currentCharge -= amount;
            if (currentCharge < 0) {
                currentCharge = 0;
            }
        }
        nbt.setInteger("charge", currentCharge);
        return currentCharge > 0;
    }

    /**
     * Checks whether this gem has unlimited use.
     *
     * @param stack The ItemStack to check
     * @return
     */
    public static boolean hasUnlimitedCharge (ItemStack stack) {
        if (getMaxCharge(stack) == 0) {
            return true;
        }

        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        if (nbt.hasKey("infinite")) {
            return nbt.getBoolean("infinity");
        } else {
            nbt.setBoolean("infinite", false);
            return false;
        }
    }

    /**
     * Convenience method to check whether a gem is out of charge
     *
     * @param stack The gem to check
     * @return
     */
    public static boolean isChargeEmpty (ItemStack stack) {
        NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
        if (!hasUnlimitedCharge(stack)) {
            return getCharge(stack) == 0;
        } else {
            return false;
        }
    }

    /**
     * Checks whether the gem is toggled on
     *
     * @param stack The gem to check
     * @return The gem's toggle status. If the gem has no toggle state, always returns false.
     */
    public static boolean isToggledOn (ItemStack stack) {
        ArcaneGemItem agi = (ArcaneGemItem) stack.getItem();
        if (agi.hasToggleMode()) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            if (!nbt.hasKey("toggle")) {
                nbt.setBoolean("toggle", false);
            }
            return nbt.getBoolean("toggle");
        }
        return false;
    }

    /**
     * Switches whether the gem is toggled on or off.
     *
     * @param stack The gem to swap the toggle mode of.
     */
    public static void swapToggle (ItemStack stack) {
        ArcaneGemItem gem = (ArcaneGemItem) stack.getItem();
        if (gem.hasToggleMode()) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            if (!nbt.hasKey("toggle")) {
                nbt.setBoolean("toggle", true);
            } else {
                nbt.setBoolean("toggle", !nbt.getBoolean("toggle"));
            }
            stack.setTagCompound(nbt);
        }
    }

    /**
     * Sets the gem's toggle state. Has no effect if the gem has no toggle functionality.
     *
     * @param stack The gem to modify
     * @param state The desired toggle state
     */
    public static void setToggle (ItemStack stack, boolean state) {
        ArcaneGemItem gem = (ArcaneGemItem) stack.getItem();
        if (gem.hasToggleMode()) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            nbt.setBoolean("toggle", state);
            stack.setTagCompound(nbt);
        }
    }

    /**
     * Get gems that are capable of operating passively. Checks for held, gems slotted in a Fabrial's active or passive slots, and a gem in the Gem Socket.
     *
     * @param player
     * @return List of appropriate gems
     */
    public static ArrayList<ItemStack> getAvailableGems (EntityPlayer player) {
        ArrayList<ItemStack> gems = new ArrayList<>();

        //Held gems
        if (player.getHeldItemMainhand().getItem() instanceof ArcaneGemItem) {
            gems.add(player.getHeldItemMainhand());
        }
        if (player.getHeldItemOffhand().getItem() instanceof ArcaneGemItem) {
            gems.add(player.getHeldItemOffhand());
        }

        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        //TODO: check for fabrial

        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for (int i : BaubleType.BODY.getValidSlots()) {
            if (handler.getStackInSlot(i).getItem() instanceof BaubleGemSocket) {
                if (handler.getStackInSlot(i).getTagCompound().hasKey("gem")) {
                    ItemStack containedStack = GemSocketHandler.getHandler(handler.getStackInSlot(i)).getInventory().getStackInSlot(0);
                    gems.add(containedStack);
                }
            }
        }

        return gems;
    }
}

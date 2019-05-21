/***
 * This code was written by mezz for Just Enough Items, licensed
 * under the terms of the MIT License. The code was originally retrieved
 * on 21-5-19 from: https://raw.githubusercontent.com/mezz/JustEnoughItems/1.12/src/main/java/mezz/jei/gui/ingredients/CycleTimer.java
 *
 * See LICENSE.md in the root directory for a copy of the MIT License or visit:
 * https://opensource.org/licenses/MIT
 */
package com.aranaira.arcanearchives.util;

import javax.annotation.Nullable;
import java.util.List;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CycleTimer {
	/* the amount of time in ms to display one thing before cycling to the next one */
	private static final int cycleTime = 1000;
	private long startTime;
	private long drawTime;
	private long pausedDuration = 0;

	public CycleTimer(int offset) {
		if (offset == -1) {
			offset = (int) (Math.random() * 10000);
		}
		long time = System.currentTimeMillis();
		this.startTime = time - (offset * cycleTime);
		this.drawTime = time;
	}

	@Nullable
	public <T> T getCycledItem(List<T> list) {
		if (list.isEmpty()) {
			return null;
		}
		Long index = ((drawTime - startTime) / cycleTime) % list.size();
		return list.get(index.intValue());
	}

	public void onDraw() {
		if (!GuiScreen.isShiftKeyDown()) {
			if (pausedDuration > 0) {
				startTime += pausedDuration;
				pausedDuration = 0;
			}
			drawTime = System.currentTimeMillis();
		} else {
			pausedDuration = System.currentTimeMillis() - drawTime;
		}
	}
}

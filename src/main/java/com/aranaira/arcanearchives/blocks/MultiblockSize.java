package com.aranaira.arcanearchives.blocks;

@SuppressWarnings("WeakerAccess")
public class MultiblockSize {
	public int width = 1;
	public int height = 1;
	public int length = 1;

	public MultiblockSize (int width, int height, int length) {
		this.width = width;
		this.height = height;
		this.length = length;
	}

	public MultiblockSize (int[] size) {
		if (size.length == 3) {
			this.width = size[0];
			this.height = size[1];
			this.length = size[2];
		}
	}

	public int[] getArray () {
		return new int[]{this.width, this.height, this.length};
	}

	public boolean hasAccessors () {
		return width + height + length > 3;
	}
}

package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.util.zen.ZenDocExporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExportDocumentation {

	public static void main (String[] args) {

		String targetPath = "docs/zs/";
		Class[] classes = {/*GCTTweaker.class*/};

		ZenDocExporter export = new ZenDocExporter();
		Path path = Paths.get(targetPath);

		try {
			Files.createDirectories(path);
			export.export(path, classes);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

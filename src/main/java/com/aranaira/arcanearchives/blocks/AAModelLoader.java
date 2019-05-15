package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.lireherz.guidebook.guidebook.client.BookBakedModel;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.resource.IResourceType;

import java.util.function.Predicate;

public class AAModelLoader implements ICustomModelLoader {
	private static AAModelLoader INSTANCE;

	private BookBakedModel.ModelLoader bookBakedModel = new BookBakedModel.ModelLoader();
	private OBJLoader objLoader = OBJLoader.INSTANCE;

	public static AAModelLoader getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new AAModelLoader();
		}
		return INSTANCE;
	}

	@Override
	public void onResourceManagerReload (IResourceManager resourceManager) {
		bookBakedModel.onResourceManagerReload(resourceManager);
		// objLoader is already registered at the top level so it'll already get this notification
	}

	@Override
	public void onResourceManagerReload (IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		bookBakedModel.onResourceManagerReload(resourceManager, resourcePredicate);
	}

	@Override
	public boolean accepts (ResourceLocation modelLocation) {
		return (modelLocation.getNamespace().equals(ArcaneArchives.MODID.toLowerCase()) && modelLocation.getPath().endsWith(".obj")) || bookBakedModel.accepts(modelLocation);
	}

	@Override
	public IModel loadModel (ResourceLocation modelLocation) throws Exception {
		if (bookBakedModel.accepts(modelLocation)) {
			return bookBakedModel.loadModel(modelLocation);
		} else {
			return objLoader.loadModel(modelLocation);
		}

	}
}

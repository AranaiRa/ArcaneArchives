package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.events.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.*;

import java.io.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class ShaderHelper {

    private static final int VERT = OpenGlHelper.GL_VERTEX_SHADER;
    private static final int FRAG = OpenGlHelper.GL_FRAGMENT_SHADER;

    public static final ResourceLocation BLOOM_SHADER_LOC = new ResourceLocation(ArcaneArchives.MODID, "shaders/bloom.frag");

    public static int bloomShader;

    public static void initShaders() {
        if (Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
            ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(manager -> {
                deleteShader(bloomShader); bloomShader= 0;

                loadShaders();
            });
        }
    }

    private static void loadShaders() {
        bloomShader = loadFragmentShaderProgram(BLOOM_SHADER_LOC);
    }

    private static int loadShaderProgram(ResourceLocation vertexShaderLoc, ResourceLocation fragmentShaderLoc) {
        int vertexShader = createShader(vertexShaderLoc, VERT);
        int fragmentShader = createShader(fragmentShaderLoc, FRAG);
        int program = OpenGlHelper.glCreateProgram();
        OpenGlHelper.glAttachShader(program, vertexShader);
        OpenGlHelper.glAttachShader(program, fragmentShader);
        OpenGlHelper.glLinkProgram(program);

        return program;
    }

    private static int loadVertexShaderProgram(ResourceLocation vertexShaderLoc) {
        int vertexShader = createShader(vertexShaderLoc, VERT);
        int program = OpenGlHelper.glCreateProgram();
        OpenGlHelper.glAttachShader(program, vertexShader);
        OpenGlHelper.glLinkProgram(program);

        return program;
    }

    private static int loadFragmentShaderProgram(ResourceLocation fragmentShaderLoc) {
        int fragmentShader = createShader(fragmentShaderLoc, FRAG);
        int program = OpenGlHelper.glCreateProgram();
        OpenGlHelper.glAttachShader(program, fragmentShader);
        OpenGlHelper.glLinkProgram(program);

        return program;
    }

    private static void deleteShader(int shaderID) {
        if(shaderID != 0) {
            OpenGlHelper.glDeleteShader(shaderID);
        }
    }

    private static int createShader(ResourceLocation shaderFile, int shaderType) {
        int shader = OpenGlHelper.glCreateShader(shaderType);

        if (shader == 0) {
            return 0;
        }

        ARBShaderObjects.glShaderSourceARB(shader, getShaderAsString(shaderFile));
        OpenGlHelper.glCompileShader(shader);

        if(GL20.glGetShaderi(shader, OpenGlHelper.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
        }

        return shader;
    }

    public static void useShader(int shader, Consumer<Integer> callback) {
        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(shader != 0 && callback != null) {
            int ticks = ARBShaderObjects.glGetUniformLocationARB(shader, "ticks");
            ARBShaderObjects.glUniform1iARB(ticks, ClientTickHandler.ticksInGame);

            callback.accept(shader);
        }
    }

    public static void useShader(int shader) {
        useShader(shader, null);
    }

    public static void releaseShader() {
        useShader(0);
    }

    //Can be used to get the log info oif a shader obj, for any errors/bugs that need tracking down
    public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    private static String getShaderAsString(ResourceLocation shaderFile) {
        InputStream shaderStream = getShaderASStream(shaderFile);
        String s = "";

        if(shaderStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(shaderStream, "UTF-8"))) {
                s = reader.lines().collect(Collectors.joining("\n"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ArcaneArchives.logger.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
            } catch (IOException e) {
                e.printStackTrace();
                ArcaneArchives.logger.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
            }
        }

        return s;
    }

    private static InputStream getShaderASStream(ResourceLocation shaderFile) {
        Minecraft minecraft = Minecraft.getMinecraft();
        IResource resource = null;

        try {
            resource = minecraft.getResourceManager().getResource(shaderFile);
        } catch (IOException e) {
            ArcaneArchives.logger.fatal("Unable to locate Shader! Source: " + shaderFile.toString(), e);
        }

        return resource.getInputStream();
    }
}

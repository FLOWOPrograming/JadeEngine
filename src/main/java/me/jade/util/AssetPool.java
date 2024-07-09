package me.jade.util;

import me.jade.ecs.components.Spritesheet;
import me.jade.renderer.Shader;
import me.jade.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    //-----------
    //--SHADERS--
    //-----------

    private static Map<String, Shader> shaders = new HashMap<>();

    public static Shader getShader(String filepath) {
        File shaderFile = new File(filepath);
        String absoluteFilePath = shaderFile.getAbsolutePath();

        if (shaders.containsKey(absoluteFilePath)) return shaders.get(absoluteFilePath);

        System.out.println("Generating shader at '" + absoluteFilePath + "'");

        Shader shader = new Shader(absoluteFilePath);
        shader.compile();

        shaders.put(absoluteFilePath, shader);

        return shaders.get(absoluteFilePath);
    }

    //-----------
    //--TEXTURE--
    //-----------

    private static Map<String, Texture> textures = new HashMap<>();

    public static Texture getTexture(String filepath) {
        File shaderFile = new File(filepath);
        String absoluteFilePath = shaderFile.getAbsolutePath();

        if (textures.containsKey(absoluteFilePath)) return textures.get(absoluteFilePath);

        System.out.println("Generating texture at '" + absoluteFilePath + "'");

        Texture texture = new Texture(absoluteFilePath);

        textures.put(absoluteFilePath, texture);

        return textures.get(absoluteFilePath);
    }

    //---------------
    //--SPRITESHEET--
    //---------------

    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Spritesheet getSpriteSheet(String resourceName) {
        File shaderFile = new File(resourceName);
        String absoluteFilePath = shaderFile.getAbsolutePath();

        if (spritesheets.containsKey(absoluteFilePath)) return spritesheets.get(absoluteFilePath);

        throw new IllegalArgumentException("You did not have a spritesheet with this path!");
    }

    public static void addSpriteSheet(String resourceName, Spritesheet spritesheet) {
        File shaderFile = new File(resourceName);
        String absoluteFilePath = shaderFile.getAbsolutePath();

        if (spritesheets.containsKey(resourceName)) return;

        spritesheets.put(absoluteFilePath, spritesheet);
    }
}

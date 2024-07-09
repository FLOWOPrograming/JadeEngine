package me.jade.util;

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
}

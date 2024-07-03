package me.jade.renderer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    private int vertexID, fragmentID, shaderProgram;

    public Shader(String filePath) {
        this.filePath = filePath;

        getShaderSources();

        compile();
    }

    private void getShaderSources() {
        StringBuilder vertexSourceBuilder = new StringBuilder();
        StringBuilder fragmentSourceBuilder = new StringBuilder();
        StringBuilder currentBuilder = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("#type vertex")) {
                    currentBuilder = vertexSourceBuilder;
                } else if (line.trim().equals("#type frag")) {
                    currentBuilder = fragmentSourceBuilder;
                } else if (currentBuilder != null) {
                    currentBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        vertexSource = vertexSourceBuilder.toString();
        fragmentSource = fragmentSourceBuilder.toString();

        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }

    public void compile() {
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);

            System.err.println("Vertex shader compilation failed at " + filePath);
            System.err.println(glGetShaderInfoLog(vertexID, len));
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);

        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);

            System.err.println("Fragment shader compilation failed at " + filePath);
            System.err.println(glGetShaderInfoLog(fragmentID, len));
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);

        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);

            System.err.println("Program linking failed:");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
        }
    }

    public void use() {
        glUseProgram(shaderProgram);
    }

    public void detach() {
        glUseProgram(0);
    }
}

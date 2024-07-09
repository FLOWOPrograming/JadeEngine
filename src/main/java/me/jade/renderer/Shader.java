package me.jade.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    private boolean beingUsed;

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
        if (beingUsed) return;

        glUseProgram(shaderProgram);
        beingUsed = true;
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    //==============
    //===UNIFORMS===
    //==============

    // Matrices

    public void uploadMat4f(String varName, Matrix4f mat4) {
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4*4);
        mat4.get(matBuffer);

        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(3*3);
        mat3.get(matBuffer);

        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadMat2f(String varName, Matrix2f mat2) {
        use();

        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(2*2);
        mat2.get(matBuffer);

        glUniformMatrix2fv(varLocation, false, matBuffer);
    }

    //Vectors

    public void uploadVec4f(String varName, Vector4f vec) {
        use();
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        use();
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        use();
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform2f(varLocation, vec.x, vec.y);
    }

    // Primitives

    public void uploadFloat(String varName, Float f) {
        use();
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform1f(varLocation, f);
    }

    public void uploadInteger(String varName, Integer i) {
        use();
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform1i(varLocation, i);
    }

    // Image

    public void uploadTexture(String varName, int slot) {
        use();
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array) {
        use();
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        glUniform1iv(varLocation, array);
    }
}
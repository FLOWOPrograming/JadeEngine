package me.jade;

import me.jade.ecs.GameObject;
import me.jade.ecs.components.SpriteRenderer;
import me.jade.renderer.Shader;
import me.jade.renderer.Texture;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class LevelEditorScene extends Scene {

    private Shader shader;
    private Texture texture;

    private GameObject gameObject;

    private int vaoID, vboID, eboID;

    private float[] vertexArray = {
        //position             //color                     // UV
         100f, 0f,   0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     1, 1, // BR
         0f,   100f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     0, 0, // TL
         100f, 100f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f,     1, 0, // TR
         0f,   0f,   0.0f,     1.0f, 1.0f, 0.0f, 1.0f,     0, 1  // BL
    };


    // must be ccw
    private int[] elementArray = {
            0, 1, 3,
            2, 1, 0
    };

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.gameObject = new GameObject("Object1");
        this.gameObject.addComponent(new SpriteRenderer());
        this.addGameObject(this.gameObject);

        this.camera = new Camera(new Vector2f(0.0f, 0.0f));

        shader = new Shader("C:\\Users\\marij\\IdeaProjects\\JadeEngine\\src\\main\\java\\me\\jade\\util\\shaders\\default.glsl");
        shader.compile();

        this.texture = new Texture("C:\\Users\\marij\\IdeaProjects\\JadeEngine\\src\\main\\java\\me\\jade\\util\\images\\BD.PNG");

        //generate vao, vbo, ebo

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attrib pointers

        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        // Bind shader program
        shader.use();

        shader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());
        shader.uploadFloat("uTime", Time.getTimeElapsed());

        //Bind vao
        glBindVertexArray(vaoID);

        // Enable vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        //draw
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);

        shader.detach();
    }
}

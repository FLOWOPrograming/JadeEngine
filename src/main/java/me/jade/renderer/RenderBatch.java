package me.jade.renderer;

import me.jade.Window;
import me.jade.ecs.components.SpriteRenderer;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    // Define the layout of the vertex
    // Pos                     Col                           UV                tex ID
    // float, float,           float, float, float, float,   float, float,     float

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int UV_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0; // In bytes
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES; // In bytes
    private final int UV_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES; // In bytes
    private final int TEX_ID_OFFSET = UV_OFFSET + UV_SIZE * Float.BYTES; // In bytes

    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + UV_SIZE + TEX_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES; // In bytes

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private int maxTextureCount;
    private Shader shader;
    private List<Texture> textures;
    private int[] texSlots;

    public RenderBatch(int maxBatchSize, int maxTextureCount, Shader shader) {
        this.shader = shader;
        this.maxBatchSize = maxBatchSize;
        this.maxTextureCount = maxTextureCount;

        texSlots = new int[maxTextureCount];
        for (int i = 0; i < maxTextureCount; i++) {
            texSlots[i] = i;
        }

        this.sprites = new SpriteRenderer[maxBatchSize];

        // 4 vertex quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void start() {
        // Generate and bind vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for the vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the element attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, UV_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, UV_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRenderer sprite) {
        // Index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        if (sprite.getTexture() != null) {
            if (!textures.contains(sprite.getTexture())) {
                if (textures.size() >= this.maxTextureCount) {
                    throw new RuntimeException("Tried to add a sprite with a new texture and tried to surpass the texture limit!");
                }

                System.out.println("Adding new texture with path '" + sprite.getTexture().filepath + "' to ID:" + textures.size());
                textures.add(sprite.getTexture());
            }
        }

        //Add properties to local vertices array
        loadVertexProperties(index);

        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    public void render() {
        //TODO
        // For now we will rebuffer all data each frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjection", Window.getCurrentScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().camera().getViewMatrix());

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        shader.uploadIntArray("uTexture", texSlots);

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        for (int i = 0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }

        shader.detach();
    }

    private int[] generateIndices() {
        // 6 indices per quad
        int[] elements = new int[6 * maxBatchSize];

        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int i) {
        int startIndex = i * 6;
        int offset = i * 4;

        elements[startIndex + 0] = offset + 3;
        elements[startIndex + 1] = offset + 2;
        elements[startIndex + 2] = offset + 0;
        elements[startIndex + 3] = offset + 0;
        elements[startIndex + 4] = offset + 1;
        elements[startIndex + 5] = offset + 3;
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        // Find the offset
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        int texID = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    texID = i + 1;
                    break;
                }
            }
        }

        //       2       3
        //
        //
        //       0       1

        float xAdd = 0.0f;
        float yAdd = 0.0f;

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                xAdd = 0.0f;
                yAdd = 0.0f;
            } else if (i == 1) {
                xAdd = 1.0f;
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
                yAdd = 1.0f;
            } else {
                xAdd = 1.0f;
                yAdd = 1.0f;
            }

            // pos
            vertices[offset + 0] = sprite.parent.transform.position.x + (xAdd * sprite.parent.transform.scale.x);
            vertices[offset + 1] = sprite.parent.transform.position.y + (yAdd * sprite.parent.transform.scale.y);

            // color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // UV
            vertices[offset + 6] = sprite.getTexCoords()[i].x;
            vertices[offset + 7] = sprite.getTexCoords()[i].y;

            // Tex ID
            vertices[offset + 8] = texID;

            offset += VERTEX_SIZE;
        }
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    public boolean hasTextureRoom() {
        return this.textures.size() < this.maxTextureCount;
    }

    public boolean hasTexture(Texture tex) {
        return textures.contains(tex);
    }
}

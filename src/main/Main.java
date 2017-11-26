package main;

import processing.core.PApplet;

import java.util.List;


public class Main extends PApplet {

    public static final int SCREEN_X = 1024;
    public static final int SCREEN_Y = 768;

    // Unser Objekt, eine Liste von Dreiecken
    //List<Face> cube = ObjReader.readFile("cube.obj");
    List<Face> teapot = ObjReader.readFile("teapot.obj");
    //List<Face> cooper = ObjReader.readFile("cooper.obj");

    public static void main(String[] args) {
        PApplet.main("main.Main");
    }

    // Die aktuelle Welttransformation
    Matrix current = Matrix.unit();

    // Die Projektion (zentral) und anschliessend noch Bild in die Mitte verschieben
    Matrix projectToScreen = Matrix
            .translate(SCREEN_X/2, SCREEN_Y/2,0)
            .mult(Matrix.project(1000));


    // zbuffer - für korrekte Darstellung von Pixelverdeckung
    public static final float[][] zbuffer = new float[SCREEN_X][SCREEN_Y];

    // Hilfmethode - zBuffer löschen - wird vor jedem Frame aufgerufen
    private void clearZBuffer() {
        for (int x =0; x< SCREEN_X; x++) {
            for (int y = 0; y < SCREEN_Y; y++) {
                zbuffer[x][y] = Float.MAX_VALUE;
            }
        }

    }

    @Override
    public void settings() {
        size(SCREEN_X,SCREEN_Y, "processing.opengl.PGraphics2D");
    }


    @Override
    public void draw() {
        // Ein Frame zeichnen - erst alles löschen
        background(255);

        // Welttransformation auf Einheitsmatrix
        mresetMatrix();

        // Z-Buffer löschen
        clearZBuffer();

        /*
        // Variante 1 - Würfel
        // Welttransformation für Würfel
        mscale(300f);
        mrotateZ(mouseX/200f);
        mrotateX(mouseY/200f);
        mtranslate(0,0,1000);
        drawModel(cube);
        */

        // Variante 2 - Teekanne
        // Welttransformation für Utah-Teapot
        mscale(40f);
        mtranslate(0,0,-200);
        mrotateZ(mouseX/200f);
        mrotateX(mouseY/200f);
        mtranslate(0,0,1000);
        drawModel(teapot);

        /*
        // Variante 3 - Minicooper
        // Welttransformation für den Cooper
        mscale(8f);
        mrotateZ(mouseX/200f);
        mrotateX(mouseY/200f);
        mtranslate(0,0,1000);
        drawModel(cooper);
        */
    }

    // 3 Varianten für das Zeichnen eines 3D-Objekts
    public void drawModel(List<Face> faces) {
        /*
        // Variante 1 - einfach alle Dreiecke als Linienzüge zeichnen
        for (Face face : faces) {
            drawTriangleWithLines(face);
        }
        */


        /*
        // Variante 2 - Dreiecke als Linienzüge, mit back face culling
        for (Face face : faces) {
            Vec3 normal = current.multFree(face.getNormal()).unit();
            Vec3 view = current.mult(face.points.get(0)).sub(Vec3.of(0,0,-1000));
            if (normal.skalarProd(view) < 0) {
                drawTriangleWithLines(face);
            }
        }
        */

        // Variante 3 - Dreicke rastern, mit back face culling
        for (Face face : faces) {
            Vec3 normal = current.multFree(face.getNormal()).unit();
            Vec3 view = current.mult(face.points.get(0)).sub(Vec3.of(0,0,-1000));

            if (normal.skalarProd(view) < 0) {
               rasterTriangle(face);
            }
        }
    }

    // Hilfsfunktion, wird für Dreicksberechnungen genutzt
    float edgeFunction(Vec3 a, Vec3 b, Vec3 c) { return (c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x); }

    public void drawTriangleWithLines(Face face) {
        Vec3 v0 = face.points.get(0);
        Vec3 v1 = face.points.get(1);
        Vec3 v2 = face.points.get(2);

        line(v0, v1);
        line(v1, v2);
        line(v2, v0);
    }

    public void rasterTriangle(Face face) {

        Vec3 v0 = face.points.get(0);
        Vec3 v1 = face.points.get(1);
        Vec3 v2 = face.points.get(2);

        v0 = current.mult(v0);
        v1 = current.mult(v1);
        v2 = current.mult(v2);

        float z0 = v0.z;
        float z1 = v1.z;
        float z2 = v2.z;

        // Die Normalenvektoren müssen anders behandelt werden - keine Translation!
        Vec3 n0 = current.multFree(face.normals.get(0));
        Vec3 n1 = current.multFree(face.normals.get(1));
        Vec3 n2 = current.multFree(face.normals.get(2));

        v0 =  projectToScreen.mult(v0);
        v1 =  projectToScreen.mult(v1);
        v2 =  projectToScreen.mult(v2);

        // Bounding box des Dreiecks
        int minx = (int)Math.min(v0.x, Math.min(v1.x, v2.x));
        int miny = (int)Math.min(v0.y, Math.min(v1.y, v2.y));
        int maxx = (int)Math.max(v0.x, Math.max(v1.x, v2.x));
        int maxy = (int)Math.max(v0.y, Math.max(v1.y, v2.y));

        float area = edgeFunction(v0, v1, v2);

        for (int j = miny; j <= maxy; ++j) {
            for (int i = minx; i <= maxx; ++i) {
                Vec3 p = Vec3.of(i + 0.5f, j + 0.5f, 0f);

                // Baryzentrische Koordinaten
                float w0 = edgeFunction(v1, v2, p);
                float w1 = edgeFunction(v2, v0, p);
                float w2 = edgeFunction(v0, v1, p);

                // ISt der Pixel im Dreieck?
                if (w0 >= 0 && w1 >= 0 && w2 >= 0) {
                    w0 /= area;
                    w1 /= area;
                    w2 /= area;

                    // Kommentieren Sie eine der Varianten ein!

                    /*
                    // Farbe bestimmen Variante 1 - konstante Farbe, hier Rot
                    set(i,j,color(255,0,0));  // set-Pixel von Processing
                    */

                    /*
                    // Farbe bestimmen Variante 2 - Licht von vorne, eine Normale pro Dreieck
                    Vec3 light = Vec3.of(0,0,-1);
                    Vec3 normal = current.multFree(face.getNormal()).unit();
                    int colorVal = (int)(normal.skalarProd(light)*255);
                    set(i,j,color(colorVal, colorVal,colorVal));
                    */

                    /*
                    // Farbe bestimmen Variante 3 - Licht von vorne, eine Normale pro Dreieck, mit z-Buffer
                    Vec3 light = Vec3.of(0,0,-1);
                    Vec3 normal = current.multFree(face.getNormal()).unit();
                    int colorVal = (int)(normal.skalarProd(light)*255);
                    float z = (w0 * z0 + w1 * z1 + w2 * z2);
                    setPixelWithZBuffer(i,j,z,color(colorVal, colorVal,colorVal));
                    */

                    // Farbe bestimmen Variante 4 - Licht von vorne, Normalenvektor wird interpoliert, mit z-Buffer
                    Vec3 light = Vec3.of(0,0,-1);
                    Vec3 normal = n0.mult(w0).add(n1.mult(w1)).add(n2.mult(w2)).unit();
                    int colorVal = (int)(normal.skalarProd(light)*255);
                    float z = (w0 * z0 + w1 * z1 + w2 * z2);
                    setPixelWithZBuffer(i,j,z,color(colorVal, colorVal,colorVal));


                    /*
                    // Farbe bestimmen Variante 5 - Licht von vorne, Normalenvektor wird interpoliert, mit z-Buffer
                    // und Comic-Shader
                    Vec3 light = Vec3.of(0,0,-1);
                    Vec3 normal = n0.mult(w0).add(n1.mult(w1)).add(n2.mult(w2)).unit();
                    float z = (w0 * z0 + w1 * z1 + w2 * z2);
                    int colFromShader = pixelShader(normal);
                    setPixelWithZBuffer(i,j,z,colFromShader);
                    */

                    /*
                    // Farbe bestimmen Variante 6 - z-Wert als Pixelfarbe, ohne z-buffer
                    // und Comic-Shader
                    float z = (w0 * z0 + w1 * z1 + w2 * z2);
                    set(i,j,color(z/4));
                    */

                    /*
                    // Farbe bestimmen Variante 6 - z-Wert als Pixelfarbe, mit z-buffer
                    // und Comic-Shader
                    float z = (w0 * z0 + w1 * z1 + w2 * z2);
                    setPixelWithZBuffer(i,j,z, color(z/4));
                    */
                }
            }

        }
    }

    private int pixelShader(Vec3 normal) {
        Vec3 light = Vec3.of(0,0,-1);
        int colorVal = (int)(normal.skalarProd(light)*255);

        int finalColor = 0;

        if (colorVal > 0.9*255)
            finalColor = color(255,128,128);
        else if (colorVal > 0.5*255)
            finalColor = color(200,100,100);
        else if (colorVal > 0.25*255)
            finalColor = color(100,50,50);
        else
            finalColor = color(50,25,25);

        return finalColor;
    }

    // Einfaches Setzen eines Pixels (x,y) mit zBuffer
    // Pixel wird nur gezeichnet, wenn er VOR den bereits gezeichneten liegt (abh. von z)
    private void setPixelWithZBuffer(int x, int y, float z, int col) {
        if (x >= 0 && x < SCREEN_X && y >= 0 && y < SCREEN_Y) {
            if (zbuffer[x][y] > z) {
                set(x, y, col);
                zbuffer[x][y] = z;
            }
        }
    }

    // Linie zeichnen, mit Welttransformation und Projektion
    public void line (Vec3 a, Vec3 b) {
        Vec3 a_ = projectToScreen.mult(current.mult(a));
        Vec3 b_ = projectToScreen.mult(current.mult(b));
        line(a_.x ,a_.y, b_.x, b_.y);
    }

    // Hilfsfunktionen - Manipulation der Welttransformation


    private void mscale(float f) {
        current = Matrix.scale(f).mult(current);
    }

    // aktuelle Transformation um Rotation erweitern
    private void mrotateZ(float rotation) {
        current = Matrix.rotateZ(rotation).mult(current);
    }

    private void mrotateX(float rotation) {
        current = Matrix.rotateX(rotation).mult(current);
    }

    private void mrotateY(float rotation) {
        current = Matrix.rotateY(rotation).mult(current);
    }

    // aktuelle Transformation um Translation erweitern
    private void mtranslate(int x, int y, int z) {
        current = Matrix.translate(x,y,z).mult(current);
    }

    // aktuelle Transformation zurücksetzen
    private void mresetMatrix() {
        current = Matrix.unit();
    }

}

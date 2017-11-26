package main;


import java.util.ArrayList;

/**
 * Verwaltet einen Linienzug
 */
public class Face {
    // Punkte eines Dreiecks
    public final ArrayList<Vec3> points = new ArrayList<>();

    // Normalen auf den Dreiecksecken
    public final ArrayList<Vec3> normals = new ArrayList<>();

    // Hilfsfunktion - eine Normale aus Dreieckskanten berechnen
    public Vec3 getNormal() {
        Vec3 v1 = points.get(1).sub(points.get(0));
        Vec3 v2 = points.get(2).sub(points.get(1));

        return v1.cross(v2);
    }

    // Hilfsfunktion - Viereck in zwei Dreiecke umwandeln
    // wird vom Loader verwendet
    public Face[] triangulateQuad() {
        Face f1 = new Face();
        Face f2 = new Face();

        f1.points.add(points.get(0));
        f1.points.add(points.get(1));
        f1.points.add(points.get(2));

        f2.points.add(points.get(2));
        f2.points.add(points.get(3));
        f2.points.add(points.get(0));

        if (normals.size() == 4) {
            f1.normals.add(normals.get(0));
            f1.normals.add(normals.get(1));
            f1.normals.add(normals.get(2));

            f2.normals.add(normals.get(2));
            f2.normals.add(normals.get(3));
            f2.normals.add(normals.get(0));
        } else {
            f1.normals.add(f1.getNormal());
            f1.normals.add(f1.getNormal());
            f1.normals.add(f1.getNormal());
            f2.normals.add(f2.getNormal());
            f2.normals.add(f2.getNormal());
            f2.normals.add(f2.getNormal());
        }


        return new Face[] { f1, f2 };
    }
}

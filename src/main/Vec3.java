package main;

/**
 * Eine einfache Klasse für Vektorrechnung
 */
public class Vec3 {
    // wie genau vergleichen wir?
    private static final float EPSILON = 0.0001f;

    // Vektoren sind unveränderlich - jede Rechenoperation erzeugt einen neuen Vektor
    public final float x;
    public final float y;
    public final float z;

    // Konstruktor privat - wir nutzen eine einfache Factory-Methode
    private Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Factory
    public static Vec3 of(float x, float y, float z) {
        return new Vec3(x,y,z);
    }

    // plus, minus, mal, geteilt
    public Vec3 add(Vec3 other) {
        return new Vec3(this.x+other.x, this.y + other.y, this.z+other.z);
    }

    public Vec3 sub(Vec3 other) {
        return this.add(other.mult(-1));
    }

    public Vec3 mult(float d) {
        return new Vec3(d*x, d*y, d*z);
    }

    public Vec3 div(float d) {
        return mult(1/d);
    }

    // Länge des Vektors
    public float length() {
        return (float)Math.sqrt(lengthSquare());
    }

    // Normieren (Vektor durch seine Länge teilen)
    public Vec3 unit() {
        return mult(1/length());
    }

    // Skalarprodukt
    public float skalarProd(Vec3 other) {
        return x*other.x + y*other.y + z*other.z;
    }

    // Länge im Quadrat - Hilfsfunktion
    public float lengthSquare() {
        return x*x + y*y + z*z;
    }

    // Vorzeichenwechsel (-v)
    public Vec3 neg() {
        return mult(-1);
    }

    public Vec3 cross(Vec3 v) {
        return Vec3.of(
                y*v.z - z * v.y,
                z*v.x - x * v.z,
                x*v.y - y * v.x
        );
    }

    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3 vec3 = (Vec3) o;

        if (Math.abs(vec3.x -  x) > EPSILON) return false;
        if (Math.abs(vec3.y -  y) > EPSILON) return false;
        return Math.abs(vec3.z -  z) < EPSILON;
    }


}

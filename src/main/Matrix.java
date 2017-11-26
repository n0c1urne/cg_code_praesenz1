package main;

import java.util.Arrays;

public class Matrix {
    // die 4x4-Matrix
    private final float[] elems;

    // Konstruktor
    private Matrix(float[] elems) {
        assert (elems.length == 16);
        this.elems = elems;
    }

    // sin und cos für float
    private static float sin(float alpha) {
        return (float) Math.sin(alpha);
    }

    private static float cos(float alpha) {
        return (float) Math.cos(alpha);
    }

    // Factory
    public static Matrix of(float a1, float a2, float a3, float a4,
                            float a5, float a6, float a7, float a8,
                            float a9, float a10, float a11, float a12,
                            float a13, float a14, float a15, float a16) {
        return new Matrix(new float[]{
                a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16
        });
    }

    // einige Standard-Matrizen für typische Abbildungen

    // einheitsMatrix
    public static Matrix unit() {
        return Matrix.of(
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1
        );
    }

    // Zentralprojektion
    public static Matrix project(float d) {
        return Matrix.of(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 0, 0,
                0, 0, 1/d, 1
        );
    }

    // Rotationen
    public static Matrix rotateZ(float alpha) {
        return Matrix.of(
                cos(alpha), -sin(alpha), 0, 0,
                sin(alpha), cos(alpha), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix rotateX(float alpha) {
        return Matrix.of(
                1, 0, 0, 0,
                0, cos(alpha), -sin(alpha), 0,
                0, sin(alpha), cos(alpha), 0,
                0, 0, 0, 1
        );
    }

    public static Matrix rotateY(float alpha) {
        return Matrix.of(
                cos(alpha), 0, sin(alpha), 0,
                0, 1, 0, 0,
                -sin(alpha), 0, cos(alpha), 0,
                0, 0, 0, 1
        );
    }

    // Translation
    public static Matrix translate(float x, float y, float z) {
        return Matrix.of(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        );
    }

    // Skalierung
    public static Matrix scale(float sx, float sy, float sz) {
        return Matrix.of(
                sx, 0, 0, 0,
                0, sy, 0, 0,
                0, 0, sz, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix scale(float s) {
        return Matrix.scale(s, s, s);
    }


    // Matrix x Matrix
    public Matrix mult(Matrix v) {
        float[] res = new float[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int n=0; n<4; n++) {
                    res[4*row + col] += elems[row*4+ n]*v.elems[n*4 + col];
                }
            }
        }

        return new Matrix(res);
    }

    // Matrix x Vector
    public Vec3 mult(Vec3 v) {
        float [] res = new float[4];
        float [] v_arr = {v.x, v.y, v.z, 1};
        for (int row=0; row < 4; row++) {
            for(int col=0; col<4; col++) {
                res[row] += elems[row*4+col] * v_arr[col];
            }
        }
        return Vec3.of(res[0]/res[3], res[1]/res[3], res[2]/res[3]);
    }

    // Matrix x Vector für freie Vektoren
    // "Hack" für die Behandlung von Normalenvektoren
    // (Normalenvektoren dürfen nur rotiert werden, Translation sollte nicht stattfinden!)
    public Vec3 multFree(Vec3 v) {
        float [] res = new float[4];
        float [] v_arr = {v.x, v.y, v.z, 0};
        for (int row=0; row < 4; row++) {
            for(int col=0; col<4; col++) {
                res[row] += elems[row*4+col] * v_arr[col];
            }
        }
        return Vec3.of(res[0], res[1], res[2]);
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "elems=" + Arrays.toString(elems) +
                '}';
    }


}

package main;

import org.junit.Test;

import static org.junit.Assert.*;


// einige unit-tests für Vektoren und Matrizen, bei weitem nicht vollständig
public class TestVec {
    @Test
    public void testAdd() {
        assertEquals(
                Vec3.of(5,7,9),
                Vec3.of(1,2,3).add(Vec3.of(4,5,6)));
    }

    @Test
    public void testSub() {
        assertEquals(
                Vec3.of(0,0,0),
                Vec3.of(1,2,3).sub(Vec3.of(1,2,3)));
    }

    @Test
    public void testMult() {
        assertEquals(
                Vec3.of(2,4,6),
                Vec3.of(1,2,3).mult(2));
    }

    @Test
    public void testDiv() {
        assertEquals(
                Vec3.of(1,2,3),
                Vec3.of(2,4,6).div(2));
    }

    @Test
    public void testLengthSquare() {
        assertEquals(
                1+4+9,
                Vec3.of(1,2,3).lengthSquare(),
                0.001);
    }

    @Test
    public void testLength() {
        assertEquals(
                Math.sqrt(1+4+9),
                Vec3.of(1,2,3).length(),
                0.001);
    }

    @Test
    public void testUnit() {
        assertEquals(
                Vec3.of(1,2,3).unit().length(),
                1.0,
                0.001);
    }

    @Test
    public void testUnitMatrix() {
        assertEquals(
                Vec3.of(1,2,3),
                Matrix.unit().mult(Vec3.of(1,2,3)));
    }

    @Test
    public void testRotations() {
        float PIHalf = (float)Math.PI/2;
        assertEquals(Vec3.of(1,1,1), Matrix.rotateX(PIHalf).mult(Vec3.of(1,1,-1)));
        assertEquals(Vec3.of(1,1,-1), Matrix.rotateY(PIHalf).mult(Vec3.of(1,1,1)));
        assertEquals(Vec3.of(-1,1, 1), Matrix.rotateZ(PIHalf).mult(Vec3.of(1,1,1)));
    }

    @Test
    public void testMatrixMultiplication() {
        assertEquals(
                Vec3.of(1,2,3),
                Matrix
                        .unit()
                        .mult(Matrix.unit())
                        .mult(Vec3.of(1,2,3)));

        Vec3 one = Matrix.rotateX(1.0f)
                .mult(Matrix.rotateY(2.0f))
                .mult(Matrix.rotateZ(3.0f))
                .mult(Vec3.of(1,2,3));

        Vec3 two = Matrix.rotateX(1.0f).mult(
                Matrix.rotateY(2.0f).mult(
                        Matrix.rotateZ(3.0f).mult(
                                Vec3.of(1,2,3))));

        assertEquals(one, two);
    }



}

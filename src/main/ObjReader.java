package main;

import java.io.*;
import java.util.*;

// ein extrem simpler Parser für .obj-Files
// liest nur vertices und faces, ignoriert alle anderen Zeilen
public class ObjReader {
    private static List<Face> parse(InputStream reader) {
        List<Face> faces = new LinkedList<>();
        List<Vec3> points = new LinkedList<>();
        List<Vec3> normals = new LinkedList<>();

        Scanner s = new Scanner(reader).useLocale(Locale.US);
        while (s.hasNext()) {
            if (s.hasNext("v")) {
                s.next("v"); // consume

                points.add(Vec3.of(
                        s.nextFloat(),
                        s.nextFloat(),
                        s.nextFloat()
                ));

            } else if (s.hasNext("vn")) {
                s.next("vn"); // consume

                normals.add(Vec3.of(
                        s.nextFloat(),
                        s.nextFloat(),
                        s.nextFloat()
                ));


            } else if (s.hasNext("f")) {
                s.next("f");  // consume

                Face f = new Face();
                while(s.hasNext("\\d+(/\\d+)?(/\\d+)?")) {
                    String[] nrs = s.next("\\d+(/\\d+)?(/\\d+)?").split("/");
                    f.points.add(points.get(Integer.parseInt(nrs[0])-1));

                    if (nrs.length > 2) {
                        f.normals.add(normals.get(Integer.parseInt(nrs[2])-1));
                    }

                }

                if (f.points.size() == 4) {
                    Face[] subs = f.triangulateQuad();
                    faces.add(subs[0]);
                    faces.add(subs[1]);
                }
                if (f.points.size() == 3) {
                    if (f.normals.size() == 0) {
                        f.normals.add(f.getNormal());
                        f.normals.add(f.getNormal());
                        f.normals.add(f.getNormal());
                    }
                    faces.add(f);
                }
            } else {
                System.out.println("Skipping "+s.nextLine());
            }
        }

        System.out.println("found "+faces.size()+ "faces.");
        return faces;
    }

    public static List<Face> readFile(String filename) {

        try (InputStream fis = new FileInputStream(filename)) {
            return parse(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

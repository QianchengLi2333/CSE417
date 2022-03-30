import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

public class hw5 {

    public static List<Point> readFile(Scanner input) throws FileNotFoundException {
        List<Point> points = new ArrayList<>();
        while (input.hasNextDouble()) {
            double x = input.nextDouble();
            double y = input.nextDouble();
            Point p = new Point(x, y);
            points.add(p);
        }
        return points;
    }

    public static void version1(List<Point> points) {
//        long startTime = currentTimeMillis();
        long startTime = nanoTime();
        double minDis = 10000.0;
        Point finalA = new Point();
        Point finalB = new Point();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point a = points.get(i);
                Point b = points.get(j);
                double dis = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
                if (dis < minDis) {
                    minDis = dis;
                    finalA = a;
                    finalB = b;
                }
            }
        }
//        long endTime = currentTimeMillis();
        long endTime = nanoTime();
        double time = (endTime - startTime) * 1.0 / 10000.0;
        System.out.println("Version 1, " + points.size() + ", " + finalA.getX() + ", " + finalA.getY() + ", " +
                finalB.getX() + ", " + finalB.getY() + ", " + minDis + ", " + time);
        return;
    }

    public static void version2(List<Point> points) {
//        long startTime = currentTimeMillis();
        long startTime = nanoTime();
        double minDis = 0.0;
        if (points.size() == 0) {
            System.out.print("Version 2, " + 0);
            return;
        }
        if (points.size() == 1) {
            Point finalA = points.get(0);
            System.out.print("Version 2, " + points.size() + ", " + finalA.getX() + ", " + finalA.getY());
            return;
        }
        if (points.size() == 2) {
            Point finalA = points.get(0);
            Point finalB = points.get(1);
            minDis = points.get(0).distance(points.get(1));
            long endTime = currentTimeMillis();
            double time = (endTime - startTime) * 1.0;
            System.out.print("Version 2, " + points.size() + ", " + finalA.getX() + ", " + finalA.getY() + ", " +
                    finalB.getX() + ", " + finalB.getY() + ", " + minDis  + ", " + time);
            return;
        }
        List<Point> points2 = sort(points);
        Point[] pointsByY = new Point[points2.size()];
        double[] result = version2(points2, pointsByY, 0, points2.size() - 1);
//        long endTime = currentTimeMillis();
        long endTime = nanoTime();
        double time = (endTime - startTime) * 1.0 / 10000.0;
        System.out.println("Version 2, " + points.size() + ", " + result[1] + ", " + result[2] + ", " + result[3] +
                ", " + result[4] + ", " + result[0] + ", " + time);
        return;
    }

    public static double[] version2(List<Point> points, Point[] points4, int low, int high) {
        double[] result = new double[5];
        if (low == high) {
            Point only = points.get(low);
            result[0] = 10000.0;
            result[1] = only.getX();
            result[2] = only.getY();
            result[3] = only.getX();
            result[4] = only.getY();
            return result;
        }
//        if (low + 1 == high) {
//            Point finalA = points.get(low);
//            Point finalB = points.get(high);
//            result[0] = finalA.distance(finalB);
//            result[1] = finalA.getX();
//            result[2] = finalA.getY();
//            result[3] = finalB.getX();
//            result[4] = finalB.getY();
//            return result;
//        }
//        if (low + 2 == high) {
//            double min1 = points.get(low).distance(points.get(low + 1));
//            double min2 = points.get(low).distance(points.get(low + 2));
//            double min3 = points.get(low + 1).distance(points.get(low + 2));
//            double min4 = Math.min(min1, min2);
//            double min = Math.min(min3, min4);
//            result[0] = min;
//            if (min == min1) {
//                result[1] = points.get(low).getX();
//                result[2] = points.get(low).getY();
//                result[3] = points.get(low + 1).getX();
//                result[4] = points.get(low + 1).getY();
//            } else if (min == min2) {
//                result[1] = points.get(low).getX();
//                result[2] = points.get(low).getY();
//                result[3] = points.get(low + 2).getX();
//                result[4] = points.get(low + 2).getY();
//            } else if (min == min3) {
//                result[1] = points.get(low + 1).getX();
//                result[2] = points.get(low + 1).getY();
//                result[3] = points.get(low + 2).getX();
//                result[4] = points.get(low + 2).getY();
//            }
//            return result;
//        }
        int mid = (low + high) / 2;
        Point middle = points.get(mid);
        double[] min1 = version2(points, points4, low, mid);
        double[] min2 = version2(points, points4, mid + 1, high);
        double min = Math.min(min1[0], min2[0]);
        if (min == min1[0]) {
            result[0] = min;
            result[1] = min1[1];
            result[2] = min1[2];
            result[3] = min1[3];
            result[4] = min1[4];
        } else if (min == min2[0]) {
            result[0] = min;
            result[1] = min2[1];
            result[2] = min2[2];
            result[3] = min2[3];
            result[4] = min2[4];
        }
//        merge(points, points4, low, mid, high);
//        Point[] points5 = new Point[points.size()];
        int a = low;
        int b = mid + 1;
        for (int i = low; i <= high; i++) {
            System.out.println(low);
            System.out.println(high);
            System.out.println(min);
            if (Math.abs(points.get(i).getX() - middle.getX()) <= min) {
                if (a > mid) {
                    points4[i] = points.get(b);
                    b++;
                } else if (b > high) {
                    points4[i] = points.get(a);
                    a++;
                } else if (points.get(a).getY() < points.get(b).getY()) {
                    System.out.println("情况1");
                    System.out.println("比较" + points.get(a).getX() + " " + points.get(a).getY());
                    System.out.println("比较" + points.get(b).getX() + " " + points.get(b).getY());
                    points4[i] = points.get(a);
                    a++;
                } else if (points.get(a).getY() > points.get(b).getY()) {
                    System.out.println("情况2");
                    System.out.println("比较" + points.get(a).getX() + " " + points.get(a).getY());
                    System.out.println("比较" + points.get(b).getX() + " " + points.get(b).getY());
                    points4[i] = points.get(b);
                    b++;
                } else if (points.get(a).getY() == points.get(b).getY()) {
                    if (points.get(a).getX() <= points.get(b).getX()) {
                        System.out.println("情况3");
                        System.out.println("比较" + points.get(a).getX() + " " + points.get(a).getY());
                        System.out.println("比较" + points.get(b).getX() + " " + points.get(b).getY());
                        points4[i] = points.get(a);
                        a++;
                    } else {
                        System.out.println("情况4");
                        System.out.println("比较" + points.get(a).getX() + " " + points.get(a).getY());
                        System.out.println("比较" + points.get(b).getX() + " " + points.get(b).getY());
                        points4[i] = points.get(b);
                        b++;
                    }
                }
            } else {
                points4[i] = null;
            }
            for (int c = 0; c < points4.length; c++) {
                if (points4[c] != null) {
                    System.out.println(points4[c].getX() + " " + points4[c].getY());
                } else {
                    System.out.println("null");
                }
            }
            System.out.println("分界线");
        }

//        Collections.sort(points5, Comparator.comparing(Point::getY));
        for (int i = 0; i < points4.length - 1; i++) {
            if (points4[i] != null) {
                int j = 1;
                while (j <= 7 && i + j < points4.length && points4[i + j] != null) {
                    double distance = points4[i].distance(points4[i + j]);
                    if (distance < min) {
                        min = distance;
                        result[0] = min;
                        result[1] = points4[i].getX();
                        result[2] = points4[i].getY();
                        result[3] = points4[i + j].getX();
                        result[4] = points4[i + j].getY();
                    }
                    j++;
                }
            }
        }
        return result;
    }

    public static List<Point> sort(List<Point> points) {
        List<Point> points2 = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            points2.add(points.get(i));
        }
        Collections.sort(points2, Comparator.comparing(Point::getX));
        return points2;
    }

    public static void merge(List<Point> points, Point[] points4, int low, int mid, int high) {
        for (int i = 0; i < points.size(); i++) {
            points4[i] = points.get(i);
        }
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                points.set(k, points4[j]);
                j++;
            } else if (j > high) {
                points.set(k, points4[i]);
                i++;
            } else if (points4[i].getY() < points4[j].getY()) {
                points.set(k, points4[i]);
                i++;
            } else if (points4[i].getY() > points4[j].getY()) {
                points.set(k, points4[j]);
                j++;
            } else if (points4[i].getY() == points4[j].getY()) {
                if (points4[i].getX() <= points4[j].getX()) {
                    points.set(k, points4[i]);
                    i++;
                } else {
                    points.set(k, points4[j]);
                    j++;
                }
            }
        }
//        return pointsByY;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String file = "test2.txt";
        Scanner input = new Scanner(new File(file));
        List<Point> points = readFile(input);
        version1(points);
        version2(points);
    }

    public static class Point {
        private double x;
        private double y;

        public Point() {
            x = 0.0;
            y = 0.0;
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

//        public int compareTo(Point point) {
//            if (this.getX() != point.getX()) {
//                return (int) (this.getX() - point.getX());
//            } else if (this.getY() != point.getY()) {
//                return (int) (this.getY() - point.getY());
//            } else {
//                return 0;
//                    }
//        }

        public double distance(Point point) {
            return Math.sqrt(Math.pow(this.getX() - point.getX(), 2) + Math.pow(this.getY() - point.getY(), 2));
        }
    }
}

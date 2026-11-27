/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skiplistprojektas.calcalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Skip list efektyvumo tyrimas: palyginimas su TreeSet, surikiuotu ArrayList ir LinkedList.
 * Kiekvienas eksperimentas kartojamas KARTOJIMU kartų, rodomas vidurkis (ms).
 * @author EdasT
 */
public class Greitaveika {

    private static final int[] KIEKIAI = {2_000, 4_000, 8_000, 16_000, 32_000};
    private static final int KARTOJIMU = 3;
    private static final int PAIESKU = 2_000;

    public static void main(String[] args) {
        apsilimas();
        System.out.println("Laikas milisekundėmis, " + KARTOJIMU + " kartojimų vidurkis\n");

        System.out.println("=== add (n elementų) ===");
        antraste();
        for (int n : KIEKIAI) {
            Integer[] duom = duomenys(n, n);
            eilute(n, vidurkis(() -> pildytiSkipList(duom)),
                      vidurkis(() -> pildytiTreeSet(duom)),
                      vidurkis(() -> pildytiArrayList(duom)),
                      vidurkis(() -> pildytiLinkedList(duom)));
        }

        System.out.println("\n=== contains (" + PAIESKU + " paieškų) ===");
        antraste();
        for (int n : KIEKIAI) {
            Integer[] duom = duomenys(n, n);
            Integer[] ieskomi = duomenys(PAIESKU, n + 1);
            SkipList<Integer> sk = pildytiSkipList(duom);
            TreeSet<Integer> ts = pildytiTreeSet(duom);
            List<Integer> al = pildytiArrayList(duom);
            List<Integer> ll = pildytiLinkedList(duom);
            eilute(n, vidurkis(() -> { for (Integer x : ieskomi) sk.contains(x); }),
                      vidurkis(() -> { for (Integer x : ieskomi) ts.contains(x); }),
                      vidurkis(() -> { for (Integer x : ieskomi) Collections.binarySearch(al, x); }),
                      vidurkis(() -> { for (Integer x : ieskomi) ll.contains(x); }));
        }

        System.out.println("\n=== remove (visi n elementų) ===");
        antraste();
        for (int n : KIEKIAI) {
            Integer[] duom = duomenys(n, n);
            eilute(n, vidurkis(() -> { SkipList<Integer> s = pildytiSkipList(duom); for (Integer x : duom) s.remove(x); }),
                      vidurkis(() -> { TreeSet<Integer> s = pildytiTreeSet(duom); for (Integer x : duom) s.remove(x); }),
                      vidurkis(() -> { List<Integer> s = pildytiArrayList(duom);
                                       for (Integer x : duom) { int i = Collections.binarySearch(s, x); if (i >= 0) s.remove(i); } }),
                      vidurkis(() -> { List<Integer> s = pildytiLinkedList(duom); for (Integer x : duom) s.remove(x); }));
        }

        System.out.println("\n=== Iteravimas (visi n elementų) ===");
        antraste();
        for (int n : KIEKIAI) {
            Integer[] duom = duomenys(n, n);
            SkipList<Integer> sk = pildytiSkipList(duom);
            TreeSet<Integer> ts = pildytiTreeSet(duom);
            List<Integer> al = pildytiArrayList(duom);
            List<Integer> ll = pildytiLinkedList(duom);
            eilute(n, vidurkis(() -> { long s = 0; for (Integer x : sk) s += x; }),
                      vidurkis(() -> { long s = 0; for (Integer x : ts) s += x; }),
                      vidurkis(() -> { long s = 0; for (Integer x : al) s += x; }),
                      vidurkis(() -> { long s = 0; for (Integer x : ll) s += x; }));
        }

        System.out.println("\n=== Skip list: contains vs indexOf (" + PAIESKU + " paieškų) ===");
        System.out.printf("%8s %12s %12s%n", "n", "contains", "indexOf");
        for (int n : KIEKIAI) {
            Integer[] duom = duomenys(n, n);
            Integer[] ieskomi = duomenys(PAIESKU, n + 1);
            SkipList<Integer> sk = pildytiSkipList(duom);
            System.out.printf("%8d %12.3f %12.3f%n", n,
                    vidurkis(() -> { for (Integer x : ieskomi) sk.contains(x); }),
                    vidurkis(() -> { for (Integer x : ieskomi) sk.indexOf(x); }));
        }

        System.out.println("\n=== Atminties sąnaudos (n = 32000) ===");
        Integer[] duom = duomenys(32_000, 32_000);
        System.out.printf("SkipList:   %6d KB%n", atmintis(() -> pildytiSkipList(duom)));
        System.out.printf("TreeSet:    %6d KB%n", atmintis(() -> pildytiTreeSet(duom)));
        System.out.printf("ArrayList:  %6d KB%n", atmintis(() -> pildytiArrayList(duom)));
        System.out.printf("LinkedList: %6d KB%n", atmintis(() -> pildytiLinkedList(duom)));
    }

    /** Sugeneruoja n atsitiktinių skaičių (fiksuotas seed – rezultatai pakartojami). */
    private static Integer[] duomenys(int n, long seed) {
        Random r = new Random(seed);
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) {
            a[i] = r.nextInt(n * 10);
        }
        return a;
    }

    private static SkipList<Integer> pildytiSkipList(Integer[] d) {
        SkipList<Integer> s = new SkipList<>();
        for (Integer x : d) s.add(x);
        return s;
    }

    private static TreeSet<Integer> pildytiTreeSet(Integer[] d) {
        TreeSet<Integer> s = new TreeSet<>();
        for (Integer x : d) s.add(x);
        return s;
    }

    /** Surikiuotas ArrayList: vieta randama dvejetaine paieška, elementai pastumiami. */
    private static List<Integer> pildytiArrayList(Integer[] d) {
        List<Integer> s = new ArrayList<>();
        for (Integer x : d) {
            int i = Collections.binarySearch(s, x);
            if (i < 0) s.add(-i - 1, x);
        }
        return s;
    }

    /** LinkedList: einama nuo pradžios iki tinkamos vietos. */
    private static List<Integer> pildytiLinkedList(Integer[] d) {
        LinkedList<Integer> s = new LinkedList<>();
        for (Integer x : d) {
            var it = s.listIterator();
            boolean yra = false;
            while (it.hasNext()) {
                int c = it.next().compareTo(x);
                if (c == 0) { yra = true; break; }
                if (c > 0) { it.previous(); break; }
            }
            if (!yra) it.add(x);
        }
        return s;
    }

    /** Paleidžia veiksmą KARTOJIMU kartų ir grąžina vidutinį laiką ms. */
    private static double vidurkis(Runnable veiksmas) {
        long suma = 0;
        for (int k = 0; k < KARTOJIMU; k++) {
            System.gc();
            long t0 = System.nanoTime();
            veiksmas.run();
            suma += System.nanoTime() - t0;
        }
        return suma / (double) KARTOJIMU / 1_000_000;
    }

    /** Apytikslė struktūros užimama atmintis KB. */
    private static long atmintis(java.util.function.Supplier<Object> kurti) {
        Runtime rt = Runtime.getRuntime();
        System.gc();
        long pries = rt.totalMemory() - rt.freeMemory();
        Object o = kurti.get();
        System.gc();
        long po = rt.totalMemory() - rt.freeMemory();
        if (o.hashCode() == 42) System.out.print("");
        return (po - pries) / 1024;
    }

    /** JVM „apšildymas“, kad pirmieji matavimai nebūtų iškreipti. */
    private static void apsilimas() {
        Integer[] d = duomenys(20_000, 7);
        for (int i = 0; i < 3; i++) {
            pildytiSkipList(d);
            pildytiTreeSet(d);
            pildytiArrayList(d);
        }
    }

    private static void antraste() {
        System.out.printf("%8s %12s %12s %12s %12s%n", "n", "SkipList", "TreeSet", "ArrayList", "LinkedList");
    }

    private static void eilute(int n, double a, double b, double c, double d) {
        System.out.printf("%8d %12.3f %12.3f %12.3f %12.3f%n", n, a, b, c, d);
    }
}

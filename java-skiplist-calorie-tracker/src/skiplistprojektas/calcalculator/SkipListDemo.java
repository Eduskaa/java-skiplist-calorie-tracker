/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skiplistprojektas.calcalculator;

/**
 * Skip list demonstracija ir paprasti testai.
 * @author EdasT
 */
public class SkipListDemo {

    public static void main(String[] args) {
        SkipList<Integer> sk = new SkipList<>();

        System.out.println("=== add ===");
        int[] reiksmes = {19, 3, 25, 7, 12, 31, 7};
        for (int r : reiksmes) {
            System.out.println("add(" + r + ") -> " + sk.add(r));
        }
        System.out.println("Sąrašas: " + sk + ", dydis = " + sk.size());

        System.out.println("\n=== contains ===");
        System.out.println("contains(12) -> " + sk.contains(12));
        System.out.println("contains(13) -> " + sk.contains(13));

        System.out.println("\n=== indexOf / get ===");
        System.out.println("indexOf(19) -> " + sk.indexOf(19));
        System.out.println("indexOf(100) -> " + sk.indexOf(100));
        System.out.println("get(0) -> " + sk.get(0) + ", get(5) -> " + sk.get(5));

        System.out.println("\n=== remove ===");
        System.out.println("remove(12) -> " + sk.remove(12));
        System.out.println("remove(12) -> " + sk.remove(12));
        System.out.println("Sąrašas: " + sk + ", dydis = " + sk.size());

        System.out.println("\n=== String elementai ===");
        SkipList<String> zodziai = new SkipList<>();
        for (String z : new String[]{"obuolys", "bananas", "kivi", "apelsinas"}) {
            zodziai.add(z);
        }
        System.out.println("Sąrašas: " + zodziai);

        System.out.println("\n=== clear ===");
        sk.clear();
        System.out.println("Sąrašas: " + sk + ", tuščias = " + sk.isEmpty());

        System.out.println("\n=== Didelis testas ===");
        didelisTestas(100_000);
    }

    /** Prideda ir šalina atsitiktinius skaičius, tikrina rezultatą su TreeSet. */
    private static void didelisTestas(int n) {
        SkipList<Integer> sk = new SkipList<>();
        java.util.TreeSet<Integer> etalonas = new java.util.TreeSet<>();
        java.util.Random r = new java.util.Random(1);
        for (int i = 0; i < n; i++) {
            int x = r.nextInt(n * 10);
            sk.add(x);
            etalonas.add(x);
        }
        for (int i = 0; i < n / 2; i++) {
            int x = r.nextInt(n * 10);
            sk.remove(x);
            etalonas.remove(x);
        }
        boolean sutampa = java.util.Arrays.equals(sk.toArray(), etalonas.toArray());
        System.out.println("Elementų: " + sk.size() + ", sutampa su TreeSet: " + sutampa);
    }
}
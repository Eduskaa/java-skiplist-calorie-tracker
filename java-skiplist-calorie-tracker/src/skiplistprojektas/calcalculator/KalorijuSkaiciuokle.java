/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skiplistprojektas.calcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Kalorijų skaičiuoklė – taikomoji programa, kurioje produktų katalogas
 * saugomas skip list struktūroje.
 * @author EdasT
 */
public class KalorijuSkaiciuokle {

    private final SkipList<Produktas> katalogas = new SkipList<>();
    private final List<String> dienosIrasai = new ArrayList<>();
    private double dienosKcal = 0;
    private final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        new KalorijuSkaiciuokle().paleisti();
    }

    /** Užpildo katalogą pradiniais produktais ir paleidžia meniu. */
    public void paleisti() {
        pradiniaiProduktai();
        while (true) {
            System.out.println("\n--- Kalorijų skaičiuoklė ---");
            System.out.println("1. Rodyti katalogą");
            System.out.println("2. Ieškoti produkto");
            System.out.println("3. Pridėti produktą");
            System.out.println("4. Pašalinti produktą");
            System.out.println("5. Įrašyti suvalgytą produktą");
            System.out.println("6. Dienos suvestinė");
            System.out.println("0. Baigti");
            System.out.print("Pasirinkimas: ");
            String pasirinkimas = in.nextLine().trim();
            switch (pasirinkimas) {
                case "1" -> rodytiKataloga();
                case "2" -> ieskoti();
                case "3" -> pridetiProdukta();
                case "4" -> pasalintiProdukta();
                case "5" -> irasytiValgi();
                case "6" -> suvestine();
                case "0" -> { System.out.println("Iki!"); return; }
                default -> System.out.println("Neteisingas pasirinkimas");
            }
        }
    }

    private void pradiniaiProduktai() {
        katalogas.add(new Produktas("Obuolys", 52));
        katalogas.add(new Produktas("Bananas", 89));
        katalogas.add(new Produktas("Vištienos krūtinėlė", 165));
        katalogas.add(new Produktas("Ryžiai (virti)", 130));
        katalogas.add(new Produktas("Kiaušinis", 155));
        katalogas.add(new Produktas("Avižiniai dribsniai", 389));
        katalogas.add(new Produktas("Pienas 2,5%", 52));
        katalogas.add(new Produktas("Juoda duona", 250));
    }

    private void rodytiKataloga() {
        System.out.println("Katalogas (" + katalogas.size() + " produktų, abėcėlės tvarka):");
        int i = 1;
        for (Produktas p : katalogas) {
            System.out.println("  " + i++ + ". " + p);
        }
    }

    /** Paieška pagal tikslų pavadinimą – naudoja skip list contains ir indexOf. */
    private void ieskoti() {
        System.out.print("Produkto pavadinimas: ");
        Produktas raktas = Produktas.paieskai(in.nextLine());
        if (katalogas.contains(raktas)) {
            int idx = katalogas.indexOf(raktas);
            System.out.println("Rasta: " + katalogas.get(idx) + ", pozicija kataloge " + (idx + 1));
        } else {
            System.out.println("Tokio produkto nėra");
        }
    }

    private void pridetiProdukta() {
        System.out.print("Pavadinimas: ");
        String pav = in.nextLine();
        double kcal = skaitytiSkaiciu("Kalorijos 100 g: ");
        try {
            if (katalogas.add(new Produktas(pav, kcal))) {
                System.out.println("Pridėta");
            } else {
                System.out.println("Toks produktas jau yra");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Klaida: " + e.getMessage());
        }
    }

    private void pasalintiProdukta() {
        System.out.print("Šalinamo produkto pavadinimas: ");
        boolean pasalinta = katalogas.remove(Produktas.paieskai(in.nextLine()));
        System.out.println(pasalinta ? "Pašalinta" : "Tokio produkto nėra");
    }

    private void irasytiValgi() {
        System.out.print("Produkto pavadinimas: ");
        Produktas raktas = Produktas.paieskai(in.nextLine());
        int idx = katalogas.indexOf(raktas);
        if (idx < 0) {
            System.out.println("Tokio produkto nėra kataloge – pirmiausia jį pridėkite");
            return;
        }
        Produktas p = katalogas.get(idx);
        double gramai = skaitytiSkaiciu("Kiekis gramais: ");
        double kcal = p.kalorijos(gramai);
        dienosKcal += kcal;
        dienosIrasai.add(String.format("%s, %.0f g – %.0f kcal", p.getPavadinimas(), gramai, kcal));
        System.out.printf("Įrašyta: %.0f kcal. Šiandien iš viso: %.0f kcal%n", kcal, dienosKcal);
    }

    private void suvestine() {
        if (dienosIrasai.isEmpty()) {
            System.out.println("Šiandien dar nieko neįrašyta");
            return;
        }
        System.out.println("Šiandien suvalgyta:");
        for (String irasas : dienosIrasai) {
            System.out.println("  " + irasas);
        }
        System.out.printf("Iš viso: %.0f kcal%n", dienosKcal);
    }

    private double skaitytiSkaiciu(String klausimas) {
        while (true) {
            System.out.print(klausimas);
            try {
                double x = Double.parseDouble(in.nextLine().trim().replace(',', '.'));
                if (x >= 0) {
                    return x;
                }
            } catch (NumberFormatException e) {
                // kartojame klausimą
            }
            System.out.println("Įveskite neneigiamą skaičių");
        }
    }
}

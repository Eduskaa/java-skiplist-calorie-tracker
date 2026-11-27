/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skiplistprojektas.calcalculator;

/**
 * Maisto produktas: pavadinimas ir kalorijos 100 g.
 * Produktai rikiuojami pagal pavadinimą (be didžiųjų/mažųjų raidžių skirtumo).
 * @author EdasT
 */
public class Produktas implements Comparable<Produktas> {

    private final String pavadinimas;
    private final double kcal100g;

    /** Sukuria produktą su pavadinimu ir kalorijomis 100 g. */
    public Produktas(String pavadinimas, double kcal100g) {
        if (pavadinimas == null || pavadinimas.isBlank()) {
            throw new IllegalArgumentException("Pavadinimas negali būti tuščias");
        }
        if (kcal100g < 0) {
            throw new IllegalArgumentException("Kalorijos negali būti neigiamos");
        }
        this.pavadinimas = pavadinimas.trim();
        this.kcal100g = kcal100g;
    }

    /** Produktas paieškai – tik su pavadinimu. */
    public static Produktas paieskai(String pavadinimas) {
        return new Produktas(pavadinimas, 0);
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public double getKcal100g() {
        return kcal100g;
    }

    /** Apskaičiuoja kalorijas nurodytam gramų kiekiui. */
    public double kalorijos(double gramai) {
        return kcal100g * gramai / 100.0;
    }

    @Override
    public int compareTo(Produktas kitas) {
        return pavadinimas.compareToIgnoreCase(kitas.pavadinimas);
    }

    @Override
    public String toString() {
        return String.format("%s (%.0f kcal/100 g)", pavadinimas, kcal100g);
    }
}

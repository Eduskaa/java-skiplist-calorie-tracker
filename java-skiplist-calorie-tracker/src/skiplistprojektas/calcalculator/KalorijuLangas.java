/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skiplistprojektas.calcalculator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 * Kalorijų skaičiuoklės grafinė sąsaja (Swing).
 * Produktų katalogas saugomas skip list struktūroje.
 * @author EdasT
 */
public class KalorijuLangas extends JFrame {

    private final SkipList<Produktas> katalogas = new SkipList<>();
    private final DefaultListModel<Produktas> katalogoModelis = new DefaultListModel<>();
    private final JList<Produktas> katalogoSarasas = new JList<>(katalogoModelis);
    private final DefaultListModel<String> dienosModelis = new DefaultListModel<>();
    private final JLabel sumaLabel = new JLabel("Šiandien: 0 kcal");
    private double dienosKcal = 0;

    private final JTextField paieskaLaukas = new JTextField();
    private final JTextField pavadinimoLaukas = new JTextField();
    private final JTextField kcalLaukas = new JTextField();
    private final JTextField gramuLaukas = new JTextField("100");

    /** Sukuria langą ir užpildo katalogą pradiniais produktais. */
    public KalorijuLangas() {
        super("Kalorijų skaičiuoklė (Skip list)");
        pradiniaiProduktai();
        sukurtiSasaja();
        atnaujintiKataloga();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 520);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KalorijuLangas().setVisible(true));
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

    private void sukurtiSasaja() {
        // Kairė: katalogas su paieška
        JPanel kaire = new JPanel(new BorderLayout(5, 5));
        kaire.setBorder(BorderFactory.createTitledBorder("Produktų katalogas"));
        JPanel paieskosPanele = new JPanel(new BorderLayout(5, 5));
        JButton ieskotiBtn = new JButton("Ieškoti");
        paieskosPanele.add(paieskaLaukas, BorderLayout.CENTER);
        paieskosPanele.add(ieskotiBtn, BorderLayout.EAST);
        kaire.add(paieskosPanele, BorderLayout.NORTH);
        katalogoSarasas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        kaire.add(new JScrollPane(katalogoSarasas), BorderLayout.CENTER);
        JButton salintiBtn = new JButton("Pašalinti pažymėtą");
        kaire.add(salintiBtn, BorderLayout.SOUTH);

        // Vidurys: naujas produktas ir valgio įrašymas
        JPanel vidurys = new JPanel(new BorderLayout());
        JPanel viduriniai = new JPanel(new GridLayout(2, 1, 5, 10));

        JPanel naujas = new JPanel(new GridLayout(3, 2, 5, 5));
        naujas.setBorder(BorderFactory.createTitledBorder("Naujas produktas"));
        naujas.add(new JLabel("Pavadinimas:"));
        naujas.add(pavadinimoLaukas);
        naujas.add(new JLabel("kcal / 100 g:"));
        naujas.add(kcalLaukas);
        JButton pridetiBtn = new JButton("Pridėti");
        naujas.add(new JLabel());
        naujas.add(pridetiBtn);

        JPanel valgis = new JPanel(new BorderLayout(5, 5));
        valgis.setBorder(BorderFactory.createTitledBorder("Įrašyti suvalgytą"));
        valgis.add(new JLabel("Pažymėkite produktą kataloge"), BorderLayout.NORTH);
        JPanel valgioLaukai = new JPanel(new GridLayout(2, 2, 5, 5));
        valgioLaukai.add(new JLabel("Kiekis (g):"));
        valgioLaukai.add(gramuLaukas);
        JButton irasytiBtn = new JButton("Įrašyti");
        valgioLaukai.add(new JLabel());
        valgioLaukai.add(irasytiBtn);
        valgis.add(valgioLaukai, BorderLayout.CENTER);

        viduriniai.add(naujas);
        viduriniai.add(valgis);
        vidurys.add(viduriniai, BorderLayout.NORTH);

        // Dešinė: dienos suvestinė
        JPanel desine = new JPanel(new BorderLayout(5, 5));
        desine.setBorder(BorderFactory.createTitledBorder("Šiandien suvalgyta"));
        desine.add(new JScrollPane(new JList<>(dienosModelis)), BorderLayout.CENTER);
        sumaLabel.setFont(sumaLabel.getFont().deriveFont(Font.BOLD, 16f));
        JButton isvalytiBtn = new JButton("Nauja diena");
        JPanel apacia = new JPanel(new BorderLayout());
        apacia.add(sumaLabel, BorderLayout.CENTER);
        apacia.add(isvalytiBtn, BorderLayout.EAST);
        desine.add(apacia, BorderLayout.SOUTH);

        JPanel visas = new JPanel(new GridLayout(1, 3, 10, 10));
        visas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        visas.add(kaire);
        visas.add(vidurys);
        visas.add(desine);
        setContentPane(visas);

        ieskotiBtn.addActionListener(e -> ieskoti());
        paieskaLaukas.addActionListener(e -> ieskoti());
        pridetiBtn.addActionListener(e -> pridetiProdukta());
        salintiBtn.addActionListener(e -> pasalintiProdukta());
        irasytiBtn.addActionListener(e -> irasytiValgi());
        isvalytiBtn.addActionListener(e -> naujaDiena());
    }

    /** Perkelia skip list turinį į JList (abėcėlės tvarka). */
    private void atnaujintiKataloga() {
        katalogoModelis.clear();
        for (Produktas p : katalogas) {
            katalogoModelis.addElement(p);
        }
    }

    /** Paieška pagal pavadinimą: contains + indexOf, rastas produktas pažymimas. */
    private void ieskoti() {
        String tekstas = paieskaLaukas.getText().trim();
        if (tekstas.isEmpty()) {
            return;
        }
        Produktas raktas = Produktas.paieskai(tekstas);
        if (katalogas.contains(raktas)) {
            int idx = katalogas.indexOf(raktas);
            katalogoSarasas.setSelectedIndex(idx);
            katalogoSarasas.ensureIndexIsVisible(idx);
        } else {
            pranesimas("Produkto „" + tekstas + "“ kataloge nėra");
        }
    }

    private void pridetiProdukta() {
        try {
            double kcal = Double.parseDouble(kcalLaukas.getText().trim().replace(',', '.'));
            Produktas p = new Produktas(pavadinimoLaukas.getText(), kcal);
            if (katalogas.add(p)) {
                atnaujintiKataloga();
                katalogoSarasas.setSelectedIndex(katalogas.indexOf(p));
                pavadinimoLaukas.setText("");
                kcalLaukas.setText("");
            } else {
                pranesimas("Toks produktas jau yra");
            }
        } catch (NumberFormatException ex) {
            pranesimas("Kalorijos turi būti skaičius");
        } catch (IllegalArgumentException ex) {
            pranesimas(ex.getMessage());
        }
    }

    private void pasalintiProdukta() {
        Produktas p = katalogoSarasas.getSelectedValue();
        if (p == null) {
            pranesimas("Pažymėkite produktą kataloge");
            return;
        }
        katalogas.remove(p);
        atnaujintiKataloga();
    }

    private void irasytiValgi() {
        Produktas p = katalogoSarasas.getSelectedValue();
        if (p == null) {
            pranesimas("Pažymėkite produktą kataloge");
            return;
        }
        try {
            double gramai = Double.parseDouble(gramuLaukas.getText().trim().replace(',', '.'));
            if (gramai <= 0) {
                throw new NumberFormatException();
            }
            double kcal = p.kalorijos(gramai);
            dienosKcal += kcal;
            dienosModelis.addElement(String.format("%s, %.0f g – %.0f kcal", p.getPavadinimas(), gramai, kcal));
            sumaLabel.setText(String.format("Šiandien: %.0f kcal", dienosKcal));
        } catch (NumberFormatException ex) {
            pranesimas("Kiekis turi būti teigiamas skaičius");
        }
    }

    private void naujaDiena() {
        dienosModelis.clear();
        dienosKcal = 0;
        sumaLabel.setText("Šiandien: 0 kcal");
    }

    private void pranesimas(String tekstas) {
        JOptionPane.showMessageDialog(this, tekstas);
    }
}

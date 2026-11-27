/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skiplistprojektas.calcalculator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Skip list realizacija. Elementai laikomi didėjimo tvarka keliuose lygiuose:
 * apatinis lygis L0 saugo visus elementus, aukštesni lygiai – vis mažesnę jų dalį.
 * Paieška, įterpimas ir šalinimas vidutiniškai trunka O(log n).
 *
 * @param <E> elementų tipas
 * @author EdasT
 */
public class SkipList<E extends Comparable<E>> implements SkipListADT<E> {

    /** Skip list mazgas: elementas ir rodyklės į kitus mazgus kiekviename lygyje. */
    private static class Node<E> {
        E element;
        Node<E>[] next;

        @SuppressWarnings("unchecked")
        Node(E element, int level) {
            this.element = element;
            this.next = new Node[level];
        }
    }

    /** Didžiausias galimas lygių skaičius. */
    private static final int MAX_LEVEL = 16;
    /** Tikimybė, kad mazgas pakils vienu lygiu aukščiau. */
    private static final double P = 0.5;

    private final Node<E> head = new Node<>(null, MAX_LEVEL);
    private int level = 1;
    private int size = 0;
    private final Random random = new Random();

    /** Sukuria tuščią skip list. */
    public SkipList() {
    }

    /**
     * Atsitiktinai parenka naujo mazgo lygių skaičių (1 su tikimybe 1/2,
     * 2 su tikimybe 1/4 ir t. t.).
     * @return lygių skaičius nuo 1 iki MAX_LEVEL
     */
    private int randomLevel() {
        int lvl = 1;
        while (lvl < MAX_LEVEL && random.nextDouble() < P) {
            lvl++;
        }
        return lvl;
    }

    /**
     * Ieško elemento, leidžiantis nuo aukščiausio lygio žemyn. O(log n).
     * @param e ieškomas elementas
     * @return true, jei elementas rastas
     * @throws IllegalArgumentException jei e yra null
     */
    @Override
    public boolean contains(E e) {
        if (e == null) {
            throw new IllegalArgumentException("Elementas negali būti null");
        }
        Node<E> x = head;
        for (int i = level - 1; i >= 0; i--) {
            while (x.next[i] != null && x.next[i].element.compareTo(e) < 0) {
                x = x.next[i];
            }
        }
        x = x.next[0];
        return x != null && x.element.compareTo(e) == 0;
    }

    /**
     * Įterpia elementą, išlaikant rikiavimo tvarką. Kiekviename lygyje
     * įsimenamas mazgas (update), po kurio įterpiamas naujas mazgas. O(log n).
     * @param e pridedamas elementas
     * @return true, jei pridėtas; false, jei toks elementas jau yra
     * @throws IllegalArgumentException jei e yra null
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) {
            throw new IllegalArgumentException("Elementas negali būti null");
        }
        Node<E>[] update = new Node[MAX_LEVEL];
        Node<E> x = head;
        for (int i = level - 1; i >= 0; i--) {
            while (x.next[i] != null && x.next[i].element.compareTo(e) < 0) {
                x = x.next[i];
            }
            update[i] = x;
        }
        x = x.next[0];
        if (x != null && x.element.compareTo(e) == 0) {
            return false;
        }

        int newLevel = randomLevel();
        if (newLevel > level) {
            for (int i = level; i < newLevel; i++) {
                update[i] = head;
            }
            level = newLevel;
        }

        Node<E> newNode = new Node<>(e, newLevel);
        for (int i = 0; i < newLevel; i++) {
            newNode.next[i] = update[i].next[i];
            update[i].next[i] = newNode;
        }
        size++;
        return true;
    }

    /**
     * Pašalina elementą: mazgas „apeinamas“ kiekviename jo lygyje,
     * tušti viršutiniai lygiai pašalinami. O(log n).
     * @param e šalinamas elementas
     * @return true, jei elementas buvo rastas ir pašalintas
     * @throws IllegalArgumentException jei e yra null
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(E e) {
        if (e == null) {
            throw new IllegalArgumentException("Elementas negali būti null");
        }
        Node<E>[] update = new Node[MAX_LEVEL];
        Node<E> x = head;
        for (int i = level - 1; i >= 0; i--) {
            while (x.next[i] != null && x.next[i].element.compareTo(e) < 0) {
                x = x.next[i];
            }
            update[i] = x;
        }
        x = x.next[0];
        if (x == null || x.element.compareTo(e) != 0) {
            return false;
        }

        for (int i = 0; i < x.next.length; i++) {
            update[i].next[i] = x.next[i];
        }
        while (level > 1 && head.next[level - 1] == null) {
            level--;
        }
        size--;
        return true;
    }

    /**
     * Randa elemento indeksą, eidamas apatiniu lygiu. O(n).
     * @param e ieškomas elementas
     * @return indeksas nuo 0 arba -1, jei elemento nėra (ar e yra null)
     */
    @Override
    public int indexOf(E e) {
        if (e == null) {
            return -1;
        }
        Node<E> x = head.next[0];
        int index = 0;
        while (x != null) {
            int cmp = x.element.compareTo(e);
            if (cmp == 0) {
                return index;
            }
            if (cmp > 0) {
                return -1;
            }
            x = x.next[0];
            index++;
        }
        return -1;
    }

    /**
     * Grąžina k-ąjį elementą, eidamas apatiniu lygiu. O(n).
     * @param k indeksas nuo 0
     * @return elementas
     * @throws IndexOutOfBoundsException jei k už ribų
     */
    @Override
    public E get(int k) {
        if (k < 0 || k >= size) {
            throw new IndexOutOfBoundsException("Indeksas " + k + ", dydis " + size);
        }
        Node<E> x = head.next[0];
        for (int i = 0; i < k; i++) {
            x = x.next[0];
        }
        return x.element;
    }

    /** @return elementų skaičius */
    @Override
    public int size() {
        return size;
    }

    /** @return true, jei sąrašas tuščias */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Pašalina visus elementus – nutraukia visas head rodykles. */
    @Override
    public void clear() {
        for (int i = 0; i < MAX_LEVEL; i++) {
            head.next[i] = null;
        }
        level = 1;
        size = 0;
    }

    /** @return elementai masyve didėjimo tvarka */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (E e : this) {
            array[i++] = e;
        }
        return array;
    }

    /** @return iteratorius, einantis apatiniu lygiu didėjimo tvarka */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head.next[0];

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E e = current.element;
                current = current.next[0];
                return e;
            }
        };
    }

    /** @return elementai formatu [a, b, c] */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (E e : this) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(e);
        }
        return sb.append("]").toString();
    }
}

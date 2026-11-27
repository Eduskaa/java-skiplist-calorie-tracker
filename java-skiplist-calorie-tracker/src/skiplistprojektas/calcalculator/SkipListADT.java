package skiplistprojektas.calcalculator;

/**
 * Skip list ADT – surikiuotas rinkinys su keliais lygiais.
 * @author EdasT
 */
public interface SkipListADT<E extends Comparable<E>> extends Iterable<E> {

    /** Prideda elementą; grąžina false, jei toks jau yra. */
    boolean add(E e);

    /** Grąžina true, jei elementas yra sąraše. */
    boolean contains(E e);

    /** Pašalina elementą; grąžina true, jei pašalintas. */
    boolean remove(E e);

    /** Grąžina elemento indeksą arba -1. */
    int indexOf(E e);

    /** Grąžina elementą pagal indeksą. */
    E get(int k);

    /** Grąžina elementų skaičių. */
    int size();

    /** Grąžina true, jei sąrašas tuščias. */
    boolean isEmpty();

    /** Pašalina visus elementus. */
    void clear();

    /** Grąžina elementus masyve didėjimo tvarka. */
    Object[] toArray();
}
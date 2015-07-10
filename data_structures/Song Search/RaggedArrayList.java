import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.print.attribute.standard.MediaSize.Other;

/*
 * RaggedArrayList.java
 * PUT YOUR NAME HERE
 * 
 * Initial starting code by Prof. Boothe Sep 2012
 *
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *  
 * It keeps the items in sorted order according to the comparator.
 * Duplicates are allowed.
 * New items are added after any equivalent items.
 */
public class RaggedArrayList<E> implements Iterable<E> {
    private static final int MINIMUM_SIZE = 4; // must be even so when split get
                                               // two equal pieces
    private int size;
    private Object[] topArray; // really is an array of L2Array, but compiler
                               // won't let me cast to that
    private int topNumUsed;
    private Comparator<E> comp;

    // create an empty list
    // always have at least 1 second level array even if empty, makes code
    // easier
    // (DONE)
    RaggedArrayList(Comparator<E> c) {
        size = 0;
        topArray = new Object[MINIMUM_SIZE]; // you can't create an array of a
                                             // generic type
        topArray[0] = new L2Array(MINIMUM_SIZE); // first 2nd level array
        topNumUsed = 1;
        comp = c;
    }

    // nested class for 2nd level arrays
    // (DONE)
    private class L2Array {
        public E[] items;
        public int numUsed;

        L2Array(int capacity) {
            items = (E[]) new Object[capacity]; // you can't create an array of
                                                // a generic type
            numUsed = 0;
        }
    }

    // total size (number of entries) in the entire data structure
    // (DONE)
    public int size() {
        return size;
    }

    // null out all references so garbage collector can grab them
    // but keep otherwise empty topArray and 1st L2Array
    // (DONE)
    public void clear() {
        size = 0;
        Arrays.fill(topArray, 1, topArray.length, null); // clear all but first
                                                         // l2 array
        topNumUsed = 1;
        L2Array l2Array = (L2Array) topArray[0];
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null); // clear out
                                                              // l2array
        l2Array.numUsed = 0;
    }

    // nested class for a list position
    // used only internally
    // 2 parts: top level index and second level index
    private class ListLoc {
        public int level1Index;
        public int level2Index;

        ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        // since only used internally, can trust it is comparing 2 ListLoc's
        public boolean equals(ListLoc other) {
            return (this.level1Index == other.level1Index && this.level2Index == other.level2Index);
        }

        // move ListLoc to next entry
        // when it moves past the very last entry it will be 1 index past the
        // last value in the used level 2 array
        // used internally to scan through the array for sublist
        // also used to implement the iterator
        public void moveToNext() {
            // TO DO
            L2Array moveFrom = (L2Array) topArray[level1Index];

            // if not at the end of the L2Array, increment to the next position
            if (level2Index < moveFrom.items.length - 1)
                level2Index++;

            // already determined in last spot of level 2 array
            // if not in the last topArray, increment to start of next topArray
            else if (level1Index < topArray.length - 1) {
                level1Index++;
                level2Index = 0;
            } else {
                level1Index = -1;
                level2Index = -1;
            }
        }
    }

    /**
     * find 1st matching entry returns ListLoc of 1st matching item or of 1st
     * item greater than the item if no match, this could be a used slot at the
     * end of a level 2 array
     */
    private ListLoc findFront(E item) {

        int level1Index = 0;
        int level2Index = 0;

        L2Array searchL2 = (L2Array) topArray[level1Index];
        E[] otherItems = searchL2.items;
        E other = otherItems[level2Index];

        // find correct Level 1 Array first, then find correct Level 2 Position
        // loop through L1Array until correct L2ArrayIndex found
        // (index where end of list reached, or other is greater than item

        while (other != null && comp.compare(item, other) > 0
                && level1Index < topNumUsed) {

            otherItems = searchL2.items;
            other = otherItems[level2Index];

            if (comp.compare(item, other) > 0)
                searchL2 = (L2Array) topArray[++level1Index];
        }

        if (level1Index > 0) {

            L2Array previous = (L2Array) topArray[level1Index - 1];
            E[] previousItems = previous.items;

            for (int i = 0; i < previous.numUsed; i++) {
                if (comp.compare(item, previousItems[i]) <= 0) {
                    level1Index--;
                    break;
                }
            }

        }
        // set "searchL2" to the final index of row that is not greater than
        // item, or null, and set otherItems to the contents of the L2Array

        try{
        
        searchL2 = (L2Array) topArray[level1Index];     
        otherItems = searchL2.items;
        other = otherItems[level2Index];

        // now, search L2Array at L1 index to find correct L2 index for
        // insertion

        while (other != null && comp.compare(item, other) > 0) {
            other = otherItems[++level2Index];
        }

        return new ListLoc(level1Index, level2Index);
    }
        catch (NullPointerException e)
        {
            searchL2 = (L2Array) topArray[level1Index - 1];     
            otherItems = searchL2.items;
            
            return new ListLoc(level1Index - 1, otherItems.length - 1);
        }
    }

    /**
     * find location after the last matching entry or if no match, it finds the
     * index of the next larger item this is the position to add a new entry
     * this could be an unused slot at the end of a level 2 array
     */
    private ListLoc findEnd(E item) {

        int level1Index = 0;
        int level2Index = 0;

        L2Array searchL2 = (L2Array) topArray[level1Index];
        E[] otherItems = searchL2.items;
        E other = otherItems[level2Index];

        // find correct Level 1 Array first, then find correct Level 2 Position
        // if first position of first L2Array is null, or greater than item,
        // insert at first position in list

        if (other == null || comp.compare(item, other) <= 0)
            return new ListLoc(0, 0);

        // otherwise, loop through L1Array until correct L2ArrayIndex found
        // (index where end of list reached, or other is greater than item

        while (other != null && comp.compare(item, other) > 0
                && level1Index < topNumUsed) {

            otherItems = searchL2.items;
            other = otherItems[level2Index];

            if (comp.compare(item, other) > 0)
                searchL2 = (L2Array) topArray[++level1Index];
        }

        if (level1Index > 0)
            level1Index--;

        // set "searchL2" to the final index of row that is not greater than
        // item, or null, and set otherItems to the contents of the L2Array

        searchL2 = (L2Array) topArray[level1Index];
        otherItems = searchL2.items;
        other = otherItems[level2Index];

        // now, search L2Array at L1 index to find correct L2 index for
        // insertion

        while (other != null && comp.compare(item, other) > 0) {
            other = otherItems[++level2Index];
        }

        return new ListLoc(level1Index, level2Index);
    }

    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     */
    boolean add(E item) {
        // TO DO
        ListLoc insertLoc = findEnd(item);

        int level1Index = insertLoc.level1Index;
        int level2Index = insertLoc.level2Index;
        int level1FinalIndex = topArray.length - 1;

        L2Array currentL2 = (L2Array) topArray[level1Index];
        int level2Length = currentL2.items.length;

        // if the L2Array index is null, add item.
        if (currentL2.items[level2Index] == null) {
            currentL2.items[level2Index] = item;
            currentL2.numUsed++;
            size++;
            
        } else {
            // if the index to insert at is not null, this copies the array down
            // and leaves the target index available for insertion.
            System.arraycopy(currentL2.items, level2Index, currentL2.items,
                    level2Index + 1, (level2Length - 1) - level2Index);
            currentL2.items[level2Index] = item;
            currentL2.numUsed++;
            size++;
        }

        int targetSizeOfL2Arrays = (int) Math.floor(Math.sqrt(size));

        if (currentL2.items[level2Length - 1] != null) {
            if (level2Length < targetSizeOfL2Arrays) { // Double L2 Array
                int newLength = level2Length * 2;
                L2Array newL2Array = new L2Array(newLength);
                System.arraycopy(currentL2.items, 0, newL2Array.items, 0,
                        level2Length);
                newL2Array.numUsed = currentL2.numUsed;
                topArray[level1Index] = newL2Array;

            } else { // Split L2 Array
                int middleIndex = level2Length / 2;
                L2Array level2ArrayFront = new L2Array(level2Length);
                L2Array level2ArrayEnd = new L2Array(level2Length);

                level2ArrayFront.numUsed = middleIndex;
                level2ArrayEnd.numUsed = middleIndex;

                System.arraycopy(currentL2.items, 0, level2ArrayFront.items, 0,
                        middleIndex);
                System.arraycopy(currentL2.items, middleIndex,
                        level2ArrayEnd.items, 0, middleIndex);

                if (level1Index >= topNumUsed) { // Checks to see if there are
                                                 // L2Arrays to shuffle down
                    topArray[level1Index] = level2ArrayFront; // No L2Arrays to
                                                              // shuffle, so
                                                              // just insert
                    topArray[level1Index + 1] = level2ArrayEnd;
                } else { // Shuffle l1 array down
                    System.arraycopy(topArray, level1Index, topArray,
                            level1Index + 1, (topNumUsed - level1Index));
                    topArray[level1Index] = level2ArrayFront; // No L2Arrays to
                                                              // shuffle, so
                                                              // just insert
                    topArray[level1Index + 1] = level2ArrayEnd;
                    topNumUsed++;
                }
            }

            if (topArray[level1FinalIndex] != null) { // double level 1 array
                int newL1ArrayLength = topArray.length * 2;
                topArray = Arrays.copyOf(topArray, newL1ArrayLength);
            }
        }

        return true;
    }

    /**
     * check if list contains a match
     */
    boolean contains(E item) {

        ListLoc loc = findFront(item);

        L2Array currentL2 = (L2Array) topArray[loc.level1Index];
        E[] items = currentL2.items;
        E search = items[loc.level2Index];
        try {
            if (comp.compare(item, search) == 0)
                return true;

            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     * 
     * @param a
     *            - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {

        L2Array currentL2 = (L2Array) topArray[0];
        E[] level2 = currentL2.items;
        int count = 0;

        for (int i = 0; i < topNumUsed; i++) {
            currentL2 = (L2Array) topArray[i];
            level2 = currentL2.items;
            for (int j = 0; j < level2.length; j++) {
                if (level2[j] != null) {
                    a[count] = level2[j];
                    count++;
                }
            }
        }

        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive the original list is
     * unaffected
     * 
     * @param fromElement
     * @param toElement
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        // TO DO

        RaggedArrayList<E> subList = new RaggedArrayList<E>(comp);

        ListLoc from = findFront(fromElement);
        ListLoc to = findEnd(toElement);

        int fromL2 = from.level2Index;

        L2Array currentL2 = (L2Array) topArray[from.level1Index];
        E[] level2 = currentL2.items;

        for (int i = from.level1Index; i <= to.level1Index; i++) {
            for (int j = fromL2; j < currentL2.numUsed; j++) {

                currentL2 = (L2Array) topArray[i];
                level2 = currentL2.items;

                if (i < to.level1Index && j != to.level2Index)
                    subList.add(level2[j]);
                if (i == to.level1Index && j < to.level2Index)
                    subList.add(level2[j]);
            }
            fromL2 = 0;
        }
        return subList;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Itr is the inner class that implements the iterator class I suggest you
     * build this using a ListLoc
     */
    private class Itr implements Iterator<E> {

        // start iterator at L2 -1, so that first return will be first item,
        // located at (0 , 0)
        
        private ListLoc location = new ListLoc(0, -1);

        Itr() {

        }

        /**
         * check is more items
         */
        public boolean hasNext() {

            L2Array lastArray = (L2Array) topArray[topNumUsed - 1];
            int lastL2 = lastArray.numUsed - 1;

            if (location.equals(new ListLoc(topNumUsed - 1, lastL2)))
                return false;

            return true;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list
         */
        public E next() {

            location.moveToNext();

            int currentL1 = location.level1Index;
            int currentL2 = location.level2Index;

            L2Array nextObjArray = (L2Array) topArray[currentL1];
            E[] level2 = nextObjArray.items;

            while (level2[currentL2] == null) {
                location.moveToNext();
                currentL1 = location.level1Index;
                currentL2 = location.level2Index;
                nextObjArray = (L2Array) topArray[currentL1];
                level2 = nextObjArray.items;
            }

            return level2[currentL2];
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * Main routine for testing the RaggedArrayList by itself. There is a
     * default test case of a-g. You can also specify arguments on the command
     * line that will be processed as a sequence of characters to insert into
     * the list.
     * 
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public static void main(String[] args) {
        System.out.println("testing routine for RaggedArrayList");
        System.out
                .println("usage: any command line arguments are added by character to the list");
        System.out
                .println("       if no arguments, then a default test case is used");

        // setup the input string
        String order = "";
        if (args.length == 0)
            order = "abcdefg"; // default test
        else
            for (int i = 0; i < args.length; i++)
                // concatenate all args
                order += args[i];

        // insert them character by character into the list
        System.out.println("insertion order: " + order);
        Comparator<String> comp = new StringCmp();
        ((CmpCnt) comp).resetCmpCnt(); // reset the counter inside the
                                       // comparator
        RaggedArrayList<String> ralist = new RaggedArrayList<String>(comp);
        for (int i = 0; i < order.length(); i++) {
            String s = order.substring(i, i + 1);
            ralist.add(s);
        }
        System.out
                .println("The number of comparison to build the RaggedArrayList = "
                        + ((CmpCnt) comp).getCmpCnt());

        System.out.println("TEST: after adds - data structure dump");
        ralist.dump();
        ralist.stats();

        System.out.println("TEST: contains(\"c\") ->" + ralist.contains("c"));
        System.out.println("TEST: contains(\"7\") ->" + ralist.contains("7"));

        System.out.println("TEST: toArray");
        String[] a = new String[ralist.size()];
        ralist.toArray(a);
        for (int i = 0; i < a.length; i++)
            System.out.print("[" + a[i] + "]");
        System.out.println();

        System.out.println("TEST: iterator");
        Iterator<String> itr = ralist.iterator();
        while (itr.hasNext())
            System.out.print("[" + itr.next() + "]");
        System.out.println();

        System.out.println("TEST: sublist(b,e)");
        RaggedArrayList<String> sublist = ralist.subList("b", "e");
        sublist.dump();
    }

    /**
     * string comparator with cmpCnt for testing
     * 
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public static class StringCmp implements Comparator<String>, CmpCnt {
        int cmpCnt;

        StringCmp() {
            cmpCnt = 0;
        }

        public int getCmpCnt() {
            return cmpCnt;
        }

        public void resetCmpCnt() {
            this.cmpCnt = 0;
        }

        public int compare(String s1, String s2) {
            cmpCnt++;
            return s1.compareTo(s2);
        }
    }

    /**
     * print out an organized display of the list intended for testing purposes
     * on small examples it looks nice for the test case where the objects are
     * characters
     * 
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public void dump() {
        System.out.println("DUMP: Display of the raggedArrayList");
        for (int i1 = 0; i1 < topArray.length; i1++) {
            L2Array l2array = (L2Array) topArray[i1];
            System.out.print("[" + i1 + "] -> ");
            if (l2array == null)
                System.out.println("null");
            else {
                for (int i2 = 0; i2 < l2array.items.length; i2++) {
                    E item = l2array.items[i2];
                    if (item == null)
                        System.out.print("[ ]");
                    else
                        System.out.print("[" + item + "]");
                }
                System.out.println("  (" + l2array.numUsed + " of "
                        + l2array.items.length + ") used");
            }
        }
    }

    /**
     * calculate and display statistics
     * 
     * It use a comparator that implements the given CmpCnt interface. It then
     * runs through the list searching for every item and calculating search
     * statistics.
     * 
     * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
     */
    public void stats() {
        System.out.println("STATS:");
        System.out.println("list size N = " + size);

        // top level array
        System.out.println("Top level array " + topNumUsed + " of "
                + topArray.length + " used.");

        // level 2 arrays
        int minL2size = Integer.MAX_VALUE, maxL2size = 0;
        for (int i1 = 0; i1 < topNumUsed; i1++) {
            L2Array l2Array = (L2Array) topArray[i1];
            minL2size = Math.min(minL2size, l2Array.numUsed);
            maxL2size = Math.max(maxL2size, l2Array.numUsed);
        }
        System.out.println("level 2 array sizes: min = " + minL2size
                + " used, avg = " + (double) size / topNumUsed
                + " used, max = " + maxL2size + " used");

        // search stats, search for every item
        int totalCmps = 0, minCmps = Integer.MAX_VALUE, maxCmps = 0;
        Iterator<E> itr = iterator();
        while (itr.hasNext()) {
            E obj = itr.next();
            ((CmpCnt) comp).resetCmpCnt();
            if (!contains(obj))
                System.err
                        .println("Did not expect an unsuccesful search in stats");
            int cnt = ((CmpCnt) comp).getCmpCnt();
            totalCmps += cnt;
            if (cnt > maxCmps)
                maxCmps = cnt;
            if (cnt < minCmps)
                minCmps = cnt;
        }
        System.out.println("Successful search: min cmps = " + minCmps
                + " avg cmps = " + (double) totalCmps / size + " max cmps = "
                + maxCmps);
    }
}

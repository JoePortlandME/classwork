import java.io.*;
import java.util.*;

public class SearchByArtistPrefix {

    private Song[] songs; // keep a direct reference to the song array

    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * 
     * @param string
     * @return
     */
    public Song[] search(String artistPrefix) {

        /*
         * create a "dummy" song for finding matches, then binarySearch using
         * Artist Comparator object
         */
        Song search = new Song(artistPrefix, null, null);
        Comparator<Song> comp = new Song.SongArtistComparator();

        // reset comparator prior to searching
        ((CmpCnt) comp).resetCmpCnt();

        // search array using comparator
        int index = Arrays.binarySearch(songs, search, comp);

        // returns number of compare operations used
        int compares = ((CmpCnt) comp).getCmpCnt();
        System.out.println("Comparison used " + compares
                + " times during binary search.");
        /*
         * if index not found, provide positive-valued index where item would be
         * located, which can be used to search from
         */

        if (index < 0)
            index = -index;

        return findAllMatches(index, artistPrefix);
    }

    /**
     * this method will return all of the matches to the prefix, given the index
     * of the first match found using binarySearch
     */

    private Song[] findAllMatches(int index, String prefix) {

        prefix = prefix.toLowerCase(); // prevents no match due to case
        ArrayList<Song> results = new ArrayList<Song>();

        // if index indicates the search would belong after the last item
        // in the songs array, returns an empty list of matches

        if (index == songs.length + 1)
            return results.toArray(new Song[0]);

        // if not beginning of list, move index until current index no longer
        // matches artist prefix

        while (index > 0
                && songs[index - 1].getArtist().toLowerCase()
                        .startsWith(prefix)) {
            index--;
        }

        // index now references first matching item, increment from first item
        // until end of list is reached, or until artist name no longer matches
        // prefix

        while (index < songs.length
                && songs[index].getArtist().toLowerCase().startsWith(prefix)) {
            results.add(songs[index]);
            index++;
        }

        // convert the ArrayList of matches to an array and return

        return results.toArray(new Song[results.size()]);
    }

    // testing routine
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.err.println("usage: prog songfile artist");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        System.out.println("searching for: " + args[1]);
        Song[] byArtistResult = sbap.search(args[1]);

        // to do: show first 10 songs
        System.out.println("Total matches: " + byArtistResult.length);
        System.out.println("First ten matches: ");
        System.out.println();
        for (int i = 0; i < 10 && i < byArtistResult.length; i++) {

            System.out.println(byArtistResult[i].toString());
        }

        System.err.println("exiting normally");
    }
}

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SearchByTitlePrefix {
    private Song[] song;
    private RaggedArrayList<Song> songs; // keep a direct reference to title
                                         // RaggedArrayList
    Comparator<Song> comp;        

    public SearchByTitlePrefix(SongCollection sc) {
        song = sc.getAllSongs();
        comp = new Song.SongTitleComparator();        
        songs = new RaggedArrayList<Song>(comp);
        
        for (int i = 0; i < song.length; i++) {
            songs.add(song[i]);
        }
        
       
        int compares = ((CmpCnt) comp).getCmpCnt();
        System.out.println("Comparison used " + compares
                + " times during search.");
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * 
     * @param string
     * @return
     */
    public Song[] search(String titlePrefix) {

        /*
         * create a "dummy" song for finding matches, then binarySearch using
         * Artist Comparator object
         */
        Song search = new Song(null, titlePrefix, null);
        
        char[] charArray= titlePrefix.toCharArray();
        char lastLetter= charArray[charArray.length-1];
        lastLetter++;
        charArray[charArray.length-1]= lastLetter;
        String newTitleSearch= new String(charArray);
        
        Song stop = new Song(null, newTitleSearch, null);        
        RaggedArrayList<Song> result = new RaggedArrayList<Song>(comp);

        // reset comparator prior to searching
        

        // search array using comparator
        result = songs.subList(search, stop);

        // returns number of compare operations used
        int compares = ((CmpCnt) comp).getCmpCnt();
        System.out.println("Comparison used " + compares
                + " times during search.");
        
        Song[] a = new Song[result.size()];

        return result.toArray(a);
    }

    /**
     * this method will return all of the matches to the prefix, given the index
     * of the first match found using binarySearch
     */

    // testing routine
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.err.println("usage: prog songfile artist");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);

        System.out.println("searching for: " + args[1]);
        Song[] byTitleResult = sbtp.search(args[1]);

        // to do: show first 10 songs
        System.out.println("Total matches: " + byTitleResult.length);
        System.out.println("First ten matches: ");
        System.out.println();
        for (int i = 0; i < 10 && i < byTitleResult.length; i++) {

            System.out.println(byTitleResult[i].toString());
        }

        System.err.println("exiting normally");
    }
}

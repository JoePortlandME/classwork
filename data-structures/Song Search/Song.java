import java.io.FileNotFoundException;
import java.util.Comparator;

public class Song implements Comparable<Song> {

    private String artist, title, lyrics;
    public static int counter = 0;

    public Song(String artist, String title, String lyrics) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String toString() {
        return (artist + ", \"" + title + "\"");
    }

    /*
     * Compares the artist names and if the values are not equal, then compares
     * the title names to sort by artist, then title
     */

    public int compareTo(Song other) {
        counter++;
        String one = this.getArtist().toUpperCase();
        String two = other.getArtist().toUpperCase();
        int result = one.compareTo(two);

        if (result != 0)
            return result;
        else {
            String three = this.getTitle().toUpperCase();
            String four = other.getTitle().toUpperCase();
            return three.compareTo(four);
        }
    }

    /*
     * Comparator to be used for comparing title names. This will be used in
     * the class SearchByTitlePrefix to compare title names to the given
     */

    public static class SongTitleComparator implements Comparator<Song>, CmpCnt {
        int cmpCnt;

        SongTitleComparator() {
            cmpCnt = 0;
        }

        public int getCmpCnt() {
            return cmpCnt;
        }

        public void resetCmpCnt() {
            this.cmpCnt = 0;
        }

        public int compare(Song s1, Song s2) {
            cmpCnt++;
            String songTitle1 = s1.getTitle().toLowerCase();
            String songTitle2 = s2.getTitle().toLowerCase();

            return songTitle1.compareTo(songTitle2);
        }
    }
    
    /*
     * Comparator to be used for comparing artist names. This will be used in
     * the class SearchByArtistPrefix to compare artist names to the given
     */

    public static class SongArtistComparator implements Comparator<Song>,
            CmpCnt {
        int cmpCnt;

        SongArtistComparator() {
            cmpCnt = 0;
        }

        public int getCmpCnt() {
            return cmpCnt;
        }

        public void resetCmpCnt() {
            this.cmpCnt = 0;
        }

        public int compare(Song s1, Song s2) {
            cmpCnt++;
            String songName1 = s1.getArtist().toLowerCase();
            String songName2 = s2.getArtist().toLowerCase();

            return songName1.compareTo(songName2);
        }
    }

    /*
     * Tests by calling artist, title, and lyrics
     */

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        Song[] list = sc.getAllSongs();
        System.out.println("First Song:");
        System.out.println("Artist: " + list[0].getArtist());
        System.out.println("Title: " + list[0].getTitle());
        System.out.println("Lyrics: " + list[0].getLyrics());
    }

}

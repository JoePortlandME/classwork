import java.io.*;
import java.util.*;

public class SongCollection {

    private Song[] songs; // array of all the songs
    private int songCount = 0; // keeps count of songs

    // read in the song file and build the songs array
    public SongCollection(String filename) throws FileNotFoundException {
        // read in the song file and build the songs array
        readFile(filename);
        // sort the songs array
        sortSongs();
        // print statistics:
        // the number of songs
        // the number of comparisons used to sort it
        printStats();
    }

    // return the songs array
    // this is used as the data source for building other data structures
    public Song[] getAllSongs() {
        return songs;
    }

    private void readFile(String fileName) throws FileNotFoundException {

        // scans input using quotations as a delimiter to parse file

        Scanner input = new Scanner(new File(fileName)).useDelimiter("\"");
        List<Song> songList = new ArrayList<Song>();

        String artist, title, lyrics;
        Song nextSong;

        while (input.hasNext()) {
            input.next(); // skips "Artist:"
            artist = input.next(); // reads Artist
            input.next(); // skips "Title:"
            title = input.next(); // reads Title
            input.next(); // skips "Lyrics:"
            lyrics = input.next(); // reads Lyrics

            // creates new song, adds to list, and increments counter
            nextSong = new Song(artist, title, lyrics);
            songList.add(nextSong);
            songCount++;
        }
        songs = songList.toArray(new Song[songCount - 1]);
    }

    // sorts songs using Arrays.sort()

    private void sortSongs() {
        Arrays.sort(songs);
    }

    private void printStats() {
        System.out.println("compareTo was used " + Song.counter + " times");
        System.out.println();
        System.out.println("Total songs = " + songCount + ", first ten songs:");
        System.out.println();
    }

    // testing method
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        Song[] list = sc.getAllSongs();
        // show First 10 songs
        for (int i = 0; i < 10; i++) {
            System.out.println(list[i].toString());
        }
    }

}

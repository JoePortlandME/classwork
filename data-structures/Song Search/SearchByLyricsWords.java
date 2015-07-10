import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class SearchByLyricsWords {

	private Song[] song; // keep a direct reference to the song array
	private TreeMap<String, TreeSet<Song>> songs = new TreeMap<String, TreeSet<Song>>();
	private static int counter = 0;
	private static int songCount;
	private static int keyCount;
	private static int refCount;
	private String[] excluded = { "the", "of", "and", "a", "to", "in", "is", "you",
			"that", "it", "he", "for", "was", "on", "are", "as", "with",
			"his", "they", "at", "be", "this", "from", "I", "have", "or",
			"by", "one", "had", "not", "but", "what", "all", "were",
			"when", "we", "there", "can", "an", "your", "which", "their",
			"if", "do", "will", "each", "how", "them", "then", "she",
			"many", "some", "so", "these", "would", "into", "has", "more",
			"her", "two", "him", "see", "could", "no", "make", "than",
			"been", "its", "now", "my", "made", "did", "get", "our", "me",
			"too", null };

	public SearchByLyricsWords(SongCollection sc) {
		song = sc.getAllSongs();
		Song currentSong;

		// adds all lyrics to songs map
		for (int i = 0; i < song.length; i++) {
			currentSong = song[i];
			addLyrics(currentSong);
		}

		songCount = song.length;
		keyCount = getSongs().size();
		refCount = refNumber(getSongs());
	}

	// calculates number of references for refCount

	private int refNumber(TreeMap<String, TreeSet<Song>> songs2) {
		int count = 0;
		Set<Entry<String, TreeSet<Song>>> tempSet = getSongs().entrySet();
		TreeSet<Song> tempTree = new TreeSet<Song>();

		for (Entry<String, TreeSet<Song>> f : tempSet) {
			tempTree = f.getValue();
			count += tempTree.size();
		}
		return count;
	}

	// adds the Song to the songs Map
	private void addLyrics(Song currentSong) {
		String lyrics = currentSong.getLyrics();
		String[] words = lyrics.split("[^a-zA-Z]+");
		String word;

		// if not empty or last word, convert to lower case
		for (int i = 0; i < words.length; i++) {
			word = words[i].toLowerCase();
			TreeSet<Song> tempSongs = new TreeSet<Song>();

			// if word is not an excluded word
			if (!excludedWords(word)) {
				// if first song, add and increment
				if (getSongs().isEmpty()) {
					tempSongs.add(currentSong);
					getSongs().put(word, tempSongs);
					counter++;

					// else add reference
				} else {

					// if word in list, add song if not already there
					if (getSongs().containsKey(word)) {
						tempSongs = getSongs().get(word);
						if (!tempSongs.contains(currentSong)) {
							counter++;
							tempSongs.add(currentSong);
							getSongs().put(word, tempSongs);
						}
						
						// if word not in list, add
					} else {
						tempSongs.add(currentSong);
						getSongs().put(word, tempSongs);
						counter++;
					}
				}
			}
		}
	}

	// compares to global list of excluded words
	
	private boolean excludedWords(String word) {

		if (word.length() <= 1)
			return true;
		else {
			for (int i = 0; i < excluded.length; i++)
				if (word.equals(excluded[i]))
					return true;
		}

		return false;
	}

	/**
	 * Lyrics are split to array, then have excluded words removed
	 * Performs intersection for multiple search words, then 
	 * converts to array for return of results
	 */
	public Song[] search(String lyricsWords) {

		/*
		 * 
		 */
		String[] lyrics = lyricsWords.split("[^a-zA-Z]+");
		TreeSet<Song> results = new TreeSet<Song>();

		removeExcluded(lyrics);

		for (int i = 0; i < lyrics.length; i++) {
			if (getSongs().containsKey(lyrics[i]))
				break;
			else if (i == lyrics.length - 1)
				return null;
		}

		if (lyrics.length > 0) {
			int j = 0;

			while (j < lyrics.length && lyrics[j] != null) {

				while (excludedWords(lyrics[j]))
					j++;

				if (!getSongs().containsKey(lyrics[j]))
					return null;

				if (results.isEmpty())
					results.addAll(getSongs().get(lyrics[j]));
				else
					results.retainAll(getSongs().get(lyrics[j++]));

			}

			Song[] resultsArray = new Song[results.size()];

			return results.toArray(resultsArray);
		} else
			return null;

	}
	
	// removes words from array that are on excluded list

	private void removeExcluded(String[] lyrics) {
		List<String> newList = new ArrayList<String>();

		for (int i = 0; i < lyrics.length; i++) {
			if (!excludedWords(lyrics[i]))
				newList.add(lyrics[i]);
		}
		lyrics = newList.toArray(lyrics);
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
		SearchByLyricsWords sblw = new SearchByLyricsWords(sc);

		System.out.println("searching for: " + args[1]);
		Song[] byLyricsWords = sblw.search(args[1]);

		System.out.println("Total number of indexing terms: " + counter);
		System.out.println("Total number of indexes per song: "
				+ (counter / songCount));
		System.out.println("Total number of keys in the map: " + keyCount);
		System.out.println("Total number of song references: " + refCount);
		System.out.println("Average number of song references per key: "
				+ (refCount / keyCount));

		System.out.println();

		// to do: show first 10 songs
		if (byLyricsWords == null) {
			System.out.println("Total matches: 0");
			System.out.println("First ten matches: ");
			System.out.println();
			System.out.println("No matches found for search terms");
		} else {
			System.out.println("Total matches: " + byLyricsWords.length);
			System.out.println("First ten matches: ");
			System.out.println();
			for (int i = 0; i < 10 && i < byLyricsWords.length; i++) {

				System.out.println(byLyricsWords[i].toString());
			}

			System.err.println("exiting normally");
		}
	}

	public TreeMap<String, TreeSet<Song>> getSongs() {
		return songs;
	}

	public void setSongs(TreeMap<String, TreeSet<Song>> songs) {
		this.songs = songs;
	}
}

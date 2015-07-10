import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchByLyricsPhrase {

	private Song[] song; // keep a direct reference to the song array
	private TreeMap<String, TreeSet<Song>> songs = new TreeMap<String, TreeSet<Song>>();
	private String[] lyrics;
	private TreeMap<Integer, ArrayList<Song>> matchLength = new TreeMap<Integer, ArrayList<Song>>();
	private static int counter = 0;
	private static int songCount;
	private static int keyCount;
	private static int refCount;
	

	public SearchByLyricsPhrase(SongCollection sc) {
		song = sc.getAllSongs();
		Song currentSong;

		// adds all lyrics to songs map
		for (int i = 0; i < song.length; i++) {
			currentSong = song[i];
			addLyrics(currentSong);
		}

		songCount = song.length;
		keyCount = songs.size();
		refCount = refNumber(songs);
	}

	// calculates number of references for refCount

	private int refNumber(TreeMap<String, TreeSet<Song>> songs) {
		int count = 0;
		Set<Entry<String, TreeSet<Song>>> tempSet = songs.entrySet();
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

			// if first song, add and increment
			if (songs.isEmpty()) {
				tempSongs.add(currentSong);
				songs.put(word, tempSongs);
				counter++;

				// else add reference
			} else {

				// if word in list, add song if not already there
				if (songs.containsKey(word)) {
					tempSongs = songs.get(word);
					if (!tempSongs.contains(currentSong)) {
						counter++;
						tempSongs.add(currentSong);
						songs.put(word, tempSongs);
					}

					// if word not in list, add
				} else {
					tempSongs.add(currentSong);
					songs.put(word, tempSongs);
					counter++;
				}
			}

		}
	}

	/**
	 * Lyrics are split to array, then intersection is used
	 * to ensure that songs they contain all words, then
	 * passes to a method to calculate and sort by rank,
	 *  then converts to array for return of results
	 *  
	 *  ranking also eliminates any songs that do not contain
	 *  the phrase, although they contain the search words,
	 *  since not guaranteed by inclusion alone
	 */
	public Song[] search(String lyricsPhrase) {

		/*
		 * 
		 */

		String[] lyrics = lyricsPhrase.split("[^a-zA-Z]+");
		this.lyrics = lyrics;

		TreeSet<Song> results = new TreeSet<Song>();

		for (int i = 0; i < lyrics.length; i++) {
			if (songs.containsKey(lyrics[i]))
				break;
			else if (i == lyrics.length - 1)
				return null;
		}

		if (lyrics.length > 0) {
			int j = 0;

			while (j < lyrics.length && lyrics[j] != null) {

				if (!songs.containsKey(lyrics[j]))
					return null;
				if (results.isEmpty())
					results.addAll(songs.get(lyrics[j]));
				else
					results.retainAll(songs.get(lyrics[j++]));
			}

		//	Song[] resultsArray = new Song[results.size()];

			rankResults(results);
			
			Collection<ArrayList<Song>> rankedResults = matchLength.values();
			
			ArrayList<Song> resultsArray = new ArrayList<Song>();
			int i = 0;
			for (ArrayList<Song> s : rankedResults)
				for (Song search : s){
					resultsArray.add(search);
		}
			Song[] result = new Song[resultsArray.size()];

			for (Song insert : resultsArray)
				result[i++] = insert;
			return result;
		} else
			return null;

	}

	/**
	 * input : All songs that contain all terms
	 * 
	 * Searches songs for matches to phrase and calculate
	 * rankings, ensuring the lowest rank possible for each
	 * song, then sorts songs based on their ranking
	 **
	 */
	
	private void rankResults(TreeSet<Song> results) {

		Object[] songList = results.toArray();
		String lyrics = "";
		int subLength = Integer.MAX_VALUE;
		int tempLength = 0;
		ArrayList<Song> rankedResults = new ArrayList<Song>();

		for (Object s : songList) {
			lyrics = " " + ((Song) s).getLyrics().toLowerCase() + " ";
			String regex = "[^a-zA-Z]" + this.lyrics[0] + "[^a-zA-Z]";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(lyrics);
			matcher.find();
			while (!matcher.hitEnd()) {
				int start = matcher.start() + 1;
				int end = lengthFrom(lyrics, start, 1);
				if (end == Integer.MAX_VALUE)
					break;
				tempLength = end - start;
				if (tempLength < subLength && tempLength != 0)
					subLength = tempLength;
				matcher.find();
			}
			if (subLength < lyrics.length()) {
				rankedResults = new ArrayList<Song>();
				if (matchLength.containsKey(subLength))
					rankedResults = matchLength.get(subLength);
				rankedResults.add((Song) s);
				matchLength.put(subLength, rankedResults);
				
			}
			subLength = Integer.MAX_VALUE;
		}

	}
	
	/**
	 *Passes first word match to be searched for
	 *subsequent words, returning index of the end of
	 *the last word when found
	 */
	private int lengthFrom(String search, int start, int wordNumber) {

		String song = search;
		int from = start;
		int word = wordNumber;

		if (wordNumber < lyrics.length) {

			String regex = "[^a-zA-Z]" + this.lyrics[wordNumber] + "[^a-zA-Z]";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(search);
			matcher.find();
			while (matcher != null && !matcher.hitEnd()
					&& matcher.start() + 1 < from)
				matcher.find();

			if (matcher.hitEnd())
				return Integer.MAX_VALUE;

			if (wordNumber < lyrics.length - 1)
				return lengthFrom(song, matcher.end() - 1, word + 1);
			else
				return matcher.end() - 1;

		}
		return 0;
	}

	/**
	 * this method will return all of the matches to the prefix, given the index
	 * of the first match found using binarySearch
	 */

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 2) {
			System.err.println("usage: prog songfile artist");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByLyricsPhrase sblp = new SearchByLyricsPhrase(sc);

		System.out.println("searching for: " + args[1]);
		Song[] byLyricsPhrase = sblp.search(args[1]);
		Set<Integer> keys = sblp.matchLength.keySet();
		int[] countByLyrics = new int[byLyricsPhrase.length];
		int count = 0;
		ArrayList<Song> list = new ArrayList<Song>();
		
		for (int search : keys){
			list = sblp.matchLength.get(search);
			for (Song s : list){
				countByLyrics[count++] = search;
			}
		}
		
		System.out.println("Total number of indexing terms: " + counter);
		System.out.println("Total number of indexes per song: "
				+ (counter / songCount));
		System.out.println("Total number of keys in the map: " + keyCount);
		System.out.println("Total number of song references: " + refCount);
		System.out.println("Average number of song references per key: "
				+ (refCount / keyCount));

		System.out.println();

		// to do: show first 10 songs
		if (!(byLyricsPhrase == null)) {
			System.out.println("Total matches: " + byLyricsPhrase.length);
			System.out.println("First ten matches: ");
			System.out.println();
			for (int i = 0; i < 10 && i < byLyricsPhrase.length; i++) {
				System.out.print(countByLyrics[i] + "  ");
				System.out.println(byLyricsPhrase[i].toString());
			}

			System.err.println("exiting normally");
		}
	}
}

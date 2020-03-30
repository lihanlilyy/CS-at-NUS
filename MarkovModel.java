import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.LinkedHashMap;

import jdk.jfr.Frequency;

/**
 * This is the main class for your Markov Model.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;
	private HashMap<String, int[]> kgramTable = new HashMap<>();
	int order;
	long seed;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		this.order = order;
		this.seed = seed;
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		int[] charFreq;
		for (int i = 0; i < text.length() - order; i++){
			String key = text.substring(i, i + order);
			if(kgramTable.containsKey(key)) {
				charFreq = kgramTable.get(key);
			} else {
				charFreq = new int[255];
			}
			//increase single count
			charFreq[text.charAt(i+order)]++;
			//increase overall count
			charFreq[128]++;
			kgramTable.put(key, charFreq);
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		int[] value = kgramTable.get(kgram);
		if (value == null){
			return 0;
		} else {
			return value[128];
		}
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		int[] value = kgramTable.get(kgram);
		if (value == null) {
			return 0;
		} else {
			return value[(int) c];
		}
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		int[] value = kgramTable.get(kgram);
		LinkedHashMap<Character, Integer> charsAll = new LinkedHashMap<>();
		int max = 0;
		if (value == null) {
			return NOCHARACTER;
		} else {
			for (int i=0; i<128; i++) {
				if (value[i] != 0) {
					charsAll.put((char) i, value[i] + max);
					max += value[i];
				}
			}
		}
		int rand = generator.nextInt(max);
		for (char key : charsAll.keySet()) {
			if (rand < charsAll.get(key)) {
				return  key;
			}
		}
		return NOCHARACTER;
	}
}

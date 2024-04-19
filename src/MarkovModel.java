import java.util.Map;
import java.util.Random;
import java.util.HashMap;
/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	private int order;

	private HashMap<String, HashMap<Character, Integer>> markovTable;
	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed  the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		markovTable = new HashMap<>();
		this.order = order;
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		for (int i = 0; i <= text.length() - order; i++) {
			String s = text.substring(i, i + order);
			Character c = (i + order == text.length()) ? NOCHARACTER : text.charAt(i + order);
			if (markovTable.containsKey(s)) {
				HashMap<Character, Integer> subm = markovTable.get(s);
				if (subm.containsKey(c)) {
					Integer num = subm.get(c);
					subm.put(c, num + 1);
				} else {
					subm.put(c, 1);
				}
			} else {
				markovTable.put(s, new HashMap<>());
				HashMap<Character, Integer> subm = markovTable.get(s);
				subm.put(c, 1);
			}
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		int sum = 0;
		if (kgram.length() != order || !markovTable.containsKey(kgram)) {
			return sum;
		}
		HashMap<Character, Integer> subm = markovTable.get(kgram);
		for (Integer i : subm.values()) {
			sum += i;
		}
		return sum;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != order || !markovTable.containsKey(kgram)) {
			return 0;
		}
		HashMap<Character, Integer> subm = markovTable.get(kgram);
		if (!subm.containsKey(c)) {
			return 0;
		}
		return subm.get(c);
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		if (kgram.length() != order || !markovTable.containsKey(kgram)) {
			return NOCHARACTER;
		}

		int total = this.getFrequency(kgram);
		int rand = generator.nextInt(total);
		int intervalstart = 0;
		for (int i = 0; i < 256; i++) {
			int freq = this.getFrequency(kgram, (char) i);
			if (freq != 0) {
				if (freq + intervalstart >= rand) {
					return (char) i;
				}
				intervalstart += freq;
			}
		}

		return NOCHARACTER;
	}
}
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.HashMap;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Logan Tran
 */
public class Autocorrect {

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */

    // Constants
    private final static int ALPHABET_SIZE = 26;

    // Instance variables
    private final String[] words;
    private final int threshold;
    private final ArrayList<Integer>[][] twoGram;
    private final TST dict;
    private final HashMap<String, Integer> frequencyMap;


    // Constructor
    public Autocorrect(String[] words, int threshold) {
        this.words = words;
        this.threshold = threshold;
        this.frequencyMap = new HashMap<>();
        this.dict = new TST();

        // Initialize two-grams
        this.twoGram = new ArrayList[ALPHABET_SIZE][ALPHABET_SIZE];

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            for (int j = 0; j < ALPHABET_SIZE; j++) {
                twoGram[i][j] = new ArrayList<>();
            }
        }

        // Loop through words
        for (int k = 0; k < words.length; k++){
            String word = words[k];
            dict.insert(word);
            // Add word to each two gram it has
            for(int l = 0; l < word.length() - 1; l++) {
                int i = word.charAt(l) - 'a';
                int j = word.charAt(l + 1) - 'a';
                // Make sure characters are in the alphabet
                if(j  < 0 || j >= ALPHABET_SIZE) {
                    l++;
                }
                else {
                    twoGram[i][j].add(k);
                }
            }

        }
    }

    // Command line prompting
    public void prompt() {
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.print("Enter a word: ");
            String typed = s.nextLine();
            // Ensure word is in the dictionary
            if(dict.search(typed)) {
            }
            else {
                // Print out possible correct words
                System.out.println("you typed " + typed);
                String[] options = runTest(typed);
                if (options.length == 0) {
                    System.out.println("No suggestions found.");
                }
                // Prompt user for chosen word to record frequency
                else {
                    int maxSuggestions = Math.min(3, options.length);
                    System.out.println("Did you mean...");
                    for(int i = 0; i < maxSuggestions; i++) {
                        System.out.println(options[i]);
                    }
                    // Increase frequency to chosen word
                    System.out.print("Choose (1-" + maxSuggestions + ") or 0 to skip: ");
                    int choice = Integer.parseInt(s.nextLine());
                    if (choice >= 1 && choice <= maxSuggestions) {
                        String chosenWord = options[choice - 1];
                        frequencyMap.put(chosenWord, frequencyMap.getOrDefault(chosenWord, 0) + 1);
                        System.out.println("Selected: " + chosenWord);
                    } else {
                        System.out.println("No selection made.");
                    }
                }
            }
            System.out.println("~~~~~~~~");
        }
    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {

        // Temp arraylist for words in threshold
        ArrayList<CandidateWord> possibleWords = new ArrayList<>();

        // Associative array to prevent multiple look-ups for the same word
        boolean[] seen = new boolean[words.length];

        // Find words in Threshold out of two-grams
        for(int k = 0; k < typed.length() - 1; k++) {
            int i = typed.charAt(k) - 'a';
            int j = typed.charAt(k + 1) - 'a';
            if(j  < 0 || j >= ALPHABET_SIZE) {
                k++;
            }
            else {
                for(int index: twoGram[i][j]) {
                    if(!seen[index]) {
                        seen[index] = true;
                        int distance = ed(typed, words[index]);
                        if(distance <= threshold) {
                            // Add word to possible words with
                            int freq = frequencyMap.getOrDefault(words[index], 0);
                            possibleWords.add(new CandidateWord(distance, words[index], freq));
                        }
                    }
                }
            }
        }

        // Sort words in order of alphabet, frequency and edit distance
        possibleWords.sort(
                Comparator.comparingInt(CandidateWord::geted)
                        .thenComparing((a, b) -> Integer.compare(b.getFrequency(), a.getFrequency()))
                        .thenComparing(CandidateWord::getWord));

        // Convert to string
        String result[] = new String[possibleWords.size()];
        for(int i = 0; i < possibleWords.size(); i++) {
            result[i] = possibleWords.get(i).getWord();
        }

        return result;
    }

    // Function to return edit distance
    public int ed(String typed, String target) {
        int m = typed.length();
        int n = target.length();

        // Initialize array for tabulation
        int[][] tab = new int[m + 1][n + 1];

        // Pad array
        for(int i = 1; i < m + 1; i++) {
            tab[i][0] = i;
        }
        for(int i = 1; i < n + 1; i++) {
            tab[0][i] = i;
        }

        // Use tabulation to find edit distance
        for(int i = 1; i < m + 1; i++) {
            for(int j = 1; j < n + 1; j++) {
                if(typed.charAt(i - 1) == target.charAt(j - 1)) {
                    tab[i][j] = tab[i - 1][j - 1];
                }
                else {
                    tab[i][j] = 1 + Math.min(tab[i - 1][j - 1], Math.min(tab[i][j - 1], tab [i - 1][j]));
                }
            }
        }

        return tab[m][n];
    }


    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader testReader = new BufferedReader(new FileReader("dictionaries/large.txt"));
        AutocorrectTester tester = new AutocorrectTester();
        String[] dictionary = tester.loadWords(testReader);
        Autocorrect a = new Autocorrect(dictionary,3);
        a.prompt();
    }
}
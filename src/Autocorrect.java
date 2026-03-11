import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

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

    private final static int ALPHABET_SIZE = 26;

    private String[] words;
    private int threshold;
    private ArrayList<Integer>[][] twoGram;


    public Autocorrect(String[] words, int threshold) {
        this.words = words;
        this.threshold = threshold;

        this.twoGram = new ArrayList[ALPHABET_SIZE][ALPHABET_SIZE];

        // Initialize two-grams
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            for (int j = 0; j < ALPHABET_SIZE; j++) {
                twoGram[i][j] = new ArrayList<>();
            }
        }

        // Add word to two-grams it has
        for (int k = 0; k < words.length; k++){
            String word = words[k];
            for(int l = 0; l < word.length() - 1; l++) {

                int i = word.charAt(l) - 'a';
                int j = word.charAt(l + 1) - 'a';

                twoGram[i][j].add(k);

            }

        }




    }

    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {

        // Temp arrays
        ArrayList<CandidateWord> possibleWords = new ArrayList<>();

        // Find words in Threshold out of two-grams
        boolean[] seen = new boolean[words.length];

        for(int k = 0; k < typed.length() - 1; k++) {
            int i = typed.charAt(k) - 'a';
            int j = typed.charAt(k + 1) - 'a';

            for(int index: twoGram[i][j]) {
                if(!seen[index]) {
                    seen[index] = true;
                    int distance = ed(typed, words[index]);
                    if(distance <= threshold) {
                        possibleWords.add(new CandidateWord(distance, words[index]));
                    }
                }
            }
        }

        // Sort
        possibleWords.sort(Comparator.comparing(CandidateWord::geted)
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
}
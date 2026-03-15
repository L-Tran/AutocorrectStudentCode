public class CandidateWord  {

    // Instance variables
    private int ed;
    private String word;
    private int frequency;

    // Constructor
    public CandidateWord(int ed, String word, int frequency) {
        this.ed = ed;
        this.word = word;
    }

    // Getters
    public int geted() {
        return ed;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }
}

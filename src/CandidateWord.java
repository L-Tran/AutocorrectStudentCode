public class CandidateWord  {
    private int ed;
    private String word;
    private int frequency;


    public CandidateWord(int ed, String word, int frequency) {
        this.ed = ed;
        this.word = word;
    }

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

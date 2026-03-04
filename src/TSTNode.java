public class TSTNode {
    // Instance variables
    private boolean isWord;
    private TSTNode[] children;
    private char letter;

    // Constructor
    public TSTNode(char c) {
        letter = c;
        // Each node has 3 children for each direction
        children = new TSTNode[3];
        isWord = false;
    }

    // Getters & Setters
    public TSTNode[] getChildren() {
        return children;
    }

    public boolean isWord() {
        return isWord;
    }

    public char getLetter() {
        return letter;
    }

    public void setWord(boolean bool) {
        isWord = bool;
    }
}

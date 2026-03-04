public class TST {
    // Instance variables
    private TSTNode root;

    // Constants for direction for TSTNode
    public static final int LESS_NODE = 0;
    public static final int EQUAL_NODE = 1;
    public static final int GREATER_NODE = 2;


    // Constructor
    public TST() {
        // Set root to m because it is the middle of the alphabet
        root = new TSTNode('m');
    }

    // Insert word
    public void insert(String word) {
        TSTNode current = root;
        // Counter for current letter
        int i = 0;
        // While the loop has not gone through the entire word
        while(i < word.length()) {
            char c = word.charAt(i);
            // If letter is less than node
            if(c < current.getLetter()) {
                // If less node is empty
                if(current.getChildren()[LESS_NODE] == null) {
                    // Set node to current letter
                    current.getChildren()[LESS_NODE] = new TSTNode(c);
                }
                // Iterate to the next node
                current = current.getChildren()[LESS_NODE];
            }
            // If letter is greater than node
            else if (c > current.getLetter()) {
                // If greater node is empty
                if(current.getChildren()[GREATER_NODE] == null) {
                    // Set node to current letter
                    current.getChildren()[GREATER_NODE] = new TSTNode(c);
                }
                // Iterate to the next node
                current = current.getChildren()[GREATER_NODE];
            }
            // If letter is equal to the letter of the node
            else {
                // Make sure the end of the word is not reached
                if(i + 1 == word.length()) {
                    current.setWord(true);
                    break;
                }
                // Iterate to next letter of word
                i++;
                c = word.charAt(i);
                // If equal node is empty
                if(current.getChildren()[EQUAL_NODE] == null) {
                    // Set to the next letter
                    current.getChildren()[EQUAL_NODE] = new TSTNode(c);
                }
                // Iterate to the next node
                current = current.getChildren()[EQUAL_NODE];
            }
        }
        // Set last node to true to make the word true
        current.setWord(true);
    }

    // Search for word
    public boolean search(String word) {
        TSTNode current = root;
        int i = 0;
        // Make sure current node is not null
        while(current != null) {
            char c = word.charAt(i);
            // If letter is less than node
            if(c < current.getLetter()) {
                // Iterate to next node
                current = current.getChildren()[LESS_NODE];

            }
            // If letter is greater than node
            else if (c > current.getLetter()) {
                // Iterate to next node
                current = current.getChildren()[GREATER_NODE];
            }
            // If letter is equal to node
            else {
                // Make sure the end of the word is not reached
                if(i + 1 == word.length()){
                    // Return if the word is word or not if it is the end of the word
                    return current.isWord();
                }
                // Go to next letter
                i++;
                // Iterate to next node
                current = current.getChildren()[EQUAL_NODE];
            }
        }
        // Return false if it is null
        return false;
    }

}

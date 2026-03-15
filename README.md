Autocorrect

This Autocorrect tool first prompts the user to enter the word. If the word is a real word, the code with detect it with
an if-statement that uses the search function implemented in the TST class with the dictionary that was inserted as a TST.
If the word is not a word, then the code will ask the user if they mean up to three possible words. Those words are found 
using the runTest function which synthesizes the top three words that are most similar to the misspelled word. To do this, it
utilizes the two-gram array that was initialized in the beginning of the code. It assigns every two-gram arraylist all the words that
have the two-gram in it, which helps narrow down the search of similar words for each misspelled word. Then out of the two-
gram words, it checks the edit distance for each word. The edit distance function utilizes edit distance levenshtein distance
using dynamic program. This specific function uses tabulation. After that is found and assigned to each potential word, the 
list of words is then sorted in order of edit, frequency, and then alphabet. Then the top three elements of the array are presented
to the user, and asks them to select the word they intended to choose. When the user selects the word, it increases the frequency
of the word in a different hashmap. That frequency is then accounted for in later sorting. 
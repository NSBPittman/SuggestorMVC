import java.io.*;
import java.util.ArrayList;

/**
 * Created by Nick Pittman on 1/21/2016.
 * Suggests word from sent in dictionary based on what is partially finished word is sent in
 */
public class WordModelAndController implements ISuggester {
    private int currentIndexOfSpace;
    private final ArrayList<String> dictionary = new ArrayList<>();
    private int numCharacters;

    /**
     * Creates dictionary for class to check sent in words
     * @param dictionaryLocation string represents file path of dictionary
     */
    public WordModelAndController(String dictionaryLocation, int numCharacters) throws IOException {
        this.numCharacters = numCharacters;

        File dicFile;
        BufferedReader br = null;

        try {
            dicFile = new File(dictionaryLocation);
            if (!dicFile.exists()) {//file not found
                throw new FileNotFoundException("Could not find file: " + dictionaryLocation);
            }
            br = new BufferedReader(new FileReader(dicFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] entries = line.split(",");
                for (String word : entries) {
                    dictionary.add(word);
                }
            }
        } finally {
            br.close();
        }
    }

    public ArrayList<String> calculateBestMatches(String line, int numMatches){

        ArrayList<String> match = new ArrayList<>();
        String currentlyTypedWord = getCurrentlyTypedWord(line);
        String alreadyTypedWords = getAlreadyTypedWords(line);

        for (String word : dictionary) {
            boolean fullymatches = true;
            if (currentlyTypedWord.length() > numCharacters) {
                for (int i = 0; i < currentlyTypedWord.length(); i++) {
                    if (!currentlyTypedWord.toLowerCase().startsWith(String.valueOf(word.toLowerCase().charAt(i)), i)) {
                        fullymatches = false;
                        break;
                    }
                }
                if (fullymatches) {
                    match.add(alreadyTypedWords + word);
                    return match;
                }
            }
        }
        return match;
    }

    public String getSuggested(String suggestedWord, String text){
        String newText = text.substring(text.lastIndexOf(text)).replace(text, suggestedWord);
        return newText;
    }

    /**
     * finds the word that is currently being typed, separates it from preceding words
     * @param line complete line sent in
     * @return string for word that is currently being typed
     */
    private String getCurrentlyTypedWord(String line){
        String wordBeingTyped = "";
        if (line.contains(" ")) {
            int tmp = line.lastIndexOf(" ");
            if (tmp >= currentIndexOfSpace) {
                currentIndexOfSpace = tmp;
                wordBeingTyped = line.substring(line.lastIndexOf(" "));
            }
        }
        else {
            wordBeingTyped = line;
        }
        return wordBeingTyped.trim();
    }

    /**
     * finds the text that precedes the word that is currently being typed
     * @param line complete line sent in
     * @return string of what was entered prior to current word
     */
    private String getAlreadyTypedWords(String line){
        String currentlyTypedWord = getCurrentlyTypedWord(line);
        int previousLength = line.length() - currentlyTypedWord.length();
        String previouslyTyped = line.substring(0, previousLength);
        return previouslyTyped;
    }
}

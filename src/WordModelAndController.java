import java.io.*;
import java.util.ArrayList;

/**
 * Created by Nick Pittman on 1/21/2016.
 * Suggests word from sent in dictionary based on what is partially finished word is sent in
 */
public class WordModelAndController implements ISuggestor {
    private int currentIndexOfSpace;
    private final ArrayList<String> dictionary = new ArrayList<>();

    /**
     * Creates dictionary for class to check sent in words
     * @param dictionaryLocation string represents file path of dictionary
     */
    public WordModelAndController(String dictionaryLocation) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(dictionaryLocation)));
            String line;
            while ((line = br.readLine()) != null) {

                String[] entries = line.split(",");

                for (String word : entries) {
                    dictionary.add(word);
                }
            }
        }

        catch(FileNotFoundException exception)
        {
            System.out.println("The file " + dictionaryLocation + " was not found.");
        }
        catch(IOException exception)
        {
            System.out.println(exception);
        }
    }

    public ArrayList<String> calculateBestMatches(String line, int numMatches){//be more consistent in naming things

        ArrayList<String> match = new ArrayList<>();
        String currentlyTypedWord = getCurrentlyTypedWord(line);
        String alreadyTypedWords = getAlreadyTypedWords(line);

        for (String word : dictionary) {//get words in the dictionary which we added
            boolean fullymatches = true;
            if (currentlyTypedWord.length() > 2) { //requires that 3 characters be typed
                for (int i = 0; i < currentlyTypedWord.length(); i++) {//each string in the word
                    if (!currentlyTypedWord.toLowerCase().startsWith(String.valueOf(word.toLowerCase().charAt(i)), i)) {//check for match
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

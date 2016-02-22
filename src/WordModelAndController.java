import java.io.*;
import java.util.ArrayList;

/**
 * Created by NickDesktop on 1/21/2016.
 */
public class WordModelAndController implements ISuggestor {
    private int currentIndexOfSpace;
    private final ArrayList<String> dictionary = new ArrayList<>();


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

    public ArrayList<String> calculateBestMatches(String userHyp, int numMatches){//be more consistent in naming things

        ArrayList<String> match = new ArrayList<>();
        String currentlyTypedWord = getCurrentlyTypedWord(userHyp);
        String alreadyTypedWords = getAlreadyTypedWords(userHyp);



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
                    System.out.println("userHyp: " + userHyp + "\nword: " + word + "\ncurrentlyTypedWord:" + currentlyTypedWord);

                    match.add(alreadyTypedWords + word);
                    return match;
                }
            }
        }
        return match;
    }

    public String getSuggested(String suggestedWord, String text, String typedWord, String t){

        String newText = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);;
        return newText;
    }

    private String getCurrentlyTypedWord(String userHyp){
        String wordBeingTyped = "";
        if (userHyp.contains(" ")) {
            int tmp = userHyp.lastIndexOf(" ");
            if (tmp >= currentIndexOfSpace) {
                currentIndexOfSpace = tmp;
                wordBeingTyped = userHyp.substring(userHyp.lastIndexOf(" "));
            }
        }
        else {
            wordBeingTyped = userHyp;
        }
        return wordBeingTyped.trim();
    }

    private String getAlreadyTypedWords(String userHyp){
        String currentlyTypedWord = getCurrentlyTypedWord(userHyp);
        int previousLength = userHyp.length() - currentlyTypedWord.length();
        String previouslyTyped = userHyp.substring(0, previousLength);
        return previouslyTyped;
    }

}

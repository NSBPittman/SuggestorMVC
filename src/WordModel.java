import java.util.ArrayList;

/**
 * Created by NickDesktop on 1/21/2016.
 */
public class WordModel implements ISuggestor {
    private int currentIndexOfSpace;
    private final ArrayList<String> dictionary = new ArrayList<>();

    public ArrayList<String> calculateBestMatches(String userHyp, int numMatches){//be more consistent in naming things
        ArrayList<String> words = new ArrayList<>();
        words.add("Patient");
        words.add("has");
        words.add("sclerosis");
        words.add("hyperthyroid");
        words.add("vitiligo");
        words.add("scleritis");
        words.add("leukemia");
        words.add("dead");
        words.add("world");
        words.add("hello");

        for (String word : words) {
            dictionary.add(word);
        }

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
        //String text = textField.getText();
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
        System.out.println("previouslyTyped: " + previouslyTyped);
        return previouslyTyped;
    }

}

import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.spell.FixedWeightEditDistance;
import com.aliasi.util.Distance;
import com.aliasi.spell.JaroWinklerDistance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SpellChecker implements ISuggester {
    protected List<String> dictionary;
    private int numCharacters;
//    private JaccardDistance jaccard;
    private JaroWinklerDistance jaroWinkler;


    public SpellChecker(String dictionaryLocation, int numCharacters) throws IOException {
        this.dictionary = readInDictionary(dictionaryLocation);
        this.numCharacters = numCharacters;
//        TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
//        this.jaccard = new JaccardDistance(tokenizerFactory);
        this.jaroWinkler = JaroWinklerDistance.JARO_WINKLER_DISTANCE;

    }

    public SpellChecker(List<String> dictionary, int numCharacters){
        this.dictionary = dictionary;
        this.numCharacters = numCharacters;
//        TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
//        this.jaccard = new JaccardDistance(tokenizerFactory);
        this.jaroWinkler = JaroWinklerDistance.JARO_WINKLER_DISTANCE;

    }

    /**
     * builds List<String> for passed in file location
     * @param dicLocation filepath to file to be read
     * @return List<String> of lines from file
     * @throws IOException
     */
    public List<String> readInDictionary(String dicLocation) throws IOException{
        dictionary = new ArrayList<String>();
        File dicFile;
        BufferedReader br = null;

        try {
            dicFile = new File(dicLocation);
            if (!dicFile.exists()) {//file not found
                throw new FileNotFoundException("Could not find file: " + dicLocation);
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
        return dictionary;
    }

    private String lowestDistance(String wordIn){
        double lowestDistance = .100;
        String currentSuggestion = wordIn;
        for (String dicWord : dictionary){
            float cDistance = (float)jaroWinkler.distance(dicWord, wordIn);
            if (cDistance < lowestDistance) {
                lowestDistance = cDistance;
                currentSuggestion = dicWord;
                //System.out.println("lowestDistance: " + lowestDistance + "\tcurrentSuggestion: " + currentSuggestion);
            }
        }
        return currentSuggestion;
    }

//    private void jaccardTest(String wordIn){
//        for (String dicWord : dictionary) {
//            System.out.println(dicWord + ": " +jaccard.distance(dicWord, wordIn));
//        }
//        System.out.println("\n");
//    }

    private String suggestWord(String wordIn) {
        String suggestedWord = wordIn;
        for (String dicWord : dictionary) {
            //System.out.println("suggestWord: dicWord: "+ dicWord + " wordIn: " + wordIn);
            if (dicWord.equals(wordIn)) {
                //System.out.println("FULL MATCH");
                return suggestedWord;
            }
            else {
                //System.out.println("NOT FULL MATCH: calling lowestDistance");
                suggestedWord = lowestDistance(wordIn);
            }
        }
        return suggestedWord;
    }

//    private void jaroTest(){
//        JaroWinklerDistance jaroWinkler = JaroWinklerDistance.JARO_WINKLER_DISTANCE;
//        System.out.println(jaroWinkler.distance("cancer", "cacner"));
//
//    }


    public ArrayList<String> calculateBestMatches(String phrase, int numMatches){
        ArrayList<String> match = new ArrayList<>();
        String[] wordsInPhrase = phrase.split("\\s+");

        String correctedPhrase = "";
        for (String word : wordsInPhrase) {
            correctedPhrase = correctedPhrase + " " + suggestWord(word);
            correctedPhrase = correctedPhrase.trim();
            System.out.println(correctedPhrase);
        }
        match.add(correctedPhrase);

        return match;
    }
}


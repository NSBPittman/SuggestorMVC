import com.aliasi.spell.JaccardDistance;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SpellCheckJaccard implements ISuggester {
    protected List<String> dictionary;
    private int numCharacters;

    public SpellCheckJaccard(String dictionaryLocation, int numCharacters) throws IOException {
        this.dictionary = readInDictionary(dictionaryLocation);
        this.numCharacters = numCharacters;
    }

    public SpellCheckJaccard(List<String> dictionary, int numCharacters){
        this.dictionary = dictionary;
        this.numCharacters = numCharacters;
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

    private String suggestWord(String wordIn){
        for (String word : dictionary) {
            boolean fullymatches = true;
            if (wordIn.length() > numCharacters) {
                for (int i = 0; i < wordIn.length(); i++) {
                    if (!wordIn.toLowerCase().startsWith(String.valueOf(word.toLowerCase().charAt(i)), i)) {
                        fullymatches = false;
                        break;
                    }
                }
                if (fullymatches) {
                    System.out.println("Case fullmatches");
                    return word;
                }
            }
        }
        System.out.println("Case not full matches");
        return wordIn;
    }

    public ArrayList<String> calculateBestMatches(String phrase, int numMatches){
        ArrayList<String> matches = new ArrayList<>();
        String[] wordsInPhrase = phrase.split("\\s+");

        for (String word : wordsInPhrase){
            //System.out.println(word);
            System.out.println(suggestWord(word));

        }
        return matches;
    }
}


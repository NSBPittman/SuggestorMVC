/**
 * Created by Nick Pittman on 1/19/2016.
 * Suggests string/phrase bases on closeness of sent in text to sent in csv file of phrases
 */
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.aliasi.tokenizer.EnglishStopTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.Tokenization;
import com.aliasi.tokenizer.TokenizerFactory;

/**
 * TFIDFModelAndController has variabls
 * EKBLocation a file path to sent in phrase csv file
 * documents a List of all phrases from csv file
 * stemDocs a stemmed List of phrases from csv file
 * tempDocs a String array used to read in csv file
 * minReq a required score for phrase to have to be considered "good enough" to be returned
 */
public class TFIDFModelAndController implements ISuggester {
    private String EKBLocation;
    protected List<String> documents;
    private List<String> stemDocs;
    private double minReq;

    /**
     * Constructor, creates documents and stemDocs
     * @param EKBLocation file path for sent in csv file
     * @param minReq a required score for phrase to have to be considered "good enough" to be returned
     */
    public TFIDFModelAndController(String EKBLocation, double minReq) {
        this.EKBLocation = EKBLocation;
        this.minReq = minReq;
        documents = new ArrayList<String>();

        try {
            BufferedReader ugh = new BufferedReader(new FileReader(new File(EKBLocation)));
            BufferedReader br = new BufferedReader(new FileReader(new File(EKBLocation)));
            List<String> itemsToAdd = new ArrayList<String>();
            String line;
            while((line = br.readLine()) != null){
                documents.add(line);
            }

        }
        catch(FileNotFoundException exception)
        {
            System.out.println("The file " + EKBLocation + " was not found.");
        }
        catch(IOException exception)
        {
            System.out.println(exception);
        }

        //documents = Arrays.asList(tempDocs);

        stemDocs = makeStemmedDocuments(documents);//TAKE DOCUMENTS AND RETURN TOKENIZED AND STEMMED DOCUMENTS
    }

    /**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(String doc, String term) {
        double result = 0;
        double length = (Math.random() * .0000000000000001);//maybe create function to generate this number so it can be not random for testing or random if it needs tp be

        String[] splitStr = term.split("\\s+");
        for (String newTerm : splitStr) {
            //System.out.println(newTerm);
            String[] splitDoc = doc.split("\\s+");
            for (String word : splitDoc) {
                length++;
                //System.out.println(word);
                if (newTerm.equalsIgnoreCase(word)) {
                    result++;
                    //System.out.println(result);
                }
            }
        }
        return result / length;
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(List<String> docs, String term) {
        double n = 0;
        String[] splitTerm = term.split("\\s+");
        for (String doc : docs) {
            String[] splitDoc = doc.split("\\s+");
            for (String newTerm : splitTerm) {
                for (String word : splitDoc) {
                    //System.out.println("Word: " + word + "in Doc: " + doc);
                    if (newTerm.equalsIgnoreCase(word)) {
                        n++;
                        break;
                        //use bool to break out of inner for loop
                        //or make inner loop into a function of it's own
                        //break exists so that n is count of how many document term appears in not how many times it appears in all docs
                    }
                }
            }
        }
        n = n + (Math.random() * .0000000000000000001);
        return Math.abs(Math.log((docs.size() / n)));
    }


    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(String doc, List<String> docs, String term) {
        //System.out.println("Doc: " + doc + " Size: " + doc.size() + "\nDocs: " + docs + "\nTerm: " + term);
        return tf(doc, term) * idf(docs, term);

    }

    /**
     * Tokenizes sent in line
     * @param line string to be tokenized
     * @return ArrayList of tokenized line
     */
    public static ArrayList<String> tokenize(String line) {
        // create a new instance
        TokenizerFactory f1 = IndoEuropeanTokenizerFactory.INSTANCE;
        // create new object for lowercase tokenizing
        TokenizerFactory fLowercase = new LowerCaseTokenizerFactory(f1);
        // create new object for english stop word list
        TokenizerFactory fStopEngTokenize = new EnglishStopTokenizerFactory(
                fLowercase);
        // do tokenizing for line based on the english stop word list that we
        // have created
        Tokenization tk = new Tokenization(line, fStopEngTokenize);

        // get whole tokens result
        String[] result = tk.tokens();
        // store to arraylist, it is optional, you could resurn String[] also.
        ArrayList<String> arrResultToken = new ArrayList<String>();
        for (int i = 0; i < result.length; i++) {
            arrResultToken.add(result[i]);
        }
        return arrResultToken;
    }

    /**
     * Stems sent in token ArrayList
     * @param token sent in ArrayList of tokens to be stemmed
     * @return ArrayList of stemmed tokens
     */
    public static ArrayList<String> stemming(ArrayList<String> token) {
        TokenizerFactory f1 = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory fPorter = new PorterStemmerTokenizerFactory(f1);
        ArrayList<String> arrResultStem = new ArrayList<String>();
        for (int i = 0; i < token.size(); i++) {
            Tokenization tk1 = new Tokenization(token.get(i), fPorter);
            String[] rs = tk1.tokens();
            arrResultStem.add(rs[0]);
        }
        return arrResultStem;
    }

    private static String stringBuilder (ArrayList<String> arrList) {
        String nString = "";
        for (String value : arrList) {
            nString = nString + " " + value;
        }
        nString = nString.substring(1);
        return nString;
    }

    /**
     * Takes in List of documents, tokenizes, then stems them
     * @param documents List of phrases to be stemmed
     * @return tokenized and stemmed documents
     */
    private static ArrayList<String> makeStemmedDocuments(List<String> documents) {
        ArrayList<String> sDocs = new ArrayList<String>();
        for (String cDoc : documents){
            ArrayList<String> tDoc = tokenize(cDoc);
            ArrayList<String> sDoc = stemming(tDoc);
            String stemDoc = stringBuilder(sDoc);
            sDocs.add(stemDoc);
        }
        return sDocs;
    }

    /**
     * Gets score for document/phrase in comparison with entered user text/hypothesis
     * @param documents List of phrases to be stemmed
     * @param cDoc Doc to be scored
     * @param hypothesis User entered phrase, used to score cDoc
     * @return
     */
    private Double docScore(List<String> documents, String cDoc, String hypothesis){
        Double score = 0.0;
        TFIDFModelAndController calculator = new TFIDFModelAndController(EKBLocation, minReq);

        ArrayList<String> hyp = tokenize(hypothesis);
        hyp = stemming(hyp);
        for (String cHyp : hyp) {
            score += calculator.tfIdf(cDoc, documents, cHyp);
        }
        return score;
    }

    private static void reverse(double[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Calculates best score and returns double array of scores that meet minReq
     * @param scoreArr double array of scores for the documents/phrases
     * @param retLen length of array to return
     * @return double array of scores that meet minReq
     */
    private double[] getTopScores(double[] scoreArr, int retLen){
        double[] topArr = new double[retLen];
        for (int i = 0; i < retLen; i++)
            topArr[i] = -1;

        Arrays.sort(scoreArr);
        reverse(scoreArr);
        int ifI = 0;
        for (int i = 0; i < retLen; i++){
            if (scoreArr[i] >= minReq){
                topArr[ifI] = scoreArr[i];
                ifI++;
            }
        }
        return topArr;
    }

    private static boolean dubContains (double[] arr, double val){
        boolean contains = false;
        for (double arrVal : arr){
            if (arrVal == val)
                contains = true;
        }
        return contains;
    }


    /**
     * Creates ArrayList of the passed in documents that meet minReq
     * @param documents List of stemmed hypotheses/phrases score against hypothesis
     * @param hypothesis String that the suggester
     * @param ekbHyps List of hypotheses/phrases sent in to build sortedHyps with
     * @param numMatches number of matches to return
     * @return ArrayList<String> of the documents from ekbHyps that exceed minReq
     */
    private  ArrayList<String> getBestMatches (List<String> documents, String hypothesis, List<String> ekbHyps, int numMatches){
        double[] scoreArr;
        scoreArr = new double[documents.size()];
        double cDocScore;
        int iter = 0;
        for (String cDoc : documents){
            cDocScore = docScore(documents, cDoc, hypothesis);
            scoreArr[iter] = cDocScore;
            iter++;
        }

        ArrayList<String> sortedHyps = new ArrayList<>();
        double[] scoreArrOG = new double[scoreArr.length];
        for (int i = 0; i < scoreArr.length; i++)
            scoreArrOG[i] = scoreArr[i];

        double[] topScores = getTopScores(scoreArr, numMatches);

        for (int i = 0; i < scoreArr.length; i++){
            if (dubContains(topScores, scoreArrOG[i])){
                sortedHyps.add(ekbHyps.get(i));
            }
        }
        return sortedHyps;
    }


    public ArrayList<String> calculateBestMatches(String line, int numMatches){//be more consistent in naming things
        ArrayList<String> best;
        best = getBestMatches(stemDocs, line, documents, numMatches);
        return best;
    }

    public String getSuggested(String suggestedWord, String text){
        String newText = suggestedWord;
        return newText;
    }
}

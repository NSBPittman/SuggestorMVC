/**
 * Created by NickDesktop on 1/19/2016.
 */
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
 * Created by NickDesktop on 11/16/2015.
 */
//model
public class TFIDFmodel implements ISuggestor {
    /**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(String doc, String term) {
        double result = 0;
        double length = (Math.random() * .00000000000000000000000000000001);//maybe create function to generate this number so it can be not random for testing or random if it needs tp be

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
        //todo instead of making sure length > 0 throw exception if length is 0

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
                        //System.out.println("TERMS: " + n);
                        break;//todo rewrite code to not use break
                        //use bool to break out of inner for loop
                        //or make inner loop into a function of it's own
                        //break exists so that n is count of how many document term appears in not how many times it appears in all docs
                    }
                }
            }
        }
        //System.out.println("doc size: " + docs.size() + " | n: :" + n + " | idf: " + docs.size() / n);
        n = n + (Math.random() * .00000000000000000000000000000001);
        return Math.abs(Math.log((docs.size() / n)));
        //return Math.log(docs.size() / n);
    }

    public double idf2(List<String> docs, String term) {
        double n = 0;
        String[] splitTerm = term.split("\\s+");
        for (String doc : docs) {
            String[] splitDoc = doc.split("\\s+");
            List<String> wordsFound;
            for (String newTerm : splitTerm) {
                for (String word : splitDoc) {
                    //System.out.println("Word: " + word + "in Doc: " + doc);
                    if (newTerm.equalsIgnoreCase(word)) {
                        n++;
                        //System.out.println("TERMS: " + n);
                        break;//todo rewrite code to not use break
                        //break exists so that n is count of how many document term appears in not how many times it appears in all docs
                    }
                }
            }
        }
        //System.out.println("doc size: " + docs.size() + " | n: :" + n + " | idf: " + docs.size() / n);
        n = n + (Math.random() * .00000000000000000000000000000001);
        return Math.abs(Math.log((docs.size() / n)));
        //return Math.log(docs.size() / n);
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
        //System.out.print("\nString: " + nString);
        nString = nString.substring(1);
        //System.out.print("\nnString:" + nString);

        return nString;
    }

    private static ArrayList<String> makeStemmedDocuments(List<String> documents) {
        //String stemDocs = "";
        ArrayList<String> sDocs = new ArrayList<String>();
        for (String cDoc : documents){
            ArrayList<String> tDoc = tokenize(cDoc);
            ArrayList<String> sDoc = stemming(tDoc);
            String stemDoc = stringBuilder(sDoc);
            sDocs.add(stemDoc);
            //stemDocs = stemDocs + "," + stemDoc;
        }//todo maybe think about adding stemDocs to arrayList rather than making a string than converting it...
        //System.out.print("stemDocs: " + stemDocs);
        //ArrayList<String> sDList = new ArrayList<String>(Arrays.asList(stemDocs.split(",")));
        //System.out.print("\nsDList: " + sDList);
        //String haha = stringBuilder(sDList);
        //System.out.print("\nHAHA: " + haha);

        //sDList.remove(0);
        //System.out.println("IN makeStemmedDocuments: " + sDocs);

        return sDocs;
    }

    private static Double docScore(List<String> documents, String cDoc, String hypothesis){
        Double score = 0.0;
        TFIDFmodel calculator = new TFIDFmodel();

        ArrayList<String> hyp = tokenize(hypothesis);
        hyp = stemming(hyp);
        for (String cHyp : hyp) {
            score += calculator.tfIdf(cDoc, documents, cHyp);
        }
        return score;
    }

    private static String bestMatch(List<String> documents, String hypothesis, List<String> ekbHyps){
        String topRes = "NAH";        double[] scoreArr;
        scoreArr = new double[documents.size()];
        //ArrayList docScore = new ArrayList();
        Double cDocScore = 0.0;
        System.out.println("\n--------------------------\nIN BEST MATCH\n--------------------------");
        int iter = 0;
        for (String cDoc : documents){
            cDocScore = docScore(documents, cDoc, hypothesis);
            scoreArr[iter] = cDocScore;
            iter++;
            //docScore.add(cDocScore);
            System.out.println(cDoc + ": " + cDocScore);
        }

        double maxScore = 0.0;
        for (double score : scoreArr){
            //System.out.print("Score: " + score + "\n");
            if (score>maxScore){
                maxScore = score;
            }
        }
        System.out.println("\nMaximum Score for Top Match: " + maxScore);

        //Get string for doc with best score
        //todo take best score and find associated doc strings
        int maxIndex = -1;
        for (int i = 0; i < scoreArr.length; i++){
            if (scoreArr[i] == maxScore){
                maxIndex = i;
            }
        }
        topRes = ekbHyps.get(maxIndex);
        System.out.println("\nBest EKB hypothesis: " + topRes);

        return topRes;
    }

    private static ArrayList<String> bestMatches(List<String> documents, String hypothesis, List<String> ekbHyps){
        //have param for length of list to return, and other const in notes on google drive.
        ArrayList<String> topRes = new ArrayList<String>();
        double[] scoreArr;
        scoreArr = new double[documents.size()];
        //ArrayList docScore = new ArrayList();
        Double cDocScore = 0.0;
        System.out.println("\n--------------------------\nIN BEST MATCH\n--------------------------");
        int iter = 0;
        for (String cDoc : documents){
            cDocScore = docScore(documents, cDoc, hypothesis);
            scoreArr[iter] = cDocScore;
            iter++;
            //docScore.add(cDocScore);
            System.out.println(cDoc + ": " + cDocScore);
        }

        double maxScore = 0.0;
        for (double score : scoreArr){
            //System.out.print("Score: " + score + "\n");
            if (score>maxScore){
                maxScore = score;
            }
        }
        System.out.println("\nMaximum Score for Top Match: " + maxScore);

        //Get string for doc with best score
        //todo take best score and find associated doc strings
        //int maxIndex = -1;
        for (int i = 0; i < scoreArr.length; i++){
            if (scoreArr[i] > maxScore-.2){
                //maxIndex = i;
                topRes.add(ekbHyps.get(i));
            }
        }
        //topRes = ekbHyps.get(maxIndex);
        System.out.println("\nBest EKB hypothesis: " + topRes);

        return topRes;
    }

    //oh lord the side effects
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

    //returns array of requested length provided scores meet basic requirements
    //hello side effects
    private static double[] getTopScores(double[] scoreArr, int retLen){
        double[] topArr = new double[retLen];
        for (int i = 0; i < retLen; i++)
            topArr[i] = -20.20;

        double minReq = .5;//TO BE MADE GLOBAL
        Arrays.sort(scoreArr);
        reverse(scoreArr);
        //need iterator that only get's iterated if score matches req
        int ifI = 0;
        for (int i = 0; i < retLen; i++){
            if (scoreArr[i] >= minReq){
                topArr[ifI] = scoreArr[i];
                ifI++;
            }
        }
//        for (double score : topArr)
//            System.out.println("in getTopScores: " + score);

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

    private static ArrayList<String> getBestMatches (List<String> documents, String hypothesis, List<String> ekbHyps, int numMatches){
        //have param for length of list to return, and other const in notes on google drive.
        ArrayList<String> topRes = new ArrayList<String>();
        double[] scoreArr;
        scoreArr = new double[documents.size()];
        //ArrayList docScore = new ArrayList();
        double cDocScore;
        System.out.println("\n--------------------------\nIN BEST MATCH\n--------------------------");
        int iter = 0;
        for (String cDoc : documents){
            cDocScore = docScore(documents, cDoc, hypothesis);
            scoreArr[iter] = cDocScore;
            iter++;
            //docScore.add(cDocScore);
            System.out.println(cDoc + ": " + cDocScore);
        }

        ArrayList<String> sortedHyps = new ArrayList<>();
        double[] sortedScores = new double[documents.size()];
        double maxScore = sortedScores[0];
        System.out.println("\nMaximum Score for Top Match: " + maxScore);
        double[] scoreArrOG = new double[scoreArr.length];
        for (int i = 0; i < scoreArr.length; i++)
            scoreArrOG[i] = scoreArr[i];

        double[] topScores = getTopScores(scoreArr, numMatches);

        for (int i = 0; i < scoreArr.length; i++){
            if (dubContains(topScores, scoreArrOG[i])){
                sortedHyps.add(ekbHyps.get(i));
            }
        }

        //topRes = ekbHyps.get(maxIndex);
        System.out.println("\nBest EKB hypothesis: " + sortedHyps);

        return sortedHyps;
    }

//    public static String returnBestMatch(String userHyp){
//        String doc1 = "Patient has Hyperthyroid";
//        String doc2 = "Patient has Leukemia";
//        String doc3 = "Patient has Scleritis";
//        String doc4 = "Patient has Vitiligo";
//        String doc5 = "Patient has Sclerosis";
//        String doc6 = "Patient has Crohn's disease";
//        //ASSEMBLE LIST OF DOCUMENTS
//        List<String> documents = Arrays.asList(doc1, doc2, doc3, doc4, doc5, doc6);
//        //TAKE DOCUMENTS AND RETURN TOKENIZED AND STEMMED DOCUMENTS
//        List<String> stemDocs = makeStemmedDocuments(documents);
//        //Round2 calculator = new Round2();
//        String best = bestMatch(stemDocs, userHyp, documents);
//
//        return best;
//    }

    public ArrayList<String> calculateBestMatches(String userHyp, int numMatches){//be more consistent in naming things
        String doc1 = "Patient has Hyperthyroid";
        String doc2 = "Patient has Leukemia";
        String doc3 = "Patient has Scleritis";
        String doc4 = "Patient has Vitiligo";
        String doc5 = "Patient has Sclerosis";
        String doc6 = "Patient has Crohn's disease";
        String doc7 = "The patient has contracted sclerosis";
        //ASSEMBLE LIST OF DOCUMENTS
        List<String> documents = Arrays.asList(doc1, doc2, doc3, doc4, doc5, doc6, doc7);
        //TAKE DOCUMENTS AND RETURN TOKENIZED AND STEMMED DOCUMENTS
        List<String> stemDocs = makeStemmedDocuments(documents);
        //Round2 calculator = new Round2()

        ArrayList<String> best;
        best = getBestMatches(stemDocs, userHyp, documents, numMatches);
        return best;
    }

    public String getSuggested(String suggestedWord, String text, String typedWord, String t){
        String newText = suggestedWord;
        return newText;
    }
}

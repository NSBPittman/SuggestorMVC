import com.sun.media.jfxmedia.logging.Logger;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick Pittman on 2/21/2016.
 * EXISTS ONLY AS A TEMPLATE TO PLUG INTO LARGER PROJECTS
 * contains:
 *      frame and panel to stuff,
 *      strings for the filepath of the used csv files,
 *      minReq setting to determine how strong a match has to be for TFIDF matching
 *      numCharacters setting to determine how many characters must be entered to trigger word suggesting
 *      comment toggled code to decide on which model to use
 *      Calls AutoSuggestorMVC, which creates AutoSuggestor
 */
public class ExampleClass {

    public static List<String> readInDictionary(String dicLocation) throws IOException {
        List<String> dictionary = new ArrayList<String>();
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

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();

        String dictinaryLocation = "src/resources/lilDict.csv";
        //String dictinaryLocation = "src/resources/dictionary.csv";
        String EKBLocation = "src/resources/lilFakeEKB.csv";



        double minReq = 0.5;
        int numCharacters = 3;

        ISuggester theModel = null;

        try {
            //List<String> dictionary = readInDictionary(dictinaryLocation);
            //theModel = new WordModelAndController(dictionary,numCharacters);

            //theModel = new TFIDFModelAndController(EKBLocation, minReq);
            theModel = new WordModelAndController(dictinaryLocation, numCharacters);
        }
        catch (IOException e){
            //System.out.println(e);
            //e.printStackTrace();
            Logger.logMsg(0, e.toString());
        }

        AutoSuggestorCreator.createAutoSuggestor(frame, p, theModel);

    }
}

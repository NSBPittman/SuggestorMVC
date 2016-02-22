import javax.swing.*;
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
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();

        String dictinaryLocation = "src/resources/lilDict.csv";

        String EKBLocation = "src/resources/lilFakeEKB.csv";

        double minReq = 0.5;
        int numCharacters = 3;

        TFIDFModelAndController theModel = new TFIDFModelAndController(EKBLocation, minReq);
        //WordModelAndController theModel = new WordModelAndController(dictinaryLocation);

        AutoSuggestorMVC.createAutoSuggestor(frame, p, theModel);

    }
}

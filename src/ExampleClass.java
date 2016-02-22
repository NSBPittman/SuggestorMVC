import javax.swing.*;
import java.awt.*;
import java.io.File;
/**
 * Created by Nick Pittman on 2/21/2016.
 * EXISTS ONLY AS A TEMPLATE TO PLUG INTO LARGER PROJECTS
 */
public class ExampleClass {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();

        String dictinaryLocation = "src/resources/lilDict.csv";

        String EKBLocation = "src/resources/lilFakeEKB.csv";

        double minReq = 0.5;

        //TFIDFModelAndController theModel = new TFIDFModelAndController(EKBLocation, minReq);
        WordModelAndController theModel = new WordModelAndController(dictinaryLocation);

        AutoSuggestorMVC.createAutoSuggestor(frame, p, theModel);

    }
}

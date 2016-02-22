import javax.swing.*;
import java.awt.*;
import java.io.File;
/**
 * Created by NickDesktop on 2/21/2016.
 */
public class ExampleClass {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();

        String dictinaryLocation = "src/resources/lilDict.csv";

        String EKBLocation = "src/resources/lilFakeEKB.csv";

        TFIDFModelAndController theModel = new TFIDFModelAndController(EKBLocation);
        //WordModelAndController theModel = new WordModelAndController(dictinaryLocation);

        AutoSuggestorMVC.createAutoSuggestor(frame, p, theModel);

    }
}

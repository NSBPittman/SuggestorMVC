import javax.swing.*;
import java.awt.*;

/**
 * Created by Nick Pittman on 1/21/2016.
 * Creates AutoSuggestorView and packs frame
 */
public class AutoSuggestorCreator {
    public static void createAutoSuggestor(JFrame frame, JPanel p, ISuggester theModel) {
        JTextField f = new JTextField(25);
        AutoSuggesterView autoSuggestor = new AutoSuggesterView(f, frame, Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f, theModel) {
        };
        p.add(f);
        frame.add(p);
        frame.pack();
        frame.setVisible(true);
    }
}

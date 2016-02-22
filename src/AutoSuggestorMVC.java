import javax.swing.*;
import java.awt.*;

/**
 * Created by NickDesktop on 1/21/2016.
 */
//make window here and make autosuggestor extend textbox so that it
public class AutoSuggestorMVC {

    public static void createAutoSuggestor(JFrame frame, JPanel p, ISuggestor theModel) {
        JTextField f = new JTextField(25);

        AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, null, Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f, theModel) {
        };

        p.add(f);

        frame.add(p);

        frame.pack();
        frame.setVisible(true);


    }


}

import javax.swing.*;

/**
 * Created by NickDesktop on 1/21/2016.
 */
//make window here and make autosuggestor extend textbox so that it
public class AutoSuggestorMVC {

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new SuggestorView();
//            }
//
//
//        });
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();

        TFIDFmodel sModel = new TFIDFmodel();

        //WordModel sModel = new WordModel();

        SuggestorView sView = new SuggestorView(frame, p, sModel);



    }


}

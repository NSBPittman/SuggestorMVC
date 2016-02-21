import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Nick on 2/15/2016.
 */
public class SuggestionLabel extends JLabel{

        private boolean focused = false;
        private final JWindow autoSuggestionsPopUpWindow;
        private final JTextField textField;
        private final AutoSuggestor autoSuggestor;
        private Color suggestionsTextColor, suggestionBorderColor;
        private ISuggestor theModel;

        //HERE
        //TFIDFmodel theModel = new TFIDFmodel();
        //WordModel theModel = new WordModel();


        //view
        public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, AutoSuggestor autoSuggestor, ISuggestor theModel) {
            super(string);

            this.suggestionsTextColor = suggestionsTextColor;
            this.autoSuggestor = autoSuggestor;
            this.textField = autoSuggestor.getTextField();
            this.suggestionBorderColor = borderColor;
            this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();
            this.theModel = theModel;
            initComponent();
        }

        private void initComponent() {
            setFocusable(true);
            setForeground(suggestionsTextColor);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    super.mouseClicked(me);

                    replaceWithSuggestedText();

                    autoSuggestionsPopUpWindow.setVisible(false);
                }
            });

            getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
            getActionMap().put("Enter released", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    replaceWithSuggestedText();
                    autoSuggestionsPopUpWindow.setVisible(false);
                }
            });
        }

        public void setFocused(boolean focused) {
            if (focused) {
                setBorder(new LineBorder(suggestionBorderColor));
            } else {
                setBorder(null);
            }
            repaint();
            this.focused = focused;
        }

        public boolean isFocused() {
            return focused;
        }

        //view
        private void replaceWithSuggestedText() {

            //Word Suggester
            String suggestedWord = getText();
            String text = textField.getText();
            String typedWord = autoSuggestor.getCurrentlyTypedWord();//word model bug culprit
            String t = text.substring(0, text.lastIndexOf(typedWord));
            //String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
            //String tmp = suggestedWord;

            String tmp = theModel.getSuggested(suggestedWord, text, typedWord, t);
            textField.setText(tmp + " ");
            System.out.println("In replaceWithSuggestedText\n suggestedWord = " + suggestedWord + "\n text = " + text + "\n typedWord = " + typedWord + "\n t = " + t + "\n tmp = " + tmp);

            //Hyp Suggester
//        String hyp = textField.getText();
//        String
        }
    }



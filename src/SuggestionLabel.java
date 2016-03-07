import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Nick Pittman on 2/15/2016.
 * creates SuggestionLabels
 */
public class SuggestionLabel extends JLabel{

    private boolean focused = false;
    private final JWindow autoSuggestionsPopUpWindow;
    private final JTextField textField;
    private final AutoSuggesterView autoSuggestor;
    private Color suggestionsTextColor, suggestionBorderColor;
    private ISuggester theModel;
    private String selectedString;

    public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, AutoSuggesterView autoSuggestor, ISuggester theModel) {
        super(string);
        this.suggestionsTextColor = suggestionsTextColor;
        this.autoSuggestor = autoSuggestor;
        this.textField = autoSuggestor.getTextField();
        this.suggestionBorderColor = borderColor;
        this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();
        this.theModel = theModel;
        this.selectedString = string;
        //System.out.println("In SuggestionLabel constructor: " + string);
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

    /**
     *Replaces string in textField with suggested string
     */
    private void replaceWithSuggestedText() {
//        String suggestedWord = getText();
//        String text = textField.getText();
//        String tmp = theModel.getSuggested(suggestedWord, text);
//        textField.setText(tmp + " ");
        //System.out.println("replace: " + selectedString);
        textField.setText(selectedString);
    }
}



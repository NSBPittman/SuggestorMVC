import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by NickDesktop on 1/19/2016.
 */
public class SuggestorView extends JTextField{
    public SuggestorView(JFrame frame, JPanel p, ISuggestor theModel) {

        //JFrame frame = new JFrame();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JTextField f = new JTextField(25);

        AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, null, Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f, theModel) {
            //@Override
//            boolean wordTyped(String typedWord) {
//
//                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
//                ArrayList<String> words = new ArrayList<>();
//                words.add("Patient");
//                words.add("has");
//                words.add("sclerosis");
//                words.add("hyperthyroid");
//                words.add("vitiligo");
//                words.add("scleritis");
//                words.add("leukemia");
//                words.add("dead");
//                words.add("world");
//                words.add("hello");
//
//
//                setDictionary(words);
//                //addToDictionary("bye");//adds a single word
//
//                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
//            }
        };

        //JPanel p = new JPanel();

        p.add(f);

        frame.add(p);

        frame.pack();
        frame.setVisible(true);
    }

    //view

}
class AutoSuggestor {

    private final JTextField textField;
    private final Window container;
    private JPanel suggestionsPanel;
    private JWindow autoSuggestionPopUpWindow;
    private String typedWord;
    private final ArrayList<String> dictionary = new ArrayList<>();
    private int currentIndexOfSpace, tW, tH;
    private ISuggestor theModel;
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent de) {
            checkForAndShowSuggestions();
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            checkForAndShowSuggestions();
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            checkForAndShowSuggestions();
        }
    };
    private final Color suggestionsTextColor;
    private final Color suggestionFocusedColor;

    //HERE
    //TFIDFmodel theModel = new TFIDFmodel();
    //WordModel theModel = new WordModel();

    //view
    public AutoSuggestor(JTextField textField, Window mainWindow, ArrayList<String> words, Color popUpBackground, Color textColor, Color suggestionFocusedColor, float opacity, ISuggestor modelIn) {
        this.textField = textField;
        this.suggestionsTextColor = textColor;
        this.container = mainWindow;
        this.suggestionFocusedColor = suggestionFocusedColor;
        this.textField.getDocument().addDocumentListener(documentListener);
        theModel = modelIn;

        setDictionary(words);

        typedWord = "";
        currentIndexOfSpace = 0;
        tW = 0;
        tH = 0;

        autoSuggestionPopUpWindow = new JWindow(mainWindow);
        autoSuggestionPopUpWindow.setOpacity(opacity);

        suggestionsPanel = new JPanel();
        suggestionsPanel.setLayout(new GridLayout(0, 1));
        suggestionsPanel.setBackground(popUpBackground);

        addKeyBindingToRequestFocusInPopUpWindow();
    }


    //view
    private void addKeyBindingToRequestFocusInPopUpWindow() {
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
        textField.getActionMap().put("Down released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {//focuses the first label on popwindow
                for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
                    if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
                        ((SuggestionLabel) suggestionsPanel.getComponent(i)).setFocused(true);
                        autoSuggestionPopUpWindow.toFront();
                        autoSuggestionPopUpWindow.requestFocusInWindow();
                        suggestionsPanel.requestFocusInWindow();
                        suggestionsPanel.getComponent(i).requestFocusInWindow();
                        break;
                    }
                }
            }
        });
        suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
        suggestionsPanel.getActionMap().put("Down released", new AbstractAction() {
            int lastFocusableIndex = 0;

            @Override
            public void actionPerformed(ActionEvent ae) {//allows scrolling of labels in pop window (I know very hacky for now :))

                ArrayList<SuggestionLabel> sls = getAddedSuggestionLabels();
                int max = sls.size();

                if (max > 1) {//more than 1 suggestion
                    for (int i = 0; i < max; i++) {
                        SuggestionLabel sl = sls.get(i);
                        if (sl.isFocused()) {
                            if (lastFocusableIndex == max - 1) {
                                lastFocusableIndex = 0;
                                sl.setFocused(false);
                                autoSuggestionPopUpWindow.setVisible(false);
                                setFocusToTextField();
                                checkForAndShowSuggestions();//fire method as if document listener change occured and fired it

                            } else {
                                sl.setFocused(false);
                                lastFocusableIndex = i;
                            }
                        } else if (lastFocusableIndex <= i) {
                            if (i < max) {
                                sl.setFocused(true);
                                autoSuggestionPopUpWindow.toFront();
                                autoSuggestionPopUpWindow.requestFocusInWindow();
                                suggestionsPanel.requestFocusInWindow();
                                suggestionsPanel.getComponent(i).requestFocusInWindow();
                                lastFocusableIndex = i;
                                break;
                            }
                        }
                    }
                } else {//only a single suggestion was given
                    autoSuggestionPopUpWindow.setVisible(false);
                    setFocusToTextField();
                    checkForAndShowSuggestions();//fire method as if document listener change occured and fired it
                }
            }
        });
    }

    //view
    private void setFocusToTextField() {
        container.toFront();
        container.requestFocusInWindow();
        textField.requestFocusInWindow();
    }

    //view
    public ArrayList<SuggestionLabel> getAddedSuggestionLabels() {
        ArrayList<SuggestionLabel> sls = new ArrayList<>();
        for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
            if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
                SuggestionLabel sl = (SuggestionLabel) suggestionsPanel.getComponent(i);
                sls.add(sl);
                //System.out.println("in getAddedSuggestionLabels, line 208:\n sl: " + sl + "\n sls: " + sls);
            }
        }
        return sls;
    }

    //view or  maybe
    private void checkForAndShowSuggestions() {

        typedWord = getCurrentlyTypedWord();


        suggestionsPanel.removeAll();//remove previos words/jlabels that were added

        //used to calcualte size of JWindow as new Jlabels are added
        tW = 0;
        tH = 0;

        boolean added = wordTyped(typedWord);

        if (!added) {
            if (autoSuggestionPopUpWindow.isVisible()) {
                autoSuggestionPopUpWindow.setVisible(false);
                //System.out.println("In checkForAndShowSuggestion\n typedWord = " + typedWord);
            }
        } else {
            showPopUpWindow();
            setFocusToTextField();
        }
    }

    //view
    protected void addWordToSuggestions(String word) {
        //IMPORTANT
        SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this);

        calculatePopUpWindowSize(suggestionLabel);

        //System.out.println("In addWordToSuggestions\n word = " + word);

        suggestionsPanel.add(suggestionLabel);
    }

    //view
    public String getCurrentlyTypedWord() {//get newest word after last white spaceif any or the first word if no white spaces
        String text = textField.getText();
        String wordBeingTyped = "";
        if (text.contains(" ")) {
            int tmp = text.lastIndexOf(" ");
            if (tmp >= currentIndexOfSpace) {
                currentIndexOfSpace = tmp;
                wordBeingTyped = text.substring(text.lastIndexOf(" "));
            }
        } else {
            wordBeingTyped = text;
        }

        //Change to switch between TFIDF and Word Models
        //return wordBeingTyped.trim();
        return text;
    }


    private void calculatePopUpWindowSize(JLabel label) {
        //so we can size the JWindow correctly
        if (tW < label.getPreferredSize().width) {
            tW = label.getPreferredSize().width;
        }
        tH += label.getPreferredSize().height;
    }

    private void showPopUpWindow() {
        autoSuggestionPopUpWindow.getContentPane().add(suggestionsPanel);
        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
        autoSuggestionPopUpWindow.setSize(tW, tH);
        autoSuggestionPopUpWindow.setVisible(true);

        int windowX = 0;
        int windowY = 0;

        windowX = container.getX() + textField.getX() + 5;
        if (suggestionsPanel.getHeight() > autoSuggestionPopUpWindow.getMinimumSize().height) {
            windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getMinimumSize().height;
        } else {
            windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getHeight();
        }

        autoSuggestionPopUpWindow.setLocation(windowX, windowY);
        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
        autoSuggestionPopUpWindow.revalidate();
        autoSuggestionPopUpWindow.repaint();

    }

    public void setDictionary(ArrayList<String> words) {
        dictionary.clear();
        if (words == null) {
            return;//so we can call constructor with null value for dictionary without exception thrown
        }
        for (String word : words) {
            dictionary.add(word);
        }
    }

    public JWindow getAutoSuggestionPopUpWindow() {
        return autoSuggestionPopUpWindow;
    }

    public Window getContainer() {
        return container;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void addToDictionary(String word) {
        dictionary.add(word);
    }

    boolean wordTyped(String typedWord) {

        if (typedWord.isEmpty()) {
            return false;
        }
        //System.out.println("Typed word: " + typedWord);

        boolean suggestionAdded = false;
//FOR WORD  Model
//        for (String word : dictionary) {//get words in the dictionary which we added
//            boolean fullymatches = true;
//            if (typedWord.length() > 2) { //requires that 3 characters be typed
//                for (int i = 0; i < typedWord.length(); i++) {//each string in the word
//                    if (!typedWord.toLowerCase().startsWith(String.valueOf(word.toLowerCase().charAt(i)), i)) {//check for match
//                        fullymatches = false;
//                        break;
//                    }
//                }
//                if (fullymatches) {
//                    addWordToSuggestions(word);
//                    suggestionAdded = true;
//                }
//            }
//        }

        //FOR ENTIRE HYP controller
        //CONTROLLER SOOON

        ArrayList<String> res = theModel.calculateBestMatches(typedWord, 5);
        //System.out.println("In wordTyped: typedWord = " + typedWord);
        for (String hyp : res){
            addWordToSuggestions(hyp);
            suggestionAdded = true;
        }

        return suggestionAdded;
    }
}

//class SuggestionLabel extends JLabel {
//
//    private boolean focused = false;
//    private final JWindow autoSuggestionsPopUpWindow;
//    private final JTextField textField;
//    private final AutoSuggestor autoSuggestor;
//    private Color suggestionsTextColor, suggestionBorderColor;
//
//    //HERE
//    //TFIDFmodel theModel = new TFIDFmodel();
//    WordModel theModel = new WordModel();
//
//
//    //view
//    public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, AutoSuggestor autoSuggestor) {
//        super(string);
//
//        this.suggestionsTextColor = suggestionsTextColor;
//        this.autoSuggestor = autoSuggestor;
//        this.textField = autoSuggestor.getTextField();
//        this.suggestionBorderColor = borderColor;
//        this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();
//
//        initComponent();
//    }
//
//    private void initComponent() {
//        setFocusable(true);
//        setForeground(suggestionsTextColor);
//
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent me) {
//                super.mouseClicked(me);
//
//                replaceWithSuggestedText();
//
//                autoSuggestionsPopUpWindow.setVisible(false);
//            }
//        });
//
//        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
//        getActionMap().put("Enter released", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                replaceWithSuggestedText();
//                autoSuggestionsPopUpWindow.setVisible(false);
//            }
//        });
//    }
//
//    public void setFocused(boolean focused) {
//        if (focused) {
//            setBorder(new LineBorder(suggestionBorderColor));
//        } else {
//            setBorder(null);
//        }
//        repaint();
//        this.focused = focused;
//    }
//
//    public boolean isFocused() {
//        return focused;
//    }
//
//    //view
//    private void replaceWithSuggestedText() {
//
//        //Word Suggester
//        String suggestedWord = getText();
//        String text = textField.getText();
//        String typedWord = autoSuggestor.getCurrentlyTypedWord();//word model bug culprit
//        String t = text.substring(0, text.lastIndexOf(typedWord));
//        //String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
//        //String tmp = suggestedWord;
//
//        String tmp = theModel.getSuggested(suggestedWord, text, typedWord, t);
//        textField.setText(tmp + " ");
//        System.out.println("In replaceWithSuggestedText\n suggestedWord = " + suggestedWord + "\n text = " + text + "\n typedWord = " + typedWord + "\n t = " + t + "\n tmp = " + tmp);
//
//        //Hyp Suggester
////        String hyp = textField.getText();
////        String
//    }
//}
import java.util.ArrayList;

/**
 * Created by NickDesktop on 1/21/2016.
 */
public interface Suggestable {
    //make comments google javadoc
    public ArrayList<String> returnBestMatches(String userHyp, int numMatches);

    public String returnSuggested(String suggestedWord, String text, String typedWord, String t);

}

import java.util.ArrayList;

/**
 * Created by NickDesktop on 1/21/2016.
 */
public interface ISuggestor {
    //make comments google javadoc
    public ArrayList<String> calculateBestMatches(String userHyp, int numMatches);

    public String getSuggested(String suggestedWord, String text, String typedWord, String t);

}

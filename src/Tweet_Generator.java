
/*
 * Creator: Taylor Danielewski, Danielewski.taylor@gmail.com
 * References: (For Linking to Twitter) joe@javapapers.com, https://javapapers.com/core-java/post-to-twitter-using-java/
 */

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Tweet_Generator {

    public static void main(String[] args) throws InterruptedException, IOException, TwitterException {
        FileAndReader currentSession = new FileAndReader("Ratatouille Script.txt");
        String leadLine = "";
        sessionPlaceAndConcatonatedLine currentSessionAndLine = new sessionPlaceAndConcatonatedLine(0, "");
        while (!endOfMovie(leadLine)) {
            leadLine = currentSession.getReader().readLine().trim();
            currentSessionAndLine.incPlace();
            if (!leadLine.equals("")) {
                currentSessionAndLine.setConcatonatedLine(leadLine);
                if (endOfMovie(leadLine)) break;
                currentSessionAndLine = updateSessionAndPost(currentSessionAndLine, currentSession, findLastPlace());
                System.out.println(currentSessionAndLine.getConcatonatedLine());
            }
        }

        currentSession.getReader().close();
        setUpToPost("Fin.\nI hope you enjoyed Ratatouille!");
    }

    private static sessionPlaceAndConcatonatedLine updateSessionAndPost(sessionPlaceAndConcatonatedLine currentSessionAndLine, FileAndReader currentSession, int lastSessionPlace) throws IOException, InterruptedException, TwitterException {
        currentSessionAndLine = checkLengthAndConcat(currentSession.getReader(), currentSessionAndLine.getPlace(),
                currentSessionAndLine.getConcatonatedLine());
        checkPlaceAndPost(currentSessionAndLine.getPlace(), lastSessionPlace,
                currentSessionAndLine.getConcatonatedLine());
        return currentSessionAndLine;
    }

    private static boolean endOfMovie(String leadLine) {
        return leadLine.equals("END_OF_FILE_RATATOUILLE_2007");
    }

    private static sessionPlaceAndConcatonatedLine checkLengthAndConcat(BufferedReader scriptIn,
                                                                        int currentSessionPlace, String fullLine) throws IOException {
        String followLine = scriptIn.readLine().trim();
        currentSessionPlace++;
        if (!followLine.contentEquals("")) fullLine += " " + followLine;
        return new sessionPlaceAndConcatonatedLine(currentSessionPlace, fullLine);
    }

    private static void checkPlaceAndPost(int currentSessionPlace, int lastSessionPlace, String fullLine)
            throws IOException, InterruptedException, TwitterException {
        if (fullLine != null && currentSessionPlace >= lastSessionPlace + 1) {
            setUpToPost(fullLine);
            writeCurrentPlaceToFile(currentSessionPlace);
            TimeUnit.HOURS.sleep(2);
        }
    }

    private static void writeCurrentPlaceToFile(int currentSessionPlace) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("placeholder.txt"));
        out.write(Integer.toString(currentSessionPlace));
        out.close();
    }

    private static int findLastPlace() throws InterruptedException, IOException, TwitterException {
        FileAndReader lastSession = new FileAndReader("placeholder.txt");
        int lastSessionPlace = Integer.parseInt(lastSession.getReader().readLine());
        if (lastSessionPlace == 0) setUpToPost("Ratatouille (Film) 2007 ");
        lastSession.getReader().close();
        return lastSessionPlace;
    }

    private static void setUpToPost(String tweetToPost) throws TwitterException {
        // postTweet(new TwitterFactory().getInstance(), tweetToPost);
    }

    private static void postTweet(Twitter twitter, String tweetToPost) throws TwitterException {
        twitter.setOAuthAccessToken(new AccessToken(Keys.getAccessTokenStr(), Keys.getAccessTokenSecretStr()));
        twitter.updateStatus(tweetToPost);
    }
}
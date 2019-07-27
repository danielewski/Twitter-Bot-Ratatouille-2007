
/*
 * Creator: Taylor Danielewski, Danielewski.taylor@gmail.com
 * References: (For Linking to Twitter) joe@javapapers.com, https://javapapers.com/core-java/post-to-twitter-using-java/
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Tweet_Generator {

	// twitter keys and tokens
	public static void main(String[] args) throws InterruptedException, IOException {

		File ratatouilleScript = new File("Ratatouille Script.txt");
		BufferedReader scriptIn = new BufferedReader(new FileReader(ratatouilleScript));
		String leadLine = "";

		sessionPlaceAndConcatonatedLine currentSessionAndLine = new sessionPlaceAndConcatonatedLine(0, "");
		int lastSessionPlace = findLastPlace();
		while (!endOfMovie(leadLine)) {
			leadLine = scriptIn.readLine().trim();
			currentSessionAndLine.incPlace();

			if (!leadLine.equals("")) {
				currentSessionAndLine.setConcatonatedLine(leadLine);

				if (endOfMovie(leadLine)) {
					break;
				}

				currentSessionAndLine = checkLengthAndConcat(scriptIn, currentSessionAndLine.getPlace(),
						currentSessionAndLine.getConcatonatedLine());

				checkPlaceAndPost(currentSessionAndLine.getPlace(), lastSessionPlace,
						currentSessionAndLine.getConcatonatedLine());

			}
		}
		scriptIn.close();
		postTweet("Fin.\nI hope you enjoyed Ratatouille!");
	}

	private static boolean endOfMovie(String leadLine) {
		return leadLine.equals("END_OF_FILE_RATATOUILLE_2007");

	}

	private static sessionPlaceAndConcatonatedLine checkLengthAndConcat(BufferedReader scriptIn,
			int currentSessionPlace, String fullLine) throws IOException {
		String followLine = scriptIn.readLine().trim();
		currentSessionPlace++;
		if (!followLine.contentEquals("")) {
			fullLine += " " + followLine;
		}

		return new sessionPlaceAndConcatonatedLine(currentSessionPlace, fullLine);
	}

	private static void checkPlaceAndPost(int currentSessionPlace, int lastSessionPlace, String fullLine)
			throws IOException, InterruptedException {
		if (fullLine != null && currentSessionPlace >= lastSessionPlace + 1) {
			postTweet(fullLine);
			FileWriter fw = new FileWriter("placeholder.txt");
			BufferedWriter out = new BufferedWriter(fw);
			out.write(Integer.toString(currentSessionPlace));
			System.out.println("checkAndPost: " + fullLine + " " + Integer.toString(currentSessionPlace));
			out.close();

			TimeUnit.HOURS.sleep(2);
		}
	}

	private static int findLastPlace() throws InterruptedException, IOException {
		File lastSession = new File("placeholder.txt");
		BufferedReader findPlace = new BufferedReader(new FileReader(lastSession));
		int lastSessionPlace = Integer.parseInt(findPlace.readLine());
		if (lastSessionPlace == 0) {
			postTweet("Ratatouille (Film) 2007 ");
			TimeUnit.MINUTES.sleep(5);
		}
		findPlace.close();
		return lastSessionPlace;
	}

	public static void postTweet(String tweetToPost) {
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			AccessToken accessToken = new AccessToken(Keys.getAccessTokenStr(), Keys.getAccessTokenSecretStr());
			twitter.setOAuthAccessToken(accessToken);
			twitter.updateStatus(tweetToPost);
			System.out.println("Successfully updated the status on Twitter.");
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

}

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
	static String consumerKeyStr = "************************";
	static String consumerSecretStr = "************************";
	static String accessTokenStr = "************************";
	static String accessTokenSecretStr = "************************";

	
	public static void main(String[] args) throws InterruptedException, IOException {

		//BufferedReaders for reading in script and where the last tweet was
		File ratatouilleScript = new File("Ratatouille Script.txt");
		File lastSession = new File("placeholder.txt");
		BufferedReader findPlace = new BufferedReader(new FileReader(lastSession));
		BufferedReader scriptIn = new BufferedReader(new FileReader(ratatouilleScript));
		
		String leadLine = "";
		
		//Find where we left off last session
		int currentSessionPlace = 0;
		int lastSessionPlace = Integer.parseInt(findPlace.readLine());
		if (lastSessionPlace == 0) {
			postTweet("Ratatouille (Film) 2007 ");
			TimeUnit.MINUTES.sleep(5);
		}
		findPlace.close();
		
		
		while (!leadLine.equals("END_OF_FILE_RATATOUILLE_2007")) {
			leadLine = scriptIn.readLine().trim();
			currentSessionPlace++;
			String fullLine = null;

			if (!leadLine.equals("")) {
				fullLine = leadLine;

				if (leadLine.equals("END_OF_FILE_RATATOUILLE_2007")) {
					break;
				}

				//Check and see if the line is broken up into 2 parts, if so, concatenate the strings
				String followLine = scriptIn.readLine().trim();
				currentSessionPlace++;
				if (!followLine.contentEquals("")) {
					fullLine += " " + followLine;
				}
			}
			
			//Post the tweet if we have passed where the previous session left off
			if (fullLine != null && currentSessionPlace >= lastSessionPlace+1) {
				postTweet(fullLine);
				FileWriter fw = new FileWriter("placeholder.txt");
				BufferedWriter out = new BufferedWriter(fw);
				out.write(Integer.toString(currentSessionPlace));
				out.close();
				
				TimeUnit.HOURS.sleep(3);
			}


		}
		scriptIn.close();
		postTweet("Fin.\nI hope you enjoyed Ratatouille!");
	}

	//Post the tweet to twitter
	public static void postTweet(String tweetToPost) {
		try {
			Twitter twitter = new TwitterFactory().getInstance();

			AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);

			twitter.setOAuthAccessToken(accessToken);

			twitter.updateStatus(tweetToPost);

			System.out.println("Successfully updated the status in Twitter.");
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

}

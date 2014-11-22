package edu.virginia.rich.cs4720;

import android.util.Log;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterCall {
	Twitter twitter;
	TwitterFactory factory;
	AccessToken accessToken;
	
	public TwitterCall(String name1) throws TwitterException {
		factory = new TwitterFactory();
		twitter = factory.getInstance();
		
		twitter.setOAuthConsumer("BkTlpKgcTq3ch0GCFXuWINfSb", "8L5SrnNJLW9PKJlY6ljsyiKlx8aD0NjQdwEuQYU2jmM0hu9Buu");
		accessToken = new AccessToken("2882725972-R9xtrq7QZkO4m2uz6ZMq4pb6sZCDLisjIw3ccap",
				"wUkesEZKZ7fp89vQWtnJ45ohwpkNobX2SYdrulgQDBTnP");
		twitter.setOAuthAccessToken(accessToken);
		twitter.updateStatus(name1 + " just won a game of Raspberry Riot!");
	}
}
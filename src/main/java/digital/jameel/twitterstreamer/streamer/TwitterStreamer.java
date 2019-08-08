package digital.jameel.twitterstreamer.streamer;

import digital.jameel.twitterstreamer.Config;
import digital.jameel.twitterstreamer.Main;
import org.apache.log4j.Logger;
import twitter4j.*;

import java.util.List;

public class TwitterStreamer {

    private Config config;
    private OutputStreamer outputStreamer;
    private static final org.apache.log4j.Logger log = Logger.getLogger(TwitterStreamer.class);

    public TwitterStreamer(Config config, OutputStreamer streamer){
        this.config = config;
        this.outputStreamer = streamer;
    }

    public void search(String searchTerm, String language) throws InterruptedException{
        TwitterFactory factory = new TwitterFactory(this.config.getConfigurationBuilder().build());
        Twitter twitter = factory.getInstance();
        try {
            Query query = new Query(searchTerm);
            query.setLang(language);
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    TwitterStreamerPutRequest putRequest = new TwitterStreamerPutRequest(searchTerm, tweet);
                    this.outputStreamer.addRequest(putRequest);
                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                }
            System.out.println("Sleeping for 10 seconds");
            Thread.sleep(10 * 1000);
            } while ((query = result.nextQuery()) != null);
        } catch (TwitterException te) {
            System.out.println("Failed to search tweets: " + te.getMessage());
            te.printStackTrace();
            System.exit(-1);
        }
    }

    public void search(String searchTerm) throws InterruptedException{
        this.search(searchTerm, "en");
    }
}

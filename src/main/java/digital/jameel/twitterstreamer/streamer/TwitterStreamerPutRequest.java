package digital.jameel.twitterstreamer.streamer;

import com.google.gson.Gson;
import twitter4j.Status;

import java.util.Date;

public class TwitterStreamerPutRequest implements PutRequest {

    private String twitterAuthor;
    private String twitterMessage;
    private String subject;
    private long createdAt;
    private int favouriteCount;

    public TwitterStreamerPutRequest(String subject, String twitterAuthor, String twitterMessage){
        this.twitterAuthor = twitterAuthor;
        this.twitterMessage = twitterMessage;
        this.subject = subject;
    }

    public TwitterStreamerPutRequest(String subject, Status tweet){
        this(subject, tweet.getUser().getScreenName(), tweet.getText());
        this.createdAt = tweet.getCreatedAt().getTime();
        this.favouriteCount = tweet.getFavoriteCount();
    }

    /**
     * Convert the request to Bytes for consumption for a Kinesis stream
     * @return byte[] resulting bytes
     */
    public byte[] getBytesData(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json.getBytes();
    }

    /**
     * Convert this request to a JSON String
     * @return String resulting json
     */
    public String getJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}

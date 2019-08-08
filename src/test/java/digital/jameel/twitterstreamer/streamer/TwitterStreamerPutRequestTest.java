package digital.jameel.twitterstreamer.streamer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TwitterStreamerPutRequestTest {

    private static TwitterStreamerPutRequest request;
    private static String author;
    private static String message;

    @BeforeAll
    public static void setup(){
        author = "Test Author";
        message = "Test Message";
        request = new TwitterStreamerPutRequest("#SpiderManFarFromHome", author,message);
    }

    @Test
    public void getBytesDataIsValid(){
        byte[] data = request.getBytesData();
        String rawJs = new String(data);
        this.isValidJson(rawJs);
    }

    @Test
    public void getJsonIsValid(){
        String rawJs = request.getJson();
        this.isValidJson(rawJs);
    }

    private void isValidJson(String rawJs){
        JsonObject jsonObject = new JsonParser().parse(rawJs).getAsJsonObject();
        assert jsonObject.isJsonObject();
        assert jsonObject.get("twitterAuthor").getAsString().equals(this.author);
        assert jsonObject.get("twitterMessage").getAsString().equals(this.message);
    }
}

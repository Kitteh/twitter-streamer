package digital.jameel.twitterstreamer;

import digital.jameel.twitterstreamer.streamer.KinesisStreamer;
import digital.jameel.twitterstreamer.streamer.OutputStreamer;
import digital.jameel.twitterstreamer.streamer.TwitterStreamer;
import org.apache.log4j.Logger;

public class Main {

    //private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException{

        String searchTerm = System.getenv("TWITTERSTREAM_SEARCHTERM");
        if (searchTerm == null || searchTerm == ""){
            System.out.println("Missing Search Term env TWITTERSTREAM_SEARCHTERM");
        }
        System.out.println("Started twitter stream");
        try{
            Config config = new Config();
            OutputStreamer outputStreamer = new KinesisStreamer(config);
            TwitterStreamer streamer = new TwitterStreamer(config, outputStreamer);
            System.out.println("Searcing for : " + searchTerm);
            streamer.search(searchTerm);
        } catch (Exception e){
            System.out.println("Exception occured:" + e.getMessage());
            System.out.println(e.getStackTrace().toString());
        }
    }
}

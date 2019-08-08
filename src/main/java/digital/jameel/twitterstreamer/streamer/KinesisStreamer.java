package digital.jameel.twitterstreamer.streamer;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.Record;
import com.google.gson.Gson;
import digital.jameel.twitterstreamer.Config;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class KinesisStreamer implements OutputStreamer {

    private Config config;
    private AmazonKinesisFirehose amazonKinesis;
    private ArrayList<PutRequest> requests;

    private final int batchLimit = 50;

    public KinesisStreamer(Config config){
        this.config = config;
        this.requests = new ArrayList<PutRequest>();
        init();
    }

    public void addRequest(PutRequest message){
        this.requests.add(message);

        if (this.requests.size() >= this.batchLimit){
            this.syncRequests();
        }
    }

    protected void syncRequests(){
        Gson gson = new Gson();
        String json = gson.toJson(this.requests) + "\n"; // New Line to delimit batches
        byte[] bytes = json.getBytes();
        Record record = new Record().withData(ByteBuffer.wrap(bytes));
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(this.config.getStreamName());
        putRecordRequest.setRecord(record);
        this.amazonKinesis.putRecord(putRecordRequest);
        System.exit(0);
    }

    /*
    private void syncRequestsBatch(){
        PutRecordBatchRequest putRecordBatchRequest = new PutRecordBatchRequest();
        putRecordBatchRequest.setDeliveryStreamName(this.config.getStreamName());
        putRecordBatchRequest.setRecords(this.recordList);
        this.amazonKinesis.putRecordBatch(putRecordBatchRequest);
        System.exit(0);
    }
    */

    private void init(){
        this.amazonKinesis = AmazonKinesisFirehoseClientBuilder.standard()
                    .withRegion(this.config.getRegion())
                    .build();
    }
}

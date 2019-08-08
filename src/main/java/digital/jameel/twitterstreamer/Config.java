package digital.jameel.twitterstreamer;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import twitter4j.conf.ConfigurationBuilder;

public class Config {

    private AWSSimpleSystemsManagement client;

    public Config(){
        init();
    }

    public void init(){
        this.client = AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

    public ConfigurationBuilder getConfigurationBuilder(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        String consumerKey = this.getParameterString("twitterapi.consumerkey");
        String consumerSecret = this.getParameterString("twitterapi.consumersecret");
        String accessToken = this.getParameterString("twitterapi.accesstoken");
        String accessSecret = this.getParameterString("twitterapi.accesssecret");
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessSecret);
        return cb;
    }

    public String getStreamName(){
        return this.getParameterString("twitterapi.streamname");
    }

    public Regions getRegion(){
        return Regions.EU_WEST_1;
    }

    protected Parameter getParameter(String parameterName){
        GetParameterRequest request = new GetParameterRequest();
        request.withName(parameterName).setWithDecryption(Boolean.TRUE);
        GetParameterResult parameterResult = this.client.getParameter(request);
        return parameterResult.getParameter();
    }

    protected String getParameterString(String parameterName){
        return this.getParameter(parameterName).getValue();
    }

}

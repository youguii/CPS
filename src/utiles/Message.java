package utiles;
import java.io.Serializable;

import interfaces.MessageI;

public class Message implements MessageI {

    protected String uri;
    protected TimeStamp ts;
    protected Properties properties;
    protected Serializable content;
    
    public Message(String uri, TimeStamp ts, Properties properties, Serializable content) {
        this.uri = uri;
        this.ts = ts;
        this.properties = properties;
        this.content = content;
    }
    
    @Override
    public String getURI() throws Exception {
        return uri;
    }

    @Override
    public TimeStamp getTimeStamp() throws Exception {
        return ts;
    }

    @Override
    public Properties getProperties() throws Exception {
        return properties;
    }

    @Override
    public Serializable getPayload() throws Exception {
        return content;
    }

}



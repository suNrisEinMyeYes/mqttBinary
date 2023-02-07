package mqttlib.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

public class PublishMessage extends MqttMessage
{


    private String topic;
    private String payload;
    private int messageId;



    public PublishMessage() {
        super();
        super.setMessageType(MessageType.PUBLISH);
        this.messageId = 553; //should come from a sofisticated object that makes ids available and unavailable based on puback acknowledgments..
    }
    public String getTopic()
    {
        return this.topic;
    }
    public void setTopic(final String topic)
    {
        this.topic = topic;
    }
    public String getPayload()
    {
        return this.payload;
    }
    public void setPayload(final String payload)
    {
        this.payload = payload;
    }
    public int getMessageId()
    {
        return this.messageId;
    }
    public void setMessageId(final int messageId)
    {
        this.messageId = messageId;
    }
    @Override
    public String toString()
    {
        return "PublishMessage [topic=" + this.topic + ", payload=" + this.payload + " " + super.toString() +"]";
    }
    @Override
    public void writeTo(final WritableByteChannel channel) throws IOException
    {
        final BufferByteChannel buffer = new BufferByteChannel();

        new MqttSendableStringList(Arrays.asList(this.topic)).writeTo(buffer);

        if(getQos() > 0) {
            buffer.write(ByteBuffer.wrap(new byte[] {BitUtils.getByteOfRank(this.messageId, 1),BitUtils.getByteOfRank(this.messageId, 0)}));
        }
        new MqttSendableStringList(Arrays.asList(this.payload)).writeTo(buffer);

        super.writeTo(channel, buffer.getLength());

        buffer.flushTo(channel);
    }



    @Override
    public void initialise(final ReadableByteChannel byteChannel, final int remainingLength) throws IOException
    {
        throw new RuntimeException("not implemented"); // not needed for 6.2
    }

}
package mqttlib.binary;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class ConnackMessage extends MqttMessage
{

    private ConnackResponses response;

    public ConnackMessage() {
        super.setMessageType(MessageType.CONNACK);
    }


    public ConnackResponses getResponse()
    {
        return this.response;
    }

    public void setResponse(final ConnackResponses response)
    {
        this.response = response;
    }

    @Override
    public void writeTo(final WritableByteChannel channel) throws IOException
    {
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public void initialise(final ReadableByteChannel byteChannel, final int remainingLength) throws IOException
    {
        final int v = BitUtils.readByte(byteChannel);
        setResponse(ConnackResponses.getResp(v));
    }
}

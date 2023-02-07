package mqttlib.binary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class MqttSendableStringList implements Sendable
{
    private final List<byte[]> toBeSent;

    public MqttSendableStringList(final Iterable<String> strs)
    {
        super();

        this.toBeSent = new ArrayList<>();
        for(final String str : strs) {
            byte[] utfBytes;
            try
            {
                utfBytes = str.getBytes("UTF-8");
            } catch (final UnsupportedEncodingException e)
            {
                throw new RuntimeException("MQTT won't work without UTF-8 encryption");
            }
            final int length = utfBytes.length;
            final byte[] twoBytesOfLength = new byte[] {(byte)(length/128),(byte)length};
            this.toBeSent.add(twoBytesOfLength);
            this.toBeSent.add(utfBytes);
        }
    }

    @Override
    public void writeTo(final WritableByteChannel channel) throws IOException
    {
        for(final byte[] x : this.toBeSent) {
            channel.write(ByteBuffer.wrap(x));
        }
    }
}

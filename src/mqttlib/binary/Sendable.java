package mqttlib.binary;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public interface Sendable
{
    public void writeTo(WritableByteChannel channel) throws IOException;
}

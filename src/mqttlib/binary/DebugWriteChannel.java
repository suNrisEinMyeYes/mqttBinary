package mqttlib.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class DebugWriteChannel implements WritableByteChannel
{

    public DebugWriteChannel() {
    }

    @Override
    public boolean isOpen()
    {
        return true;
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public int write(final ByteBuffer src) throws IOException
    {
        final int v = src.remaining();
        while(src.hasRemaining()) {
            final byte b = src.get();
            System.out.println(BitUtils.getDebugStr(b));
        }
        return v;
    }

}

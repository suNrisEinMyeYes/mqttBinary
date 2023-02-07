package mqttlib.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class BufferByteChannel implements WritableByteChannel
{
    private final List<ByteBuffer> toWrite;
    private int length = 0;

    public BufferByteChannel() {
        this.toWrite = new ArrayList<>();
        this.length = 0;
    }

    public int getLength() {
        return this.length;
    }

    @Override
    public boolean isOpen()
    {
        return true;
    }

    @Override
    public void close()
    {
        throw new RuntimeException("This stream cannot be closed");
    }

    @Override
    public int write(final ByteBuffer src) throws IOException
    {
        final int remaining = src.remaining();
        this.length += remaining;
        this.toWrite.add(src);
        return remaining;
    }

    public void flushTo(final WritableByteChannel channel) throws IOException {
        for(final ByteBuffer b : this.toWrite) {
            channel.write(b);
        }
    }

}

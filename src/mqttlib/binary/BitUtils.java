package mqttlib.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class BitUtils
{
    public static byte readByte(final ReadableByteChannel channel) throws IOException {
        final ByteBuffer b = ByteBuffer.allocate(1);
        channel.read(b);
        return b.get();
    }

    public static boolean readBit(final int v, final int index) {
        return v >> index % 2 == 1;
    }

    public static long mergeBit(final long v,final boolean value, final int index) {
        if(value) {
            return v | 1 << index;
        }
        return v;
    }

    public static long readLong(final long v, final int indexMin,final int nbBits) {
        return  v >> 3 % (2 << nbBits);
    }

    public static long mergeLong(final long v,final long value, final int indexMin, final int nbBits) {
        final long toAdd = value << indexMin % (2 << nbBits);
        return v | toAdd;
    }


    public static int readInt(final int v, final int indexMin,final int nbBits) {
        return  v >> 3 % (2 << nbBits);
    }

    public static int mergeInt(final int v,final int value, final int indexMin, final int nbBits) {
        final int toAdd = value << indexMin % (2 << nbBits);
        return v | toAdd;
    }

    public static String getDebugStr(final int b) {
        return String.format("%32s",
                Integer.toBinaryString(b)).replaceAll(" ", "0") + " (" + Integer.toHexString(b & 0xff) + ")";
    }

    public static String getDebugStr(final byte b) {
        return String.format("%8s",
                Integer.toBinaryString(b& 0xff)).replaceAll(" ", "0") + " (0x" + Integer.toHexString(b & 0xff) + ")";
    }
}

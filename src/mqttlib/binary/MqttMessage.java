package mqttlib.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

public abstract class MqttMessage implements Sendable
{
    private MessageType messageType;
    private boolean dup;
    private int qos;
    private boolean retain;

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public byte getControllHeader() {
        final int messageType = this.messageType.getValue() << 4 ;
        final int retain = this.retain ? 1 : 0;
        final int qos = this.qos <<1;
        final int dup = this.dup ? 1 : 0 <<3;
        return (byte)(messageType | retain | qos | dup);
    }

    public byte[] getPacketLength(final int packetLength) {
        final List<Integer> splittedLength = new ArrayList<>();
        int v = packetLength;

        do {
            final int r = v % 128;
            splittedLength.add(r);
            v = v/128;
        }
        while(v > 0);

        final byte[] ret = new byte[splittedLength.size()];
        for(int i = 0; i < ret.length; i ++) {
            int byteV;
            if(i == ret.length-1) {
                byteV = splittedLength.get(i);
            } else {

                byteV = splittedLength.get(i) | 1 << 7;
            }
            ret[i] = (byte) byteV;
        }
        return ret;
    }

    public void ensureHeaderToZeros() {
        setDup(false);
        setQos(0);
        setRetain(false);
    }

    public void setMessageType(final MessageType messageType)
    {
        this.messageType = messageType;
    }
    public boolean isDup()
    {
        return this.dup;
    }
    public void setDup(final boolean dup)
    {
        this.dup = dup;
    }
    public int getQos()
    {
        return this.qos;
    }
    public void setQos(final int qos)
    {
        this.qos = qos;
    }
    public boolean isRetain()
    {
        return this.retain;
    }
    public void setRetain(final boolean retain)
    {
        this.retain = retain;
    }

    public void writeTo(final WritableByteChannel channel, final int remainingLength) throws IOException
    {
        final byte[] packetLength = getPacketLength(remainingLength);
        final byte header = getControllHeader();
        channel.write(ByteBuffer.wrap(new byte[] {header}));
        channel.write(ByteBuffer.wrap(packetLength));
    }

    public static MqttMessage readMessage(final ReadableByteChannel byteChannel) throws IOException {
        int v = BitUtils.readByte(byteChannel);
        final int messageType = BitUtils.readInt(v, 4, 4);
        final MqttMessage msg = MessageType.instantiate(messageType);
        msg.setQos(BitUtils.readInt(v, 1, 2));
        msg.setRetain(BitUtils.readBit(v, 0));
        msg.setDup(BitUtils.readBit(v, 3));

        int length = 0;
        int dec = 0;
        do {

            v = BitUtils.readByte(byteChannel);
            length = length + BitUtils.readInt(v, 0, 7) << dec;
            dec += 7;
        }
        while(BitUtils.readBit(v, 0));
        msg.initialise(byteChannel, length);
        return msg;
    }

    public abstract void initialise(final ReadableByteChannel byteChannel, int remainingLength) throws IOException;

}

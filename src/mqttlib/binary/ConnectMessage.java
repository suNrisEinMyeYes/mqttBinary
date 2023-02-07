package mqttlib.binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectMessage extends MqttMessage
{
    private boolean cleanSession;
    private boolean hasWill;
    private int willQos;
    private boolean willRetain;
    private boolean password;
    private boolean username;
    private int keepAlive;
    private final String protocolName = "MQTT";
    private final int protocolVersion;

    private final List<String> stringContent;
    private final MqttSendableStringList sendableStringList;

    public ConnectMessage(final String clientName) {
        super();
        this.cleanSession = true;
        this.hasWill = false;
        this.willQos = 0;
        this.willRetain = false;
        this.password = false;
        this.username = false;
        this.keepAlive = 60;
        this.protocolVersion = 4;
        this.stringContent = new ArrayList<>();
        this.stringContent.add(clientName);
        this.sendableStringList = new MqttSendableStringList(this.stringContent);
        super.setMessageType(MessageType.CONNECT);
        super.ensureHeaderToZeros();
    }

    public byte getConnectFlags() {
        int v = 0;
        if(this.cleanSession) {
            v = v | 1 <<1;
        }
        if(this.hasWill) {
            v = v | 1 <<2;
        }
        v = v | this.willQos << 3;
        if(this.willRetain) {
            v = v | 1 <<5;
        }
        if(this.password) {
            v = v | 1 <<6;
        }
        if(this.username) {
            v = v | 1 <<7;
        }
        return (byte)v;
    }

    public String getClientName()
    {
        return this.stringContent.get(0);
    }

    public void setClientName(final String clientName)
    {
        this.stringContent.set(0, clientName);
    }

    @Override
    public void writeTo(final WritableByteChannel channel) throws IOException
    {
        final byte b1 = (byte)(this.keepAlive/256);
        final byte b2 = (byte)this.keepAlive;
        final byte[] b = new byte[] {(byte)this.protocolVersion,getConnectFlags(),b1,b2};
        final BufferByteChannel buffer = new BufferByteChannel();

        //variable header start
        new MqttSendableStringList(Arrays.asList(this.protocolName)).writeTo(buffer);
        buffer.write(ByteBuffer.wrap(b));
        //payload :
        new MqttSendableStringList(this.stringContent).writeTo(buffer);

        //fixed header and remaining length
        super.writeTo(channel, buffer.getLength());

        //everything else
        buffer.flushTo(channel);
    }

    public int getKeepAlive()
    {
        return this.keepAlive;
    }
    public void setKeepAlive(final int keepAlive)
    {
        this.keepAlive = keepAlive;
    }
    public boolean isCleanSession()
    {
        return this.cleanSession;
    }
    public void setCleanSession(final boolean cleanSession)
    {
        this.cleanSession = cleanSession;
    }
    public boolean isHasWill()
    {
        return this.hasWill;
    }
    public void setHasWill(final boolean hasWill)
    {
        this.hasWill = hasWill;
    }
    public int getWillQos()
    {
        return this.willQos;
    }
    public void setWillQos(final int willQos)
    {
        this.willQos = willQos;
    }
    public boolean isWillRetain()
    {
        return this.willRetain;
    }
    public void setWillRetain(final boolean willRetain)
    {
        this.willRetain = willRetain;
    }
    public boolean isPassword()
    {
        return this.password;
    }
    public void setPassword(final boolean password)
    {
        this.password = password;
    }
    public boolean isUsername()
    {
        return this.username;
    }
    public void setUsername(final boolean username)
    {
        this.username = username;
    }

    @Override
    public void initialise(final ReadableByteChannel byteChannel, final int remainingLength) throws IOException
    {
        throw new RuntimeException("Unimplemented");
    }


}

package mqttlib.binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class Main {

    public static void main(final String[] args) {
        try {
            Main.doWithConnection();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void doWithConnection() throws IOException {
        final InetAddress adress = InetAddress.getByName("localhost");
        final Socket socket = new Socket(adress, 1883);
        final WritableByteChannel channel = Channels.newChannel(socket.getOutputStream());

        final ConnectMessage connectMsg = Main.doQuestion1();
        connectMsg.writeTo(channel);
        System.out.println("Connect message sent : ");
        System.out.println(connectMsg);

        final PublishMessage publishMsg = Main.doQuestion2();
        publishMsg.writeTo(channel);
        System.out.println("Publish message sent : ");
        System.out.println(publishMsg);
        publishMsg.writeTo(consoleWriter);
        
        socket.close();
    }

    // connect msg
    public static ConnectMessage doQuestion1() throws IOException {
        final ConnectMessage message = new ConnectMessage("python1");
        message.setKeepAlive(60);
        return message;
    }

    // publish msg
    public static PublishMessage doQuestion2() throws IOException {
        final PublishMessage message = new PublishMessage();
        message.setTopic("topic/3");
        message.setPayload("kovalenko");
        message.setQos(1);
        return message;
    }
}
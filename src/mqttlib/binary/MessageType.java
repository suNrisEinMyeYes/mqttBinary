package mqttlib.binary;

public enum MessageType
{
    CONNECT(1),
    CONNACK(2),
    PUBLISH(3),
    PUBACK(4),
    SUBSCRIBE(8),
    SUBACK(9),
    DISCONNECT(14),
    ;


    private final int value;

    private MessageType(final int v) {
        this.value = v;
    }

    public int getValue() {
        return this.value;
    }

    public static MqttMessage instantiate(final int type) {
        switch(type) {
        case 2 :
            return new ConnackMessage();
        default:
            throw new RuntimeException("Uninstantiable message type " + type);
        }
    }
}

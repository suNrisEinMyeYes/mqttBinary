package mqttlib.binary;

public enum ConnackResponses
{
    ACCEPTED(0,"Connection Accepted"),
    REFUSED_PROTV(1,"Connection Refused: unacceptable protocol version"),
    REFUSED_ID(2,"Connection Refused: identifier rejected"),
    REFUSED_SERV(3,"Connection Refused: server unavailable"),
    REFUSED_USRN_OR_PWD(4,"Connection Refused: bad user name of password"),
    REFUSED_NOT_AUTHORIZED(5,"Connection Refused: not authorized");

    private int id;
    private String descr;


    private ConnackResponses(final int id,final String desc) {
        this.id = id;
        this.descr = desc;
    }

    public static ConnackResponses getResp(final int id) {
        switch(id) {
        case 0 :
            return ACCEPTED;
        case 1 :
            return REFUSED_PROTV;
        case 2 :
            return REFUSED_ID;
        case 3 :
            return REFUSED_SERV;
        case 4 :
            return REFUSED_USRN_OR_PWD;
        case 5 :
            return REFUSED_NOT_AUTHORIZED;
        default :
            return null;
        }
    }

    public String getDescr() {
        return this.descr;
    }

    public int getId() {
        return this.id;
    }
}

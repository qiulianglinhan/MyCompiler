package common;

public class Num extends Token {

    private final int value;
    private boolean init;

    public Num(int type, String content, int value, boolean init) {
        super(type, content);
        this.value = value;
        this.init = init;
    }

    public int getValue() {
        return value;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}

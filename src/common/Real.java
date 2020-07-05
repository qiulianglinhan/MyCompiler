package common;

public class Real extends Token{

    private final double value;
    private boolean init;

    public Real(int type, String content, double value,boolean init) {
        super(type, content);
        this.value = value;
        this.init = init;
    }

    public double getValue() {
        return value;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}

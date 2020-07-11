package common;

import java.util.Objects;

public class Num extends Token {

    private int value;
    private boolean init;

    @Deprecated
    public Num(int type, String content, int value, boolean init) {
        super(type, content);
        this.value = value;
        this.init = init;
    }

    public Num(int type, String content,int line ,int value, boolean init) {
        super(type, content,line);
        this.value = value;
        this.init = init;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Num num = (Num) o;
        return value == num.value &&
                init == num.init;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, init);
    }
}

package common;

import java.util.Objects;

public class Real extends Token{

    private double value;
    private boolean init;

    @Deprecated
    public Real(int type, String content, double value,boolean init) {
        super(type, content);
        this.value = value;
        this.init = init;
    }

    public Real(int type, String content,int line, double value,boolean init) {
        super(type, content,line);
        this.value = value;
        this.init = init;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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
        Real real = (Real) o;
        return Double.compare(real.value, value) == 0 &&
                init == real.init;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, init);
    }
}

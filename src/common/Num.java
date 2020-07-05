package common;

public class Num extends Token {

    private final int value;

    public Num(int type, String content, int value) {
        super(type, content);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

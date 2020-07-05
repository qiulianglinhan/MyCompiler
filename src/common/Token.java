package common;

public class Token {
    private final int type;
    private final String content;

    public Token(int type,String content){
        this.type = type;
        this.content = content;
    }

    @Override
    public String toString() {
        return "("+type+","+content+")";
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}

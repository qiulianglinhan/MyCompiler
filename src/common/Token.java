package common;

import java.util.Objects;

/**
 * 词法单元 Token
 * 以前未设置行号，导致在错误处理时不知道行号无法提示
 * 未用行号构造器已废弃，为暂时为使代码可以运行，暂时保留
 */
public class Token {
    private final int type;
    private final String content;
    private int line = -1;      // -1 means may not initialize

    @Deprecated
    public Token(int type,String content){
        this.type = type;
        this.content = content;
    }

    public Token(int type,String content,int line){
        this.type = type;
        assert content != null;
        this.content = content;
        this.line = line;
    }

    @Override
    public String toString() {
        return "("+type+","+content+","+line+")";
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getLine() {
        return line;
    }

    // 可能需要判定是否是同一 token，重写 equals 和 hashCode 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type &&
                line == token.line &&
                content.equals(token.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content, line);
    }
}

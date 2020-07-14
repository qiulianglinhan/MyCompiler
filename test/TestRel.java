import common.Tag;
import common.Token;
import inter.departed.Inter;
import inter.departed.Rel;

import java.util.ArrayList;

public class TestRel {

    public static void main(String[] args) {
        new Inter();
        Token token1 = new Token(Tag.NUMINT,"23");
        Token token2 = new Token(Tag.GT,">");
        Token token3 = new Token(Tag.NUMINT,"10");
        ArrayList<Token> arrayList = new ArrayList<>();
        arrayList.add(token1);
        arrayList.add(token2);
        arrayList.add(token3);
        Rel rel = new Rel(arrayList);
        rel.gen();
    }

}
/*
(401,23)
(307,">")
(401,10)
 */
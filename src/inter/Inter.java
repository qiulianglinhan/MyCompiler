package inter;

import common.Tag;
import common.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inter {

    public static Map<String, Token> declaration;
    public static ArrayList<Integer> tagArrayList;
    public static Map<Integer,String> tag2symbol;
    public static Map<Integer,String> tag2low;

    public Inter(){
        declaration = new HashMap<>();
        tagArrayList = new ArrayList<>();
        tag2symbol = new HashMap<>();
        tag2low = new HashMap<>();
        tagArrayList.add(Tag.EQ);tagArrayList.add(Tag.GREATER);tagArrayList.add(Tag.LOWER);
        tagArrayList.add(Tag.GE);tagArrayList.add(Tag.LE);tagArrayList.add(Tag.EE);
        tagArrayList.add(Tag.NE);
        tag2symbol.put(Tag.EQ,"=");tag2symbol.put(Tag.GREATER,">");tag2symbol.put(Tag.LOWER,"<");
        tag2symbol.put(Tag.GE,">=");tag2symbol.put(Tag.LE,"<=");tag2symbol.put(Tag.EE,"==");
        tag2symbol.put(Tag.NE,"!=");
        tag2low.put(Tag.EQ,"=");tag2low.put(Tag.GREATER,"gt");tag2low.put(Tag.LOWER,"lw");
        tag2low.put(Tag.GE,"ge");tag2low.put(Tag.LE,"le");tag2low.put(Tag.EE,"eq");
        tag2low.put(Tag.NE,"ne");
    }

}
/*
EQ = 306,GREATER = 307, LOWER = 308, GE = 309, LE = 310, EE = 311, NE = 312
 */
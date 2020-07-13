import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
//        System.out.println(symbol.KEYWORDS.contains("ll"));
//        System.out.println(symbol.maps.get(">>"));
//        int a = 1;
//        switch (a){
//            case -1:
//                System.out.println("-1");
//                break;
//            case -2:
//                System.out.println("-2");
//                break;
//            default:
//                System.out.println("-1");
//        }
        Object a = 1;
        Object b = "aaa";
        System.out.println(a+" "+a.getClass());
        System.out.println(b+" "+b.getClass());

        String s = "goto 29";
        String[] ss = s.split(" ");
        for (String s1 : ss) {
            System.out.println(s1);
        }
    }
}

class symbol{
    static Set<String> KEYWORDS = new HashSet<>();
    static Map<String,Integer> maps = new HashMap<>();
    static {
        KEYWORDS.add("int");KEYWORDS.add("double");KEYWORDS.add("main");KEYWORDS.add("if");
        KEYWORDS.add("else");KEYWORDS.add("for");KEYWORDS.add("while");KEYWORDS.add("return");
        KEYWORDS.add("void");KEYWORDS.add("char");

        maps.put(">=",444);
    }
}
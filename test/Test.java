import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        System.out.println(symbol.KEYWORDS.contains("ll"));
        System.out.println(symbol.maps.get(">>"));
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
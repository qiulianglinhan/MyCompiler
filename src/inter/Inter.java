package inter;

import common.Token;

import java.util.HashMap;
import java.util.Map;

public class Inter {

    Map<String, Token> declaration;

    public Inter(){
        declaration = new HashMap<>();
    }

    public Map<String, Token> getDeclaration() {
        return declaration;
    }
}

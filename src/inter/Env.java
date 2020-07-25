package inter;

import java.util.Stack;

/**
 * 保存上下文环境
 */
public class Env {
    public static Stack<AllIdMapAndArrayMap> envStack;

    static {
        envStack = new Stack<>();
    }
}

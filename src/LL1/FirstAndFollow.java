package LL1;

import java.util.*;

/**文法
 * G[P]:
 P→S|Q|;
 S→V=E;
 E→TR
 R→ATR|$
 T→FY
 Y→MFY|$
 F→CZ
 C→BI
 Z→OCZ|$
 I→XBI|$
 B→(E)|i

 A→+|-
 M→*|/
 X→a|o           //a表示逻辑符号&&，o表示逻辑符号||
 O→t|d|g|l|u|e   //t表示>=，d表示<=，g表示>,l表示<,e表示==，u表示!=
 V→i             //i表示id
 Q→8JKH          //8表示if在符号表中序号
 H→fJKH|9K|$     //f 表示 else if符号的组合、9表示else在符号表中的序号
 J→(E)           //逻辑语句
 K→S|{U}|;       //if语句程序体
 U→PU|{U}U|$

 * */

public class FirstAndFollow {
    // 终结符
    private List<Character> Vt = new ArrayList<>();
    // 非终结符
    private List<Character> Vn = new ArrayList<>();
    // ε 空标识符
    private static final Character EPISON = '$';
    // 开始
    private static final Character START = 'P';
    // 终止符号
    private static Character END = '#';
    // 存放文法
    private Map<Character, String> grammar = new HashMap<>();
    // First集
    private Map<Character, Set<Character>> firstSet = new HashMap<>();
    // Follow集
    private Map<Character, Set<Character>> followSet = new HashMap<>();
    //产生式的First集
    private Map<String, Set<Character>> productionFirstSet = new HashMap<>();

    public List<Character> getVt() {
        return Vt;
    }

    public List<Character> getVn() {
        return Vn;
    }

    public static Character getEPISON() {
        return EPISON;
    }

    public static Character getSTART() {
        return START;
    }

    public static Character getEND() {
        return END;
    }

    public Map<Character, Set<Character>> getFirstSet() {
        return firstSet;
    }

    public Map<Character, Set<Character>> getFollowSet() {
        return followSet;
    }

    public Map<String, Set<Character>> getProductionFirstSet() {
        return productionFirstSet;
    }

    public FirstAndFollow(){
        init();
    }

    public FirstAndFollow(boolean verbose) {
        init();
        if (verbose){
            // display first set
            displayFirstSet();
            // display follow set
            displayFollowSet();
        }

    }

    /**
     * 初始化文法
     */
    private void init(){
        //添加非终结符号
        Vn.add(START);

        Vn.add('Q');
        Vn.add('J');
        Vn.add('K');
        Vn.add('H');

        Vn.add('S');
        Vn.add('V');
        Vn.add('E');
        Vn.add('R');
        Vn.add('T');
        Vn.add('Y');
        Vn.add('F');
        Vn.add('A');
        Vn.add('M');
        Vn.add('Z');
        Vn.add('C');
        Vn.add('I');
        Vn.add('B');
        Vn.add('X');
        Vn.add('O');
        Vn.add('U');

        //添加终结符号
        Vt.add('8');
        Vt.add('9');
        Vt.add('f');
        Vt.add('=');
        Vt.add('i');
        Vt.add('+');
        Vt.add('-');
        Vt.add('*');
        Vt.add('/');
        Vt.add('(');
        Vt.add(')');
        Vt.add('{');
        Vt.add('}');
        Vt.add(';');
        Vt.add('a');
        Vt.add('o');
        Vt.add('t');
        Vt.add('d');
        Vt.add('g');
        Vt.add('l');
        Vt.add('e');
        Vt.add('u');
        Vt.add(END);
        Vt.add(EPISON);

        //输入文法
        grammar.put('P', "S|Q|;");
        grammar.put('S', "V=E;");
        grammar.put('E', "TR");
        grammar.put('R', "ATR|" + EPISON);
        grammar.put('T', "FY");
        grammar.put('Y', "MFY|" + EPISON);
        grammar.put('F', "CZ");
        grammar.put('Z', "OCZ|" + EPISON);
        grammar.put('C', "BI");
        grammar.put('I', "XBI|" + EPISON);
        grammar.put('B', "(E)|i");
        grammar.put('A', "+|-");
        grammar.put('M', "*|/");
        grammar.put('V', "i");
        grammar.put('Q', "8JKH");
        grammar.put('H', "fJKH|9K|" + EPISON);
        grammar.put('J', "(E)");
        grammar.put('K', "S|{U}|;");
        grammar.put('U', "PU|{U}U|" + EPISON);
        grammar.put('X', "a|o");
        grammar.put('O', "t|d|g|l|u|e");

        // 初始化非终结符的first集和follow集
        for (Character sign : Vn) {
            firstSet.put(sign, new HashSet<>());
            followSet.put(sign, new HashSet<>());
        }

        // 根据文法初始化productFirstSet
        for (Character symbol : Vn) {
            String gm = grammar.get(symbol);
            String[] nArrayGm = gm.split("\\|");    // | 为分割单位
            for (String standGm : nArrayGm) {   // 单个文法符号
                productionFirstSet.put(symbol + "->" + standGm, new HashSet<>());
            }
        }

        // 构造first集合
        buildFirstSet();
        // 构造follow集合
        buildFollowSet();
    }


    /**构造FIRST集
     * 反复利用如下规则，直至FIRST集不再增大
     * （1）若X属于Vt,则FIRST(X)={X};
     * （2）若X属于Vn,且有X->aN(a属于Vt),则令a属于FIRST(X);若有X->$,则$属于FIRST(X);
     * （3）若X->Y1Y2...Yk,
     *		1. 将FIRST(Y1)中的一切非$的终结符加进FIRST(X);
     *      2. 若$属于FIRST(Y1),则将FIRST(Y2)中的一切非$的终结符加进FIRST(X);
     *      3. 若$属于FIRST(Y1),并且$属于FIRST(Y2),则将FIRST(Y3)中的一切非$终结符加进FIRST(X);以此类推
     *      4. 若$都属于FIRST(Y1...YN),则将$加进FIRST(X)
     * */
    void buildFirstSet(){
        boolean hasMoreItem = true; // First集合还能扩容
        while(hasMoreItem){
            hasMoreItem = false;
            /*
             * 当前文法符号推出的First集合大小
             * 若size增加，则First集合还可能扩容，否则退出while循环
             */
            int setSize = 0;
            for (Character leftSymbol : Vn) {
                String rightGm = grammar.get(leftSymbol);
                String []nRightGm = rightGm.split("\\|");
                setSize = firstSet.get(leftSymbol).size();
                for (String standGm : nRightGm) {
                    int curIndex = 0;
                    Character pch = standGm.charAt(curIndex);   // {U}类似处理这种情况
                    if(Vt.contains(pch)){  // 终结符处理
                        // 加入leftSymbol的first集合
                        firstSet.get(leftSymbol).add(pch);
                        productionFirstSet.get(leftSymbol+"->"+standGm).add(pch);
                        if(firstSet.get(leftSymbol).size() > setSize)
                            hasMoreItem = true;
                    }else if(Vn.contains(pch)){ // 非终结符处理
                        Set<Character> tmpSet;
                        do{
                            if(curIndex > nRightGm.length){
                                // 最终都要包含 空产生式 EPISON ε,将ε加入lefrSymbol的first集
                                firstSet.get(leftSymbol).add(EPISON);
                                productionFirstSet.get(leftSymbol+"->"+standGm).add(EPISON);
                                break;
                            }
                            pch = standGm.charAt(curIndex);
                            tmpSet = firstSet.get(pch);
                            // 把tmpSet 除 空产生式ε 外放入leftSymbol的first集
                            for (Character tmpc : tmpSet) {
                                if (tmpc != EPISON){
                                    firstSet.get(leftSymbol).add(tmpc);
                                    productionFirstSet.get(leftSymbol+"->"+standGm).add(tmpc);
                                }
                            }
                            curIndex++;
                        }while (tmpSet.contains(EPISON));
                    }else{
                        System.err.println("产生式出现未定义字符，语法分析错误。");
                        System.exit(-2);
                    }
                    // 判断是否first集合增加
                    if (setSize < firstSet.get(leftSymbol).size())
                        hasMoreItem = true;
                }
            }
        }
    }


    /**
     * 展示计算的First集合
     */
    void displayFirstSet(){
        System.out.println("文法的FIRST集如下：");
        for (Character leftSymbol : Vn) {
            System.out.print(leftSymbol+":");
            for (Character item : firstSet.get(leftSymbol)) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }

    /**构造FOLLOW集
     * ①令 #∈FOLLOW(S)        S为文法开始符号
     * ②对 A→ αBβ,  且β ≠ ε
     * 则将 FIRST(β)-{ε} 加入FOLLOW(B)中
     * ③反复, 直至每一个FOLLOW(A)不再增大
     * 对A→ αB或A→ αBβ(且ε ∈ FIRST(β)) 则FOLLOW(A)中的全部元素加入FOLLOW(B)
     * */
    void buildFollowSet(){
        followSet.get(START).add(END);
        boolean hasMoreItem = true;
        while (hasMoreItem){
            hasMoreItem = false;
            int setSize = 0;
            for (Character leftSymbol : Vn) {
                String rightGm = grammar.get(leftSymbol);
                int curIndex = 0;

                // 产生式右部处理
                while (curIndex < rightGm.length()){
                    Character firstChar = rightGm.charAt(curIndex);
                    if (Vt.contains(firstChar) || firstChar.equals('|')){   // terminate
                        curIndex++;
                        continue;
                    }
                    if (curIndex+1 < rightGm.length() ) {
                        Character secondChar = rightGm.charAt(curIndex + 1);
                        if (secondChar.equals('|')) { //达到产生的尾部了
                            //将left的Follow集加入到firstChar的Follow集
                            setSize = followSet.get(firstChar).size();
                            followSet.get(firstChar).addAll(followSet.get(leftSymbol));
                            if (followSet.get(firstChar).size() > setSize)
                                hasMoreItem = true;
                            curIndex += 2;
                            continue;
                        }
                        if (Vt.contains(secondChar)) {    // 终结符,移入firstChar的follow集合
                            setSize = followSet.get(firstChar).size();
                            followSet.get(firstChar).add(secondChar);
                            if (followSet.get(firstChar).size() > setSize) // 新终结符不一定没有
                                hasMoreItem = true;
                        } else if (Vn.contains(secondChar)) { // 非终结符
                            // 将second的FIRST集元素除ε移入firstChar的Follow集
                            setSize = followSet.get(firstChar).size();
                            for (Character firstSetItem : firstSet.get(secondChar)) {
                                if (!firstSetItem.equals(EPISON))
                                    followSet.get(firstChar).add(firstSetItem);
                            }
                            if (followSet.get(firstChar).size() > setSize)
                                hasMoreItem = true;

                            // 如果second的First集包含ε，将leftSymbol的follow集全部加入firstChar的follow集
                            if (firstSet.get(secondChar).contains(EPISON)) {
                                setSize = followSet.get(firstChar).size();
                                followSet.get(firstChar).addAll(followSet.get(leftSymbol));
                                if (followSet.get(firstChar).size() > setSize)
                                    hasMoreItem = true;
                            }
                        }
                    }else{  // 后面没有符号，把leftSymbol的follow集放入firstChar的follow集
                        setSize = followSet.get(firstChar).size();
                        followSet.get(firstChar).addAll(followSet.get(leftSymbol));
                        if (followSet.get(firstChar).size() > setSize)
                            hasMoreItem = true;
                    }
                    curIndex++;
                }
            }
        }
    }

    /**
     * 展示计算的Follow集合
     */
    void displayFollowSet(){
        System.out.println("文法的FOLLOW集如下：");
        for (Character symbol : Vn) {
            System.out.print(symbol+":");
            for (Character item : followSet.get(symbol)) {
                System.out.print(item+" ");
            }
            System.out.println();
        }
    }

//    public static void main(String[] args) {
//        new FirstAndFollow(true);
//    }
}

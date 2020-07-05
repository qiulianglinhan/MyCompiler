package LL1;

import java.io.*;
import java.util.*;

public class LL1PredictionAnalysis {
    private BufferedReader br = null;   // 输入二元式
    private static List<String> inputStream = new ArrayList<>();    // 从二元式拆解输入流
    private int indexP = 0;     // 扫描指针，初始为0
    private Map<String, String> LL1Table = new HashMap<>();  // LL1分析表
    private FirstAndFollow faf = null;  // 初始化first集follow集
    private Stack<Character> analysisStack = new Stack<>();
    private int tab = 1;

    //构造函数
    public LL1PredictionAnalysis(String fileName, boolean displaySourceCode) {
        File fp = new File(fileName);
        if (!fp.getName().endsWith(".lex")) {
            System.out.println("文件格式不正确...");
            System.exit(-2);
        }
        faf = new FirstAndFollow(false);
        //构造文件扫描
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String erYuanShi = "";
            while ((erYuanShi = br.readLine()) != null) {
                //截取符号串
                inputStream.add(erYuanShi.substring(erYuanShi.indexOf(",") + 1,
                        erYuanShi.lastIndexOf(")")));
            }
            inputStream.add("#");  //末尾添加#号，用于分析栈

            if (displaySourceCode) {
                //输出一下序列
                System.out.println("输入的源程序为：");
                int l = inputStream.size();
                for (int i = 0; i < l - 1; ++i) {
                    print_indexP(i);
                }
                System.out.println();
            }

        } catch (FileNotFoundException e) {
            System.out.println(fileName + "文件不存在...");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //构造分析表
        buildLLR1Table();
        //显示分析表
        displayLLR1Table();

        //分析栈中移入终结符号和文法的开始符号
        analysisStack.push(FirstAndFollow.getEND());
        analysisStack.push(FirstAndFollow.getSTART());
    }

    //构建LLR(1)文法的预测表
    private void buildLLR1Table() {
        //LLR1Table的形式：[E,i]:E->TR

        //遍历每一条产生式
        for (String pro : faf.getProductionFirstSet().keySet()) {
            String temp = pro.substring(3); //截取产生式->右边的字符判断是不是ε,从而选择FIRST集还是FOLLOW集
            Set<Character> symbol;
            if (temp.equals("$")) {
                symbol = faf.getFollowSet().get(pro.charAt(0));
            } else {
                symbol = faf.getProductionFirstSet().get(pro);
            }
            for (Character itSymbol : symbol) {
                LL1Table.put(pro.charAt(0) + "," + itSymbol, pro);
            }
        }
    }

    //显示分析表
    private void displayLLR1Table() {
        System.out.println("\n\n\t\t\t\tLL(1)分析表\t\t\t");
        for (int i = 0; i < faf.getVt().size(); ++i) {
            System.out.print("\t" + faf.getVt().get(i) + "\t");
        }
        System.out.println();
        for (int i = 0; i < faf.getVn().size(); ++i) {
            System.out.print(faf.getVn().get(i));
            for (int j = 0; j < faf.getVt().size(); ++j) {
                String str = faf.getVn().get(i) + "," + faf.getVt().get(j);
                String pro = LL1Table.get(str);
                if (pro != null) {
                    System.out.print("\t" + pro);
                } else {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }

    //预测分析执行
    public void analysisProcessing() {
        System.out.println("\n\n预测分析过程表");
        //System.out.println("分析栈\t\t\t\t余留输入串\t\t\t\t所用产生式");
        while (!analysisStack.isEmpty() && indexP < inputStream.size()) {
            String Input = get_indexP(indexP);
            //	System.out.println(Input);
            // if(Input.length()>1||Input.length()==1&&Character.isLetter(Input.charAt(0))) {
            // 符号长度大于1时，表示是一个变量，将其转换成i
            // 或者是长度等于1的字母变量，也转换成i
            //	Input = "i";
            //}
            //分析栈栈栈顶元素弹栈
            displayProcessing("");
            Character analysisSymbol = analysisStack.pop();

            if (faf.getVt().contains(analysisSymbol) &&
                    !analysisSymbol.equals(FirstAndFollow.getEND())) { //非终结符号，识别出来，相销
                if (analysisSymbol == Input.charAt(0)) {
                    ++indexP;
                    continue;
                } else if (analysisSymbol == '$' && Input.charAt(0) == '#') {
                    ++indexP;
                    continue;
                } else {
                    System.out.println("分析失败！" + "算术表达式应该包含字符：" + analysisSymbol);
                    return;
                }

            }

            System.out.println("出栈元素：" + analysisSymbol + "\n" + "输入元素：" + Input);

            if (analysisSymbol == (FirstAndFollow.getEND()) && Input.equals("#")) {
                ++indexP;
                continue;
            }
            if (analysisSymbol == '$' && Input.equals("#")) {
                ++indexP;

                continue;
            }
            //System.out.println(analysisSymbol+Input);
            //根据栈顶元素与输入元素的组合查找分析表，找到对应的产生式

            String temp = LL1Table.get(analysisSymbol + "," + Input);
            //System.out.println(analysisSymbol+","+Input);
            if (temp == null) { // LL1分析表缺少对应产生式
                System.out.println("LL1分析表中 [" + analysisSymbol + "," + Input + "] 无产生式");
                if (analysisSymbol == 'K') {
                    System.out.println("缺少程序体，分析失败");
                    return;
                }
            } else {
                String production = temp.substring(3);
                if (production.equals("$")) {
                    displayProcessing(temp);
                    continue;
                }
                // 产生式右部逆置压栈
                char[] nArry = new StringBuffer(production).reverse().toString().toCharArray();
                for (char c : nArry) {
                    analysisStack.push(c);
                }
                displayProcessing(temp);
            }
            //产生式右部逆序入栈
        }

        if ((analysisStack.isEmpty() || analysisStack.pop() == '#')) {
            System.out.println("分析成功");
            System.out.println("源程序为：");
            int l = inputStream.size();
            for (int i = 0; i < l - 1; ++i) {
                print_indexP(i);
            }
            System.out.println();
        } else
            System.out.println("分析失败");
    }

    //输出分析的中间过程
    private void displayProcessing(String pro) {
        System.out.print("分析栈：" + analysisStack);
        System.out.print("\t\t余留串：" + inputStream.subList(+indexP, inputStream.size()));
        System.out.println("\t\t产生式：" + pro);
    }

    /**
     * 打印当前程序至控制台
     *
     * @param i 当前判断字符，用于进行换行等操作
     */
    public void print_indexP(int i) {
        String s_curr = inputStream.get(i);
        if (i >= 1) {
            if ((inputStream.get(i - 1).equals(";") && !inputStream.get(i).equals("}"))
                    || (inputStream.get(i - 1).equals("else") && !inputStream.get(i).equals("if"))) {
                System.out.print("\n");
                for (int j = 0; j < tab; j++)
                    System.out.print("    ");
                //System.out.print("\n");
            } else if (inputStream.get(i - 1).equals("}")) {
                System.out.print("\n");
                if (inputStream.get(i).equals("}")) {
                    for (int j = 0; j < tab - 1; j++) {
                        System.out.print("    ");
                    }
                } else {

                    for (int j = 0; j < tab; j++) {
                        System.out.print("    ");
                    }
                }
            } else if (inputStream.get(i - 1).equals("{")) {
                if (!inputStream.get(i).equals("{")) {
                    System.out.print("\n");
                    for (int j = 0; j < tab; j++) {
                        System.out.print("    ");
                    }
                } else {
                    System.out.print("\n");
                }
            }

        }

        if (s_curr.equals(";"))
            System.out.print(s_curr + "\n");
        else if (s_curr.equals("{")) {
            if (inputStream.get(i - 1).equals(")")) {
                System.out.print("\n");
            }
            if (!inputStream.get(i - 1).equals("else")) {
                for (int j = 0; j < tab; j++) {
                    System.out.print("    ");
                }
            }

            System.out.print(s_curr);
            tab++;
        } else if (s_curr.equals("}")) {
            tab--;

            if (!inputStream.get(i - 1).equals("}")) {

                for (int j = 0; j < tab; j++) {
                    System.out.print("    ");
                }
            }
            System.out.print(s_curr);

        } else {
            System.out.print(s_curr + " ");
        }
    }

    /**
     * 由token转换为文法的对应符号
     * @param index 当前输入流的指针
     * @return token对应文法符号
     */
    public String get_indexP(int index) {
        String s = inputStream.get(index);

        if (s.equals("{")) {
            s = "{";
        } else if (s.equals("}")) {
            s = "}";
        } else if (s.equals("else")) {
            String s1 = inputStream.get(index + 1);
            if (s1.equals("if")) {
                s = "f";
                indexP++;
            } else {
                s = "9";
            }
        } else if (s.equals("if")) {
            if (index >= 1) {
                String s1 = inputStream.get(index - 1);
                if (s1.equals("else")) {
                    s = "f";
                } else {
                    s = "8";
                }
            } else
                s = "8";

        } else if (s.equals("&&"))
            s = "a";
        else if (s.equals("||"))
            s = "o";
        else if (s.equals("<"))
            s = "l";
        else if (s.equals("=="))
            s = "e";
        else if (s.equals("!="))
            s = "u";
        else if (s.equals(">="))
            s = "t";
        else if (s.equals("<="))
            s = "d";
        else if (s.equals(">"))
            s = "g";
        else if (s.equals(";"))
            s = ";";
        else if ((s.length() == 1) && Character.isLetterOrDigit(s.charAt(0))) {
            s = "i";
        } else if (s.length() > 1) {
            s = "i";
            for (int i = 0; i < s.length(); ++i) {
                if (!Character.isLetterOrDigit(s.charAt(i))) {
                    System.out.println("未知符号：" + s);
                    s = "unknow";
                }

            }
        }
        return s;
    }

    public static void main(String[] args) {
        LL1PredictionAnalysis ll1PredictionAnalysis = new LL1PredictionAnalysis("E:\\ideaPro\\MyCompiler\\src\\LL1\\test.lex", true);
        ll1PredictionAnalysis.analysisProcessing();
    }
}

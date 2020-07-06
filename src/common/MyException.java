package common;

/**
 * 自定义异常
 */

public class MyException extends RuntimeException {

    public static final int NUMERROR = 1;
    public static final int COMMENTERROR = 2;
    public static final int DIVERROR = 3;
    public static final int BRACKETERROR = 4;
    public static final int EXPRESSIONERROR = 5;
    public static final int DECERROR = 6;
    public static final int IDENTIFYERROR = 7;
    public static final int IDENTIFYNOTFOUND = 8;
    public static final int RELERROR = 9;

    /**
     * 自定义异常
     * @param errorCode 错误码,-1表示未知错误
     * @param line 展示当前出错行号，-1代表不显示行号
     */
    public MyException(int errorCode, int line){
        if (errorCode == NUMERROR) {
            printNumberError(line);
        }else if(errorCode == COMMENTERROR){
            printCommentError(line);
        }else if(errorCode == DIVERROR){
            printDivError(line);
        }else if (errorCode == BRACKETERROR){
            printBracketError(line);
        }else if (errorCode == EXPRESSIONERROR){
            printExpressionError(line);
        }else if(errorCode == DECERROR){
            printDeclarationError(line);
        }else if (errorCode == IDENTIFYERROR){
            printIdentifyError(line);
        }else if (errorCode == IDENTIFYNOTFOUND){
            printIdentifyNotFound(line);
        }else if (errorCode == RELERROR){
            printRelError(line);
        }
        else {
            unkonwError();
        }
    }

    private void printNumberError(int line){
        System.err.println("第"+line+"行出现数字解析错误！");
        System.exit(-1);
    }

    private void printCommentError(int line){
        System.err.println("未找到注释结束符号.");
        System.exit(-1);
    }

    private void printDivError(int line){
        System.err.println("除数不能为0.");
        System.exit(-1);
    }

    private void printBracketError(int line){
        System.err.println("左右括号不匹配.");
        System.exit(-1);
    }

    private void printExpressionError(int line){
        System.err.println("表达式本身表达错误.");
        System.exit(-1);
    }

    private void printDeclarationError(int line){
        System.err.println(line+"行赋值表达式编写错误.");
        System.exit(-1);
    }

    private void printIdentifyError(int line){
        System.err.println(line+"行标识符错误.");
        System.exit(Exit.IDENTIFYERROR);
    }

    private void printIdentifyNotFound(int line){
        System.err.println(line+"行标识符未找到错误.");
        System.exit(Exit.IDNETIFYNOTFOUDERROR);
    }

    private void printRelError(int line){
        System.err.println(line+"行relation表达式错误");
        System.exit(Exit.RELERROR);
    }

    private void unkonwError(){
        System.err.println("未知错误代码");
        System.exit(-1);
    }


}

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
    public static final int DECERROR = 6;   // 声明错误
    public static final int IDENTIFYERROR = 7;
    public static final int IDENTIFYNOTFOUND = 8;
    public static final int RELERROR = 9;
    public static final int UNKNOWWORD = 10;
    public static final int SYNTAXERROR = 11;
    public static final int FOURFORMULAERROR = 12;
    public static final int ARRAYINDEXERROR = 13;
    public static final int ARRAYERROR = 14;
    public static final int ARRAYINDEXOUTOFBOUNDS = 15;
    public static final int ARRAYIDENTIFYNOTFOUNDERROR = 16;
    public static final int INCERROR = 17;  // 自增错误
    public static final int DECERRORR = 18; // 自减错误
    public static final int BREAKERROR = 19;
    public static final int CONTINUEERROR = 20;
    public static final int CASEERROR = 21; // case
    public static final int DEFAULTERROR = 22;  // default
    public static final int ELSEERROR = 23;     // else

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
        }else if (errorCode == UNKNOWWORD){
            printUnknowWordError(line);
        }else if (errorCode == SYNTAXERROR){
            printSyntaxError(line);
        }else if (errorCode == FOURFORMULAERROR){
            printFourFormulaError(line);
        }else if (errorCode == ARRAYINDEXERROR){
            printArrayIndexError(line);
        }else if (errorCode == ARRAYERROR){
            printArrayError(line);
        }else if (errorCode == ARRAYINDEXOUTOFBOUNDS){
            printArrayIndexOutOfBounds(line);
        }else if (errorCode == ARRAYIDENTIFYNOTFOUNDERROR){
            printArrayIdentificationNotFoundError(line);
        }else if (errorCode == INCERROR){
            printIncError(line);
        }else if (errorCode == DECERRORR){
            printDecError(line);
        }else if (errorCode == BREAKERROR){
            printBreakError(line);
        }else if (errorCode == CONTINUEERROR){
            printContinueError(line);
        }else if (errorCode == CASEERROR){
            printCaseError(line);
        }else if (errorCode == DEFAULTERROR){
            printDefaultError(line);
        }else if (errorCode == ELSEERROR){
            printElseError(line);
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

    private void printUnknowWordError(int line){
        System.err.println(line+"行出现未知符号");
        System.exit(Exit.UNKNOWWORDERROR);
    }

    private void printSyntaxError(int line){
        System.err.println(line+"行出现语法错误");
        System.exit(Exit.SYNTAXERROR);
    }

    private void printFourFormulaError(int line){
        System.err.println(line+"行四元式错误");
        System.exit(Exit.FOURFORMULAERROR);
    }

    private void printArrayIndexError(int line){
        System.err.println(line+"行数组索引错误");
        System.exit(Exit.ARRAYINDEXERROR);
    }

    private void printArrayError(int line){
        System.err.println(line+"行数组转换错误");
        System.exit(Exit.ARRAYERROR);
    }

    private void printArrayIndexOutOfBounds(int line){
        System.err.println(line+"行出现数组越界异常");
        System.exit(Exit.ARRAYINDEXOUTOFBOUNDS);
    }

    private void printArrayIdentificationNotFoundError(int line){
        System.err.println(line+"行数组标识符未找到");
        System.exit(Exit.ARRAYIDENTIFYNOTFOUNDERROR);
    }

    private void printIncError(int line){
        System.err.println(line+"行自增错误");
        System.exit(Exit.INCERROR);
    }

    private void printDecError(int line){
        System.err.println(line+"行自减错误");
        System.exit(Exit.DECERRORR);
    }

    private void printBreakError(int line){
        System.err.println(line+"行break语句使用错误");
        System.exit(Exit.BREAKERROR);
    }

    private void printContinueError(int line){
        System.err.println(line+"行continue语句使用错误");
        System.exit(Exit.CONTINUEERROR);
    }

    private void printCaseError(int line){
        System.err.println(line+"行case语句错误，未配合switch语句");
        System.exit(Exit.CASEERROR);
    }

    private void printDefaultError(int line){
        System.err.println(line+"行default语句错误，未配合switch语句");
        System.exit(Exit.DEFAULTERROR);
    }

    private void printElseError(int line){
        System.err.println(line+"行else语句错误");
        System.exit(Exit.ELSEERROR);
    }

    private void unkonwError(){
        System.err.println("未知错误代码");
        System.exit(-1);
    }


}

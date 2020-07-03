package common;

public class MyException extends RuntimeException {

    public static final int NUMERROR = 1;
    public static final int COMMENTERROR = 2;

    public MyException(int errorCode, int line){
        if (errorCode == NUMERROR) {
            printNumberError(line);
        }else if(errorCode == COMMENTERROR){
            printCommentError(line);
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

    private void unkonwError(){
        System.err.println("未知错误代码");
        System.exit(-1);
    }


}

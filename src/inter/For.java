package inter;

import java.util.ArrayList;
import java.util.Stack;

public class For {
    private boolean forStatAssign;                 // 用于处理for语句第三条赋值语句
    private ArrayList<Integer> forStatRecord;       // 用于处理for循环需要替换部分

    public For(boolean forStatAssign){
        this.forStatAssign = forStatAssign;
        forStatRecord = new ArrayList<>();
    }

    public boolean getForStatAssign() {
        return forStatAssign;
    }

    public void setForStatAssign(boolean forStatAssign) {
        this.forStatAssign = forStatAssign;
    }

    public ArrayList<Integer> getForStatRecord() {
        return forStatRecord;
    }

}

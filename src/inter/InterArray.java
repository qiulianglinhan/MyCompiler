package inter;

import java.util.ArrayList;

/**
 * 用于中间代码表示的数组
 */
public class InterArray {
    private final int size;             // 数组大小
    private ArrayList<Number> array;

    public InterArray(int size) {
        this.size = size;
        array = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            array.add(0);
        }
    }

    public InterArray(int size, ArrayList<Number> array){
        this.size = size;
        this.array = array;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Number> getArray() {
        return array;
    }
}

package common;

import java.util.ArrayList;

public class Array extends Token{

    private final int size;
    private ArrayList<Number> array;

    public Array(int size,int type,int line) {
        super(type,null,line);
        this.size = size;
        array = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            array.add(0);
        }
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Number> getArray() {
        return array;
    }
}

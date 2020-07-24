package inter;

public class Parameter {
    private final int type;
    private final String id;

    public Parameter(int type, String id){
        this.type = type;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}

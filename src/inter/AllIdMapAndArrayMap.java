package inter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于存储id转value的映射
 * 存储id转数组的映射
 */
public class AllIdMapAndArrayMap {
    private Map<String,Double> idMap ;             // id -> value
    private Map<String, InterArray>  arrayMap ;    // id -> array
    private int returnPosition;                    // 保存 return 后的执行位置
    private Map<String,Double> resultIdMap;        // Result类中的 id map
    private Map<String,InterArray> resultArrayMap; // Result类中的 array map

    public AllIdMapAndArrayMap(Map<String,Double> idMap,Map<String,InterArray> arrayMap
    ,int returnPosition){
        this.idMap = new HashMap<>();
        this.arrayMap = new HashMap<>();
        this.returnPosition = returnPosition;
        this.resultArrayMap = new HashMap<>();
        this.resultIdMap = new HashMap<>();
        idMap.forEach((k,v) -> this.idMap.put(k,v));
        arrayMap.forEach((k,v) -> this.arrayMap.put(k,v));
    }

    public Map<String, Double> getIdMap() {
        return idMap;
    }

    public Map<String, InterArray> getArrayMap() {
        return arrayMap;
    }

    public int getReturnPosition() {
        return returnPosition;
    }

    public Map<String, Double> getResultIdMap() {
        return resultIdMap;
    }

    public Map<String, InterArray> getResultArrayMap() {
        return resultArrayMap;
    }
}

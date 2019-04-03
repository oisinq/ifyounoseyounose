package org.ifyounoseyounose.backend.smelldetectors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PrimitiveObsessionCollector extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(PrimitiveType md, List<Integer> collector){
        super.visit(md, collector);
        addLineNumbers(md, collector);
    }

    //get the number of each primitive type in a hashMap
    public HashMap<Type,Integer> reflectPrimitive(Class host){
        HashMap<Type, Integer> primitiveMap = new HashMap<>();

        Field[]  fields = host.getDeclaredFields();
        for(Field f:fields){
            if(f.getType().isPrimitive()){

                if(!primitiveMap.containsKey(f.getType())){
                    primitiveMap.put(f.getType(), 1);
                }
                else{
                    primitiveMap.put(f.getType(), primitiveMap.get(f.getType())+1);
                }
            }
        }

            return primitiveMap;
    }

    private void addLineNumbers(Node node, List<Integer> collector) {
        Optional<Range> m = node.getRange();
        Range r = m.get();
        for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
            collector.add(lineNumber);
        }
    }
}

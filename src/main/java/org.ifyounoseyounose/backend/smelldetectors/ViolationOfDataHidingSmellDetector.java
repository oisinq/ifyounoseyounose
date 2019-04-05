package org.ifyounoseyounose.backend.smelldetectors;


import org.ifyounoseyounose.backend.SmellReport;


import java.lang.reflect.Modifier;
import java.util.List;
import java.lang.reflect.Field;



    @Override
    public SmellReport detectSmell(List<Class> classes){
    SmellReport smells = new SmellReport();
    
    for(Class c:classes){
        Field[] fields =  c.getDeclaredFields();
        for(Field f:fields){
            int modifiers = f.getModifiers();
                if(Modifier.isPublic(modifiers)){
                    //get the line of code






                    smells.addToReport(c, );
                }

        }
    }

    return smells;
    }
}

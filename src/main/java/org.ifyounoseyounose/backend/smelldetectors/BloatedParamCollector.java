package org.ifyounoseyounose.backend.smelldetectors;


import com.github.javaparser.ast.body.MethodDeclaration;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


import java.util.*;

public class BloatedParamCollector extends VoidVisitorAdapter<List<Integer>> {

        @Override
        public void visit(MethodDeclaration md, List<Integer> collector){
            super.visit(md, collector);
            int limit = collector.get(0);

            if(md.getParameters().toString().length()>=limit){
                //giving the line of the method declaration
                collector.add(md.getRange().get().begin.line);
            }

        }


    }

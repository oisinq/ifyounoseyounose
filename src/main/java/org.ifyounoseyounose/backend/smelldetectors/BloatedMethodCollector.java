package org.ifyounoseyounose.backend.smelldetectors;
import java.util.List;
import java.util.Optional;


import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public class BloatedMethodCollector extends VoidVisitorAdapter<List<Integer>> {

    @Override
    public void visit(MethodDeclaration md, List<Integer> collector){
        super.visit(md, collector);
        int limit = collector.get(0);
        int commentNumber = 0;
        int linesCount = md.getRange().get().end.line - md.getRange().get().begin.line;
        if( linesCount>= limit){
            //gets the first line of the method
          List<Comment> comments= md.getAllContainedComments();
          List<Comment> orphaned = md.getOrphanComments();


          for(Comment comment:orphaned){
              commentNumber++;
          }
          for(Comment comment:comments){
              commentNumber++;
          }
            System.out.print(commentNumber + " ");
            System.out.println( md.getBody());

          if(linesCount-commentNumber >= limit){
            addLineNumbers(md, collector);
          }

        }
    }

    private void addLineNumbers(Node node, List<Integer> collector) {
        Optional<Range> m = node.getRange();
        Range r = m.get();
        for (int lineNumber = r.begin.line; lineNumber <= r.end.line; lineNumber++) {
            collector.add(lineNumber);
        }
    }

}

package pr.vladimir.demo1;

import pr.vladimir.demo1.API.Tree;
import pr.vladimir.demo1.API.Vector2D;
import pr.vladimir.demo1.Tiles.Carbon;
import pr.vladimir.demo1.Tiles.GridElement;

import java.util.*;

import static pr.vladimir.demo1.Backend.getMatrix;
import static pr.vladimir.demo1.Tiles.Connection.castTo;

public class Logic {
    public static Tree tree = new Tree();

    public static void updateRelMat(GridElement before, GridElement after) {
         if(after == null) {
             if(before instanceof Carbon carbon) tree.removeVertex(carbon);
             return;
         }

         var lookupList = recursiveWalk(after);
         if(after instanceof Carbon carbon) {
             tree.addEdge(carbon, lookupList.toArray(Carbon[]::new));
         }
    }

    private static List<Carbon> recursiveWalk(GridElement point, Boolean... blocks) {
        var boxVec = point.getBoxVec();
        var paddedBlocks = new Boolean[4];
        System.arraycopy(blocks, 0, paddedBlocks, 0, blocks.length);
        for (int i = 0; i < paddedBlocks.length; i++) {
           if(paddedBlocks[i] == null) paddedBlocks[i] = false;
        }

        GridElement top = null, bot = null, left = null, right = null;
        if(boxVec.getY() > 0) top = paddedBlocks[0] ? null : getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()-1));
        if(boxVec.getY() < 18) bot = paddedBlocks[1] ? null : getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()+1));
        if(boxVec.getX() > 0) left = paddedBlocks[2] ? null : getMatrix(new Vector2D(boxVec.getX()-1, boxVec.getY()));
        if(boxVec.getX() < 24) right = paddedBlocks[3] ? null : getMatrix(new Vector2D(boxVec.getX()+1, boxVec.getY()));
        var carbList = castTo(Carbon.class, top, bot, left, right);

        List<Carbon> retList = new ArrayList<>();
        if(!point.isClazz(Carbon.class)) retList.addAll(carbList);
        GridElement[] surroundings = new GridElement[4]; surroundings[0] = top; surroundings[1] = bot; surroundings[2] = left; surroundings[3] = right;
        for (int i = 0; i < surroundings.length; i++) {
           var elem = surroundings[i];
           if(elem == null) continue;
           if(elem.isClazz(Carbon.class)) continue;
           retList.addAll(recursiveWalk(elem, i == 1, i == 0, i == 3, i == 2));
        }

        return retList;
    }

    public static void treeAnalysis() {
        List<List<Carbon>> longestPaths = tree.findAllLongestPaths();
        List<Carbon> crossections = tree.findCrosssections();

        List<Carbon> longestPath = Tree.findPathWithClosestPoint(longestPaths, crossections);
        if(longestPath == null) return;

        if(longestPath.getFirst().id > longestPath.getLast().id) longestPath = longestPath.reversed();

        for (Carbon carbon : longestPath) {
            carbon.renderAnnotation(String.valueOf(longestPath.indexOf(carbon)));
        }


    }
}

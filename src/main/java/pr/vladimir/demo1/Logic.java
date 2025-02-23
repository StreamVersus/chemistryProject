package pr.vladimir.demo1;

import pr.vladimir.demo1.Tiles.Carbon;
import pr.vladimir.demo1.Tiles.GridElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pr.vladimir.demo1.Backend.getMatrix;
import static pr.vladimir.demo1.Tiles.Connection.castTo;

public class Logic {
    public static Map<Carbon, List<Carbon>> relativityMap = new HashMap<>();

    public static void updateRelMat(GridElement obj) {
         if(obj == null) return;

         var lookupList = recursiveWalk(obj);
         System.out.println(lookupList);
         if(obj instanceof Carbon carbon) {
             relativityMap.put(carbon, lookupList);
         }

         for (Carbon carbon : lookupList) {
             var copiedList = new ArrayList<>(List.copyOf(lookupList));
             copiedList.remove(carbon);
             relativityMap.put(carbon, copiedList);
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

        List<Carbon> retList = new ArrayList<>(carbList);
        GridElement[] surroundings = new GridElement[4]; surroundings[0] = top; surroundings[1] = bot; surroundings[2] = left; surroundings[3] = right;
        for (int i = 0; i < surroundings.length; i++) {
           var elem = surroundings[i];
           if(elem == null) continue;
           if(elem.isClazz(Carbon.class)) continue;
           retList.addAll(recursiveWalk(elem, i == 1, i == 0, i == 3, i == 2));
        }

        return retList;
    }
}

package pr.vladimir.chemistry;

import pr.vladimir.chemistry.API.ChemicalCompound;
import pr.vladimir.chemistry.API.Tree;
import pr.vladimir.chemistry.API.Vector2D;
import pr.vladimir.chemistry.Tiles.Carbon;
import pr.vladimir.chemistry.Tiles.Connection;
import pr.vladimir.chemistry.Tiles.FuncGroup;
import pr.vladimir.chemistry.Tiles.GridElement;

import java.util.*;

import static pr.vladimir.chemistry.Backend.getMatrix;
import static pr.vladimir.chemistry.Tiles.Connection.castTo;

public class Logic {
    public static Tree tree = new Tree();

    public static void updateRelMat(GridElement before, GridElement after) {
         if(after == null) {
             if(before instanceof Carbon carbon) tree.removeVertex(carbon);
             else if(before instanceof Connection con) {
                 Carbon[] temp = con.getAdjacent();
                 if(temp[0] != null && temp[1] != null) tree.removeEdge(temp[0], temp[1]);
             }
             return;
         }

         var lookupList = recursiveWalk(after);
         if(after instanceof Carbon carbon) {
             tree.addEdge(carbon, lookupList.toArray(Carbon[]::new));
         } else if (after instanceof Connection con) {
             Carbon[] temp = con.getAdjacent();
             if(temp[0] != null && temp[1] != null) tree.addEdge(temp[0], temp[1]);
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
        List<Carbon> crossections = tree.findCrossections();

        List<Carbon> longestPath = tree.findLongestPath(longestPaths, crossections).reversed();
        if(longestPath == null) return;
        List<List<Carbon>> radicals = tree.returnRadicals(longestPath);

        if(FuncGroup.funcVar != null) {
            //sort by funcGroup
            if(!Connection.specialSet.isEmpty()) return;
            int index = longestPath.indexOf(FuncGroup.funcVar);
            int indexFromEnd = longestPath.size() - 1 - index;

            if(indexFromEnd < index) longestPath = longestPath.reversed();
        }
        else if(!Connection.specialSet.isEmpty()) {
            int shortestLength = Integer.MAX_VALUE;
            boolean LtR = true;

            //sort by connection
            for (Connection connection : Connection.specialSet) {
                int dirLength = LtR ? shortestLength : longestPath.size() - 1 - shortestLength;
                int index = Integer.MAX_VALUE;
                for (Carbon carbon : connection.getAdjacent()) {
                    index = Math.min(longestPath.indexOf(carbon), index);
                }

                int fromEnd = longestPath.size() - 1 - index;
                if(index < fromEnd  && dirLength > index) {
                    shortestLength = index;
                    LtR = true;
                }
                else if(dirLength > fromEnd) {
                    shortestLength = index;
                    LtR = false;
                }

                if(!LtR) longestPath = longestPath.reversed();
            }
        } else if (!crossections.isEmpty()) {
            //sort by radical
            int shortestLength = Integer.MAX_VALUE;
            boolean LtR = true;

            for (Carbon point : crossections) {
                if (longestPath.contains(point)) {
                    int dirLength = LtR ? shortestLength : longestPath.size() - 1 - shortestLength;
                    int index = longestPath.indexOf(point);
                    int fromEnd = longestPath.size() - 1 - index;

                    if(index < fromEnd  && dirLength > index) {
                        shortestLength = index;
                        LtR = true;
                    }
                    else if(dirLength > fromEnd) {
                        shortestLength = index;
                        LtR = false;
                    }

                    if(!LtR) longestPath = longestPath.reversed();
                }
            }
        } else {
            //sort LtR
            if(longestPath.getFirst().getBoxVec().getX() > longestPath.getLast().getBoxVec().getX()) longestPath = longestPath.reversed();
        }

        for (Carbon carbon : longestPath) {
            carbon.renderAnnotation(String.valueOf(longestPath.indexOf(carbon) + 1));
        }

        ChemicalCompound main = new ChemicalCompound(longestPath, radicals);

        Frontend.renderName(main.getName());
    }
}

package pr.vladimir.demo1;

import pr.vladimir.demo1.Tiles.Carbon;
import pr.vladimir.demo1.Tiles.GridElement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static pr.vladimir.demo1.Frontend.gridSize;
import static pr.vladimir.demo1.Frontend.screenSize;
import static pr.vladimir.demo1.Tiles.Carbon.idList;

public class Logic {
    public final static Integer[][] relativityMatrix;
    public static GridElement[][] cachedGrid;
    static {
        var carbAmount = idList.size();
        relativityMatrix = new Integer[carbAmount][carbAmount];

        int gridHeight = (int) (screenSize.getX() / gridSize);
        int gridWidth = (int) (screenSize.getY() / gridSize);
        cachedGrid = new GridElement[gridHeight][gridWidth];
    }

    public static void updateRelMat(GridElement[][] grid) {



    }

    private static List<Carbon> recursiveWalk(GridElement point) {
        return null;
    }

    public static GridElement[][] cacheDiff(GridElement[][] grid) {
        GridElement[][] retMat = grid.clone();
        for (int i = 0; i < cachedGrid.length; i++) {
           var line = cachedGrid[i];
           for (int i1 = 0; i1 < line.length; i1++) {
                var cachedElem = line[i1];
                var gridElem = retMat[i][i1];
                if(cachedElem == null || gridElem == null) continue;

                if(Objects.equals(gridElem.toString(), cachedElem.toString())) retMat[i][i1] = null;
            }
        }

        cachedGrid = Arrays.stream(grid).map(GridElement[]::clone).toArray(_ -> grid.clone());
        return null;
    }
}

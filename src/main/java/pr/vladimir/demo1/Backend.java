package pr.vladimir.demo1;

import pr.vladimir.demo1.API.Vector2D;
import pr.vladimir.demo1.Tiles.Carbon;
import pr.vladimir.demo1.Tiles.Connection;
import pr.vladimir.demo1.Tiles.GridElement;

import static pr.vladimir.demo1.Frontend.*;

public class Backend {
    public static GridElement[][] formulaMatrix = new GridElement[0][];

    public Backend() {
        var screenSize = Frontend.screenSize;

        int gridHeight = (int) (screenSize.getX() / gridSize);
        int gridWidth = (int) (screenSize.getY() / gridSize);
        formulaMatrix = new GridElement[gridHeight][gridWidth];
    }

    public void handleRMB(Vector2D boxVec) {
        var value = getMatrix(boxVec);
        if(value == null || value.isClazz(Connection.class)) new Carbon(boxVec);
        else if(value.isClazz(Carbon.class)) {
            ((Carbon) value).destroy();
            Connection.updateAll();
        }
    }

    public void handleLMB(Vector2D boxVec) {
        var value = getMatrix(boxVec);
        if(value == null) new Connection(boxVec);
        if(value instanceof Connection con) con.increment();
    }

    public static void setMatrix(Vector2D vec, GridElement i) {
        GridElement before = getMatrix(vec);
        formulaMatrix[(int) vec.getX()][(int) vec.getY()] = i;
        Logic.updateRelMat(before, i);
    }

    public static GridElement getMatrix(Vector2D vec) {
        return formulaMatrix[(int) vec.getX()][(int) vec.getY()];
    }
}

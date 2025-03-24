package pr.vladimir.chemistry;

import pr.vladimir.chemistry.API.Vector2D;
import pr.vladimir.chemistry.Tiles.Carbon;
import pr.vladimir.chemistry.Tiles.Connection;
import pr.vladimir.chemistry.Tiles.FuncGroup;
import pr.vladimir.chemistry.Tiles.GridElement;

import static pr.vladimir.chemistry.Frontend.*;

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
        if(state == 0) {
            if (value == null) new Carbon(boxVec);
            else if (value.isClazz(Carbon.class)) {
                ((Carbon) value).destroy();
                Connection.updateAll();
            }
        }
        else {
            if (value == null) new FuncGroup(boxVec);
            else if (value.isClazz(FuncGroup.class)) {
                ((FuncGroup) value).increment();
            }
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

        if(i instanceof Connection c) Connection.specialSet.remove(c);
    }

    public static GridElement getMatrix(Vector2D vec) {
        if((int) vec.getX() >= 24 || (int) vec.getY() >= 18) return null;
        return formulaMatrix[(int) vec.getX()][(int) vec.getY()];
    }
}

package pr.vladimir.chemistry.Tiles;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import pr.vladimir.chemistry.API.Vector2D;

import java.util.List;

import static pr.vladimir.chemistry.Backend.setMatrix;
import static pr.vladimir.chemistry.Frontend.canvas;
import static pr.vladimir.chemistry.Frontend.gridSize;

public class FuncGroup extends Carbon{
    public static final List<String> lookup = List.of("CHO", "OH", "COOH");
    public static FuncGroup funcVar;

    public FuncGroup(Vector2D boxVec) {
        super(boxVec);
        if(funcVar != null) this.destroy();
        else funcVar = this;
    }

    public FuncGroup(Vector2D boxVec, Integer i) {
        super(boxVec);
        if(funcVar != null) this.destroy();
        else funcVar = this;

        state = i;
        update();
    }

    @Override
    public void render() {
        if(isForRemoval) return;
        var boxCoordVec = new Vector2D(getBoxVec().getX() * gridSize, getBoxVec().getY() * gridSize);
        var gc = canvas.getGraphicsContext2D();

        String text = lookup.get(state);
        var textX = boxCoordVec.getX() + gridSize / 2F;
        var textY = boxCoordVec.getY() + gridSize / 2F;

        gc.setFont(javafx.scene.text.Font.font("Arial", 16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setStroke(Color.BLACK);
        gc.clearRect(boxCoordVec.getX() + 2,
                boxCoordVec.getY() + 2,
                gridSize - 5,
                gridSize - 5);
        gc.fillText(text,
                textX,
                textY);
    }

    public void increment() {
        if(state == lookup.size() - 1) {
            this.destroy();
            return;
        }

        state++;
        update();
    }

    @Override
    public void destroy() {
        idMap.remove(id);
        clear();
        isForRemoval = true;
        setMatrix(getBoxVec(), null);
        if(funcVar == this) funcVar = null;
    }

    @Override
    public boolean isClazz(Class<?> gridclass) {
        return FuncGroup.class == gridclass;
    }
}

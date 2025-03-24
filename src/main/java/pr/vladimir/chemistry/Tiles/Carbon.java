package pr.vladimir.chemistry.Tiles;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import pr.vladimir.chemistry.API.Vector2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static pr.vladimir.chemistry.Frontend.*;
import static pr.vladimir.chemistry.Backend.*;

public class Carbon implements GridElement {
    public static Map<Integer, Carbon> idMap = new HashMap<>();
    public int id;
    private final Vector2D boxVec;
    private boolean canUpdate = true;
    public int hydroCount = 4;
    public boolean isForRemoval = false;
    public Integer state = 0;

    public Carbon(Vector2D boxVec) {
        int count = 0;
        for (Integer i : idMap.keySet()) {
            if(i != count) break;
            count++;
        }

        id = count;
        this.boxVec = boxVec;
        idMap.put(id, this);

        setMatrix(boxVec, this);
        this.update();
    }

    @Override
    public void canUpdate() {
        canUpdate = true;
    }

    @Override
    public void render() {
        var boxCoordVec = new Vector2D(boxVec.getX() * gridSize, boxVec.getY() * gridSize);
        var gc = canvas.getGraphicsContext2D();

        var text = "C";
        if(hydroCount != 0) text = "CH" + hydroCount;
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

        if(isVerbose) {
            renderAnnotation(String.valueOf(id));
        }
    }

    @Override
    public void update() {
        if(!canUpdate) return;
        canUpdate = false;
        hydroCount = 4;

        GridElement top = null, bot = null, left = null, right = null;
        if(boxVec.getY() > 0) top = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()-1));
        if(boxVec.getY() < 18) bot = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()+1));
        if(boxVec.getX() > 0) left = getMatrix(new Vector2D(boxVec.getX()-1, boxVec.getY()));
        if(boxVec.getX() < 24) right = getMatrix(new Vector2D(boxVec.getX()+1, boxVec.getY()));

        if(bot != null && !bot.isClazz(Carbon.class)) {
            bot.update();
            if(bot instanceof Connection botCon && botCon.isVertical) hydroCount -= botCon.value;
        }
        if(top != null && !top.isClazz(Carbon.class)) {
            top.update();
            if(top instanceof Connection topCon && topCon.isVertical) hydroCount -= topCon.value;
        }
        if(left != null && !left.isClazz(Carbon.class)) {
            left.update();
            if(left instanceof Connection leftCon && !leftCon.isVertical) hydroCount -= leftCon.value;
        }
        if(right != null && !right.isClazz(Carbon.class)) {
            right.update();
            if(right instanceof Connection rightCon && !rightCon.isVertical) hydroCount -= rightCon.value;
        }

        render();
    }

    @Override
    public boolean isClazz(Class<?> gridclass) {
        return Carbon.class == gridclass || FuncGroup.class == gridclass;
    }

    @Override
    public Vector2D getBoxVec() {
        return boxVec;
    }

    public void clear() {
        var gc = canvas.getGraphicsContext2D();
        var boxCoordVec = new Vector2D(boxVec.getX() * gridSize, boxVec.getY() * gridSize);

        gc.clearRect(boxCoordVec.getX() + 2,
                boxCoordVec.getY() + 2,
                gridSize - 5,
                gridSize - 5);
    }

    public void destroy() {
        idMap.remove(id);
        clear();
        isForRemoval = true;
        setMatrix(boxVec, null);
    }

    public static Carbon getByID(int id) {
        return idMap.get(id);
    }

    public void renderAnnotation(String print) {
        if(isVerbose && !Objects.equals(print, String.valueOf(id))) return;

        var boxCoordVec = new Vector2D(boxVec.getX() * gridSize, boxVec.getY() * gridSize);
        var gc = canvas.getGraphicsContext2D();
        var textX = boxCoordVec.getX() + gridSize / 2F;
        var textY = boxCoordVec.getY() + gridSize / 2F;

        gc.clearRect(boxCoordVec.getX() + 2,
                boxCoordVec.getY() + 2,
                gridSize - 5,
                15);

        gc.fillText(String.valueOf(print),
                textX,
                textY - 15);
    }
}

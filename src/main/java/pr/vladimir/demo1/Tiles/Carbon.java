package pr.vladimir.demo1.Tiles;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import pr.vladimir.demo1.Vector2D;

import java.util.HashMap;
import java.util.Map;

import static pr.vladimir.demo1.Frontend.*;
import static pr.vladimir.demo1.Backend.*;
import static pr.vladimir.demo1.Tiles.Connection.castTo;

public class Carbon implements GridElement {
    public static Map<Integer, Carbon> idList = new HashMap<>();
    public int id;
    private final Vector2D boxVec;
    private boolean canUpdate = true;
    public int hydroCount = 4;
    public boolean isForRemoval = true;

    public Carbon(Vector2D boxVec) {
        int count = 0;
        for (Integer i : idList.keySet()) {
            if(i != count) break;
            count++;
        }
        System.out.println(idList.keySet());
        id = count;

        this.boxVec = boxVec;

        GridElement top = null, bot = null, left = null, right = null;
        if(boxVec.getY() > 0) top = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()-1));
        if(boxVec.getY() < 18) bot = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()+1));
        if(boxVec.getX() > 0) left = getMatrix(new Vector2D(boxVec.getX()-1, boxVec.getY()));
        if(boxVec.getX() < 24) right = getMatrix(new Vector2D(boxVec.getX()+1, boxVec.getY()));
        var carbList = castTo(Carbon.class, top, bot, left, right);

        if(!carbList.isEmpty()) return;
        idList.put(id, this);

        setMatrix(boxVec, this);
        update();
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
            gc.setStroke(Color.RED);
            gc.fillText(String.valueOf(id),
                    textX,
                    textY - 15);
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
        return getClass() == gridclass;
    }

    @Override
    public Vector2D getBoxVec() {
        return boxVec;
    }

    private void clear() {
        var gc = canvas.getGraphicsContext2D();
        var boxCoordVec = new Vector2D(boxVec.getX() * gridSize, boxVec.getY() * gridSize);

        gc.clearRect(boxCoordVec.getX() + 2,
                boxCoordVec.getY() + 2,
                gridSize - 5,
                gridSize - 5);
    }

    public void destroy() {
        idList.remove(id);
        clear();
        isForRemoval = true;
        setMatrix(boxVec, null);
    }
}

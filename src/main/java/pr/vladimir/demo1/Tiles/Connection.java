package pr.vladimir.demo1.Tiles;

import javafx.scene.paint.Color;
import pr.vladimir.demo1.API.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static pr.vladimir.demo1.Backend.*;
import static pr.vladimir.demo1.Frontend.*;

public class Connection implements GridElement {
    private final Vector2D boxVec;
    public int value = 1;
    public boolean isVertical = false;
    private boolean canUpdate = true;
    private static final List<Connection> conList = new ArrayList<>();

    public Connection(Vector2D boxVec) {
        this.boxVec = boxVec;
        boolean isAble = true;

        GridElement top = null, bot = null, left = null, right = null;
        if(boxVec.getY() > 0) top = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()-1));
        if(boxVec.getY() < 18) bot = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()+1));
        if(boxVec.getX() > 0) left = getMatrix(new Vector2D(boxVec.getX()-1, boxVec.getY()));
        if(boxVec.getX() < 24) right = getMatrix(new Vector2D(boxVec.getX()+1, boxVec.getY()));
        var conList = castTo(Connection.class, top, bot, left, right);
        if(!conList.isEmpty()) return;

        var carbList = castTo(Carbon.class, top, bot, left, right);
        for (Carbon carbon : carbList) {
            if(carbon.hydroCount == 0) {
                isAble = false;
                break;
            }
        }
        Connection.conList.add(this);

        if(!isAble) return;
        setMatrix(boxVec, this);
        update();
    }

    @Override
    public void canUpdate() {
        canUpdate = true;
    }

    @Override
    public void render() {
        var gc = canvas.getGraphicsContext2D();
        var boundingBox = 10;
        var boxCoordVec = new Vector2D(boxVec.getX() * gridSize, boxVec.getY() * gridSize);

        gc.setStroke(Color.BLACK);

        clear();
        if(value == -1) return;
        if(isVertical) {
            var x = boxCoordVec.getX() + (gridSize / 2F);
            var y1 = boxCoordVec.getY() + boundingBox;
            var y2 = boxCoordVec.getY() + gridSize - boundingBox;

            switch (value) {
                case 1 -> gc.strokeLine(x, y1, x, y2);
                case 2 -> {
                    x -= 2.5;
                    gc.strokeLine(x, y1, x, y2);
                    x += 5;
                    gc.strokeLine(x, y1, x, y2);
                }
                case 3 -> {
                    gc.strokeLine(x, y1, x, y2);
                    x -= 5;
                    gc.strokeLine(x, y1, x, y2);
                    x += 10;
                    gc.strokeLine(x, y1, x, y2);
                }
            }
        }
        else {
            var y = boxCoordVec.getY() + (gridSize / 2F);
            var x1 = boxCoordVec.getX() + boundingBox;
            var x2 = boxCoordVec.getX() + gridSize - boundingBox;
            switch (value) {
                case 1 -> gc.strokeLine(x1, y, x2, y);
                case 2 -> {
                    y -= 2.5;
                    gc.strokeLine(x1, y, x2, y);
                    y += 5;
                    gc.strokeLine(x1, y, x2, y);
                }
                case 3 -> {
                    gc.strokeLine(x1, y, x2, y);
                    y -= 5;
                    gc.strokeLine(x1, y, x2, y);
                    y += 10;
                    gc.strokeLine(x1, y, x2, y);
                }
            }
        }
    }

    @Override
    public void update() {
        if(!canUpdate) return;
        canUpdate = false;

        GridElement top = null, bot = null, left = null, right = null;
        if(boxVec.getY() > 0) top = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()-1));
        if(boxVec.getY() < 18) bot = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()+1));
        if(boxVec.getX() > 0) left = getMatrix(new Vector2D(boxVec.getX()-1, boxVec.getY()));
        if(boxVec.getX() < 24) right = getMatrix(new Vector2D(boxVec.getX()+1, boxVec.getY()));

        if(left == null && right == null) isVertical = bot != null || top != null;
        else isVertical = false;

        if(bot != null) bot.update();
        if(top != null) top.update();
        if(left != null) left.update();
        if(right != null) right.update();

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

    public void increment() {
        if(value == 3) {
            setMatrix(boxVec, null);
            update();
            clear();
        }

        GridElement top = null, bot = null, left = null, right = null;
        if(boxVec.getY() > 0) top = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()-1));
        if(boxVec.getY() < 18) bot = getMatrix(new Vector2D(boxVec.getX(), boxVec.getY()+1));
        if(boxVec.getX() > 0) left = getMatrix(new Vector2D(boxVec.getX()-1, boxVec.getY()));
        if(boxVec.getX() < 24) right = getMatrix(new Vector2D(boxVec.getX()+1, boxVec.getY()));

        var carbList = castTo(Carbon.class, top, bot,left, right);
        for (Carbon carbon : carbList) {
           if(carbon.hydroCount == 0) {
               setMatrix(boxVec, null);
               update();
               clear();
               conList.remove(this);
           }
        }

        value++;
        update();
    }

    private void clear() {
        var gc = canvas.getGraphicsContext2D();
        var boxCoordVec = new Vector2D(boxVec.getX() * gridSize, boxVec.getY() * gridSize);

        gc.clearRect(boxCoordVec.getX() + 2,
                boxCoordVec.getY() + 2,
                gridSize - 5,
                gridSize - 5);
    }

    public static <T> List<T> castTo(Class<T> castClass, GridElement... elems) {
        List<T> retlist = new ArrayList<>();
        for (GridElement elem : elems) {
            if(elem == null) continue;
            try {
                if(elem.isClazz(castClass)) retlist.add(castClass.cast(elem));
            } catch (Exception _) {}
        }
        return retlist;
    }

    public static void updateAll() {
        for (Connection connection : conList) {
            connection.update();
        }
    }
}

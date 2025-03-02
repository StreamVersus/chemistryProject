package pr.vladimir.demo1.Tiles;

import pr.vladimir.demo1.API.Vector2D;

public interface GridElement {
    void canUpdate();
    void render();
    void update();
    boolean isClazz(Class<?> gridClass);
    Vector2D getBoxVec();
}

package pr.vladimir.chemistry.Tiles;

import pr.vladimir.chemistry.API.Vector2D;

public interface GridElement {
    void canUpdate();
    void render();
    void update();
    boolean isClazz(Class<?> gridClass);
    Vector2D getBoxVec();
}

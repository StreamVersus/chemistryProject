package pr.vladimir.demo1.Tiles;

public interface GridElement {
    void canUpdate();
    void render();
    void update();
    boolean isClazz(Class<?> gridClass);
}

package pr.vladimir.chemistry.API;

import pr.vladimir.chemistry.Tiles.*;

public class Presets {
    public static void setPreset(int i) {
        switch (i) {
            case 1 -> first();
            case 2 -> second();
            case 3 -> third();
            case 4 -> fourth();
            case 5 -> fifth();
        }

        for (Carbon value : Carbon.idMap.values()) {
            value.canUpdate();
            value.update();
        }
    }

    public static void first() {
        new Carbon(new Vector2D(8.0, 4.0));
        new Connection(new Vector2D(9.0, 4.0));
        new Carbon(new Vector2D(10.0, 4.0));
        new Connection(new Vector2D(10.0, 5.0));
        new Carbon(new Vector2D(10.0, 6.0));
        new Connection(new Vector2D(11.0, 4.0));
        new Carbon(new Vector2D(12.0, 4.0));
        new Connection(new Vector2D(13.0, 4.0));
        new Carbon(new Vector2D(14.0, 4.0));
        new Connection(new Vector2D(15.0, 4.0), 3);
        new Carbon(new Vector2D(16.0, 4.0));
    }

    public static void second() {
        new Carbon(new Vector2D(15.0, 6.0));
        new Carbon(new Vector2D(9.0, 6.0));
        new Carbon(new Vector2D(11.0, 6.0));
        new Carbon(new Vector2D(11.0, 8.0));
        new Carbon(new Vector2D(13.0, 4.0));
        new Carbon(new Vector2D(13.0, 6.0));
        new Carbon(new Vector2D(15.0, 2.0));
        new Carbon(new Vector2D(15.0, 4.0));

        new Connection(new Vector2D(10.0, 6.0));
        new Connection(new Vector2D(11.0, 7.0));
        new Connection(new Vector2D(12.0, 6.0), 2);
        new Connection(new Vector2D(13.0, 5.0));
        new Connection(new Vector2D(14.0, 6.0));
        new Connection(new Vector2D(15.0, 3.0), 2);
        new Connection(new Vector2D(15.0, 5.0));
    }

    public static void third() {
        new Carbon(new Vector2D(14.0, 8.0));
        new Carbon(new Vector2D(14.0, 6.0));
        new Carbon(new Vector2D(12.0, 6.0));
        new Carbon(new Vector2D(10.0, 6.0));
        new Carbon(new Vector2D(10.0, 4.0));
        new Carbon(new Vector2D(8.0, 6.0));

        new FuncGroup(new Vector2D(16.0, 6.0));

        new Connection(new Vector2D(9.0, 6.0));
        new Connection(new Vector2D(10.0, 5.0));
        new Connection(new Vector2D(11.0, 6.0));
        new Connection(new Vector2D(13.0, 6.0));
        new Connection(new Vector2D(14.0, 7.0));
        new Connection(new Vector2D(15.0, 6.0));
    }

    public static void fourth() {
        new Carbon(new Vector2D(4.0, 6.0));
        new Connection(new Vector2D(5.0, 6.0));
        new Carbon(new Vector2D(6.0, 4.0));
        new Connection(new Vector2D(6.0, 5.0));
        new Carbon(new Vector2D(6.0, 6.0));
        new Connection(new Vector2D(7.0, 6.0));
        new Carbon(new Vector2D(8.0, 6.0));
        new Connection(new Vector2D(8.0, 7.0));
        new Carbon(new Vector2D(8.0, 8.0));
        new Connection(new Vector2D(8.0, 9.0));
        new Carbon(new Vector2D(8.0, 10.0));
        new Connection(new Vector2D(9.0, 6.0));
        new Carbon(new Vector2D(10.0, 4.0));
        new Connection(new Vector2D(10.0, 5.0));
        new Carbon(new Vector2D(10.0, 6.0));
        new Connection(new Vector2D(11.0, 6.0));
        new Carbon(new Vector2D(12.0, 6.0));
        new Connection(new Vector2D(12.0, 7.0));
        new Carbon(new Vector2D(12.0, 8.0));
        new Connection(new Vector2D(13.0, 6.0));
        new Carbon(new Vector2D(14.0, 6.0));
        new Connection(new Vector2D(15.0, 6.0));
        new FuncGroup(new Vector2D(16.0, 6.0), 1);
    }

    public static void fifth() {
        new Carbon(new Vector2D(2.0, 2.0));
        new Connection(new Vector2D(3.0, 2.0));
        new Carbon(new Vector2D(4.0, 0.0));
        new Connection(new Vector2D(4.0, 1.0));
        new Carbon(new Vector2D(4.0, 2.0));
        new Connection(new Vector2D(5.0, 2.0));
        new Carbon(new Vector2D(6.0, 2.0));
        new Connection(new Vector2D(6.0, 3.0));
        new Carbon(new Vector2D(6.0, 4.0));
        new Connection(new Vector2D(7.0, 2.0));
        new Carbon(new Vector2D(8.0, 2.0));
        new Connection(new Vector2D(9.0, 2.0));
        new Carbon(new Vector2D(10.0, 2.0));
        new Connection(new Vector2D(11.0, 2.0));
        new FuncGroup(new Vector2D(12.0, 2.0), 2);
    }
}

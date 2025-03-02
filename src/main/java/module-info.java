module pr.vladimir.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires openj9.jvm;

    exports pr.vladimir.demo1;
    exports pr.vladimir.demo1.Tiles;
    exports pr.vladimir.demo1.API;

    opens pr.vladimir.demo1 to javafx.fxml;
    opens pr.vladimir.demo1.Tiles to javafx.fxml;
    opens pr.vladimir.demo1.API to javafx.fxml;
}
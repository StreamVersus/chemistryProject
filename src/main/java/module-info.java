module pr.vladimir.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires openj9.jvm;


    opens pr.vladimir.demo1 to javafx.fxml;
    exports pr.vladimir.demo1;
    exports pr.vladimir.demo1.Tiles;
    opens pr.vladimir.demo1.Tiles to javafx.fxml;
}
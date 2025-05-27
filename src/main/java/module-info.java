module pr.vladimir.chemistry {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports pr.vladimir.chemistry;
    exports pr.vladimir.chemistry.Tiles;
    exports pr.vladimir.chemistry.API;

    opens pr.vladimir.chemistry to javafx.fxml;
    opens pr.vladimir.chemistry.Tiles to javafx.fxml;
    opens pr.vladimir.chemistry.API to javafx.fxml;
}
module Controller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens Controller to javafx.fxml;
    opens Model to javafx.base;
    opens Service to javafx.base;
    exports Controller;
    exports Service;
    exports Model;
}
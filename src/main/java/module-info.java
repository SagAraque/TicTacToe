module com.ter.tresenraya {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.ter.server;
    opens com.ter.server to javafx.fxml;
    exports com.ter.client;
    opens com.ter.client to javafx.fxml;
}
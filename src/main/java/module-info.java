module com.example.bitego_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.bitego_javafx to javafx.fxml;
    exports com.example.bitego_javafx;
}
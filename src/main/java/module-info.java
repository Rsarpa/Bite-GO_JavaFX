module com.example.bitego_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.mariadb.jdbc;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens com.example.bitego_javafx to javafx.fxml, org.hibernate.orm.core;
    exports com.example.bitego_javafx;
    exports com.example.bitego_javafx.Util;
    opens com.example.bitego_javafx.Util to javafx.fxml, org.hibernate.orm.core;
    exports com.example.bitego_javafx.Controller;
    opens com.example.bitego_javafx.Controller to javafx.fxml, org.hibernate.orm.core;
    exports com.example.bitego_javafx.Model;
    opens com.example.bitego_javafx.Model to org.hibernate.orm.core;
}
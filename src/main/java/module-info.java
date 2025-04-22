module org.example.projetolistatarefas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.projetolistatarefas to javafx.fxml;
    opens org.example.projetolistatarefas.controller to javafx.fxml;
    exports org.example.projetolistatarefas;
}
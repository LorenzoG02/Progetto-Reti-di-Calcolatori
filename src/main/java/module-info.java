module com.example.reti {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.reti to javafx.fxml;
    exports com.example.reti;
}
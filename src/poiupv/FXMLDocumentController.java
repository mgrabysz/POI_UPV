/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import poiupv.Poi;
import poiupv.Point;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private Label posicion;
    @FXML
    private RadioMenuItem marcarPuntoMenuButton;
    @FXML
    private ToggleGroup herramientasToggleGroup;
    @FXML
    private RadioMenuItem MoverMenuButton;
    
    private Point selectedMark;
    @FXML
    private ColorPicker colorPicker;

    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

//    void listClicked(MouseEvent event) {
//        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();
//
//        // Animación del scroll hasta la posicion del item seleccionado
//        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
//        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
//        double scrollH = itemSelected.getPosition().getX() / mapWidth;
//        double scrollV = itemSelected.getPosition().getY() / mapHeight;
//        
//        final Timeline timeline = new Timeline();
//        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
//        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
//        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
//        timeline.getKeyFrames().add(kf);
//        timeline.play();
//
//        // movemos el objto map_pin hasta la posicion del POI
//        double pinW = map_pin.getBoundsInLocal().getWidth();
//        double pinH = map_pin.getBoundsInLocal().getHeight();
//        map_pin.setLayoutX(itemSelected.getPosition().getX());
//        map_pin.setLayoutY(itemSelected.getPosition().getY());
//        pin_info.setText(itemSelected.getDescription());
//        map_pin.setVisible(true);
//    }

//    private void initData() {
//        hm.put("2F", new Poi("2F", "Edificion del DSIC", 325, 225));
//        hm.put("Agora", new Poi("Agora", "Agora", 600, 360));
//        map_listview.getItems().add(hm.get("2F"));
//        map_listview.getItems().add(hm.get("Agora"));
//    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        selectedMark = new Point(1, 1);
        
        colorPicker.setValue(Color.RED);

    }

    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    @FXML
    private void cerrarAplicacion(ActionEvent event) {
        ((Stage)zoom_slider.getScene().getWindow()).close();
    }

    @FXML
    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2022");
        mensaje.showAndWait();
    }

    // ============== Practica ================
    @FXML
    private void ScrollPaneClicked(MouseEvent event) {
        
        if (marcarPuntoMenuButton.isSelected()) {
            marcarPunto(event);
        }
    }
    
    private void marcarPunto(MouseEvent event) {
        
        Point point = new Point(Settings.RADIUS_NORMAL, Settings.RADIUS_BIG);
        point.setStroke(Color.TRANSPARENT);
        point.setStrokeWidth(3);
        point.setFill(colorPicker.getValue());

        zoomGroup.getChildren().add(point);
        
        // draw a point
        Coordinates coordinates = calculateCoordinates(event.getX(), event.getY());
        point.setCenterX(coordinates.getX());
        point.setCenterY(coordinates.getY());
        event.consume();
        
        // event handler for selecting the circle
        EventHandler<MouseEvent> eventHandlerMouseClicked = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                
                // unselect previously selected
                selectedMark.unselect();
                
                // select new
                selectedMark = point;
                selectedMark.select();
            } 
        };  
        
        // event handlers for mouse enter and mouse exit
        EventHandler<MouseEvent> eventHandlerMouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                point.setRadius(Settings.RADIUS_BIG);
            }
        };
        EventHandler<MouseEvent> eventHandlerMouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (point != selectedMark) {
                    point.setRadius(Settings.RADIUS_NORMAL);
                }
                
            }
        };
        
        //Registering the event filters
        point.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked);
        point.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered);
        point.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited);
    }

    @FXML
    private void marcarPuntoClicked(ActionEvent event) {
    }

    @FXML
    private void MoverClicked(ActionEvent event) {
    }

    @FXML
    private void colorPickerClicked(ActionEvent event) {
    }
    
    public Coordinates calculateCoordinates(double initialX, double initialY) {
        /**
         * Function takes coordinates on the scrollPane and calculates
         * coordinates on map taking into account scrollPane shift and zoom
         */
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double zoomScale = zoom_slider.getValue();
        Bounds viewportBounds = map_scrollpane.getViewportBounds();
        
        // find coordinates of top left corner of Viewport
        double shiftX = (mapWidth - viewportBounds.getWidth() / zoomScale) * scrollH;
        double shiftY = (mapHeight - viewportBounds.getHeight() / zoomScale) * scrollV;
        
        double x = initialX / zoomScale + shiftX;
        double y = initialY / zoomScale + shiftY;
        
        Coordinates coordinates = new Coordinates(x, y);
        return coordinates;
    }

}
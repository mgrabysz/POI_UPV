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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
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
    
    private Object selectedMark, drawingMark;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private RadioMenuItem cambiarColorButton;
    @FXML
    private RadioMenuItem seleccionarMenuButton;
    @FXML
    private RadioMenuItem lineaMenuButton;
    @FXML
    private Label grosorLabel;
    @FXML
    private Spinner<Double> grosorSpinner;
    
    public Tool tool;
    @FXML
    private RadioMenuItem circuloMenuButton;
    @FXML
    private RadioMenuItem anotarTextoButton;

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        
        selectedMark = null;    // mark which is selected (point, circle, line)
        drawingMark = null;     // mark which is currently being drawn
        
        colorPicker.setValue(Color.RED);
        
        // arguments(min, max, initialValue, amountToStepBy)
        double d = Settings.LINE_STROKE_NORMAL;
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 8, d, 1);
        grosorSpinner.setValueFactory(valueFactory);
        
        grosorSpinner.disableProperty().bind(Bindings.or(Bindings.or(lineaMenuButton.selectedProperty(), circuloMenuButton.selectedProperty()), anotarTextoButton.selectedProperty()).not());
        
        seleccionarMenuButton.setSelected(true);
        tool = Tool.SELECTION;
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
    
    // ============== Getters & setters =================
    
    public ColorPicker getColorPicker() {
        return colorPicker;
    }
    public Object getSelectedMark() {
        return selectedMark;
    }
    public void setSelectedMark(Object mark) {
        selectedMark = mark;
    }
    public Group getZoomGroup() {
        return zoomGroup;
    }
    public void setDrawingMark(Object mark) {
        drawingMark = mark;
    }
    public Spinner getGrosorSpinner() {
        return grosorSpinner;
    }
    
    // ============== Tools buttons ======================
    @FXML
    private void marcarPuntoClicked(ActionEvent event) {
        tool = Tool.MARK_POINT;
        setAllTransparent();
    }
    @FXML
    private void seleccionarClicked(ActionEvent event) {
        tool = Tool.SELECTION;
        setAllNotTransparent();
    }
    @FXML
    private void cambiarColorButtonClicked(ActionEvent event) {
        tool = Tool.CHANGE_COLOR;
        setAllNotTransparent();
        if (selectedMark instanceof Point) {
            Point selectedPoint = (Point)selectedMark;
            selectedPoint.unselect();
            selectedMark = null;
        }
    }
    @FXML
    private void lineaClicked(ActionEvent event) {
        setAllTransparent();
        tool = Tool.DRAW_LINE;
        double d = Settings.LINE_STROKE_NORMAL;
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 8, d, 1);
        grosorSpinner.setValueFactory(valueFactory);
    }
    @FXML
    private void circuloMenuButtonClicked(ActionEvent event) {
        setAllTransparent();
        tool = Tool.DRAW_CIRCLE;
        double d = Settings.LINE_STROKE_NORMAL;
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 8, d, 1);
        grosorSpinner.setValueFactory(valueFactory);
    }
    @FXML
    private void anotarTextoButtonClicked(ActionEvent event) {
        setAllTransparent();
        tool = Tool.ADD_TEXT;
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(10, 50, 30, 10);
        grosorSpinner.setValueFactory(valueFactory);
    }
    
    @FXML
    private void colorPickerClicked(ActionEvent event) {
        if (selectedMark instanceof Point) {
            Point selectedPoint = (Point)selectedMark;
            selectedPoint.setFill(colorPicker.getValue());
        }
    }
        
    // =============== Event handlers =================
    @FXML
    private void panePressed(MouseEvent event) {
        if (tool == Tool.DRAW_LINE){
            initializeLine(event);
        } else if (tool == Tool.DRAW_CIRCLE) {
            initializeCircle(event);
        }
    }
    @FXML
    private void paneDragged(MouseEvent event) {
        if (tool == Tool.DRAW_LINE) {
            LineExtended line = (LineExtended)drawingMark;
            line.setEndX(event.getX());
            line.setEndY(event.getY());
            event.consume();
        } else if (tool == Tool.DRAW_CIRCLE) {
            CircleExtended circle = (CircleExtended)drawingMark;
            double radius = Math.abs(event.getX() - circle.getInitialX());
            circle.setRadius(radius);
            event.consume();
        }
    }
    @FXML
    private void paneReleased(MouseEvent event) {
        if (drawingMark instanceof LineExtended){
            LineExtended line = (LineExtended)drawingMark;
            line.initializeHandlers();
            drawingMark = null;
        } else if (drawingMark instanceof CircleExtended) {
            CircleExtended circle = (CircleExtended)drawingMark;
            circle.initializeHandlers();
            drawingMark = null;
        }
    }
    
    @FXML
    private void paneClicked(MouseEvent event) {
        if (tool == Tool.MARK_POINT) {
            marcarPunto(event);
        } else if (tool == Tool.ADD_TEXT && drawingMark == null) {
            addText(event);
        } else if (tool == Tool.ADD_TEXT && drawingMark instanceof TextFieldExtended) {
            drawingMark = null;
        }
    }
    
    @FXML
    private void scrollPaneMouseExited(MouseEvent event) {
        
        // blocking drawing line outside the scrollpane
        if (drawingMark instanceof Line){
            LineExtended drawingLine = (LineExtended)drawingMark;
            zoomGroup.getChildren().remove(drawingLine);
        }
    }

    // ========== Additional functions ============
    
    private void marcarPunto(MouseEvent event) {
        
        Point point = new Point(Settings.RADIUS_NORMAL, Settings.RADIUS_BIG, this);
        zoomGroup.getChildren().add(point);
        
        // draw a point
        point.setCenterX(event.getX());
        point.setCenterY(event.getY());
        event.consume();
        
        point.initializeHandlers();
    }

    private void initializeLine(MouseEvent event) {
        double widthNormal = grosorSpinner.getValue();
        double widthBig = Settings.LINE_STROKE_BIG;
        LineExtended line = new LineExtended(event.getX(), event.getY(), event.getX(), event.getY(), widthNormal, widthBig, this);
        
        zoomGroup.getChildren().add(line);
        drawingMark = line;
    }

    private void initializeCircle(MouseEvent event) {
        double widthNormal = grosorSpinner.getValue();
        double widthBig = Settings.LINE_STROKE_BIG;
        CircleExtended circle = new CircleExtended(event.getX(), event.getY(), widthNormal, widthBig, this);
        
        zoomGroup.getChildren().add(circle);
        drawingMark = circle;
    }
    
    private void addText(MouseEvent event) {
        TextFieldExtended textField = new TextFieldExtended(event.getX(), event.getY(), this);
        zoomGroup.getChildren().add(textField);
        textField.requestFocus();
        drawingMark = textField;
    }

    private void setAllTransparent() {
        for (int i=1; i<zoomGroup.getChildren().size(); i++)    // starts with i=1 because on index 0 is Pane
            zoomGroup.getChildren().get(i).setMouseTransparent(true);
    }
    
    private void setAllNotTransparent() {
        for (int i=1; i<zoomGroup.getChildren().size(); i++)    // starts with i=1 because on index 0 is Pane
            zoomGroup.getChildren().get(i).setMouseTransparent(false);
    }

    
    
}

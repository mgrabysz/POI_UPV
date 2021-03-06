/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import poiupv.acciones.Action;
import poiupv.acciones.ActionNewExtreme;
import poiupv.acciones.ActionNewMark;
import poiupv.marcas.CircleExtended;
import poiupv.constantes.Instructions;
import poiupv.marcas.LineExtended;
import poiupv.marcas.Point;
import poiupv.constantes.Settings;
import poiupv.marcas.TextFieldExtended;
import poiupv.constantes.Tool;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private ToggleGroup herramientasToggleGroup;    
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ToggleButton cambiarColorButton;
    @FXML
    private Label grosorLabel;
    @FXML
    private Spinner<Double> grosorSpinner;
    @FXML
    private ToggleButton anotarTextoButton;
    @FXML
    private Label instructionLabel;
    @FXML
    private Pane pane;
    @FXML
    private ToggleButton eliminarMarcaButton;
    @FXML
    private MenuItem limpiarButton;
    @FXML
    private ImageView protractor;
    @FXML
    private ToggleButton activarToggleButton;
    @FXML
    private MenuItem deshacerMenuItem;
    @FXML
    private ToggleButton marcarPuntoButton;
    @FXML
    private ToggleButton pintarLineaButton;
    @FXML
    private ToggleButton marcarExtremosButton;
    @FXML
    private ToggleButton seleccionarColorButton;
    @FXML
    private ToggleButton pintarCirculoButton;
    
    // ================== variables ===========================================
    private Object drawingMark;     // object being currently drawn
    public Tool tool;               // currently selected tool
    private ArrayList<Action> recentActions;    // list storing last 5 done actions, used for UNDO
    private ObservableList<Action> observableRecentActions;
    
    // ================== variables for moving protractor ======
    private double initialX, initialY, baseX, baseY;
    


    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom depender?? de los parametros del 
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
        zoom_slider.setMin(0.8);
        zoom_slider.setMax(1.6);
        zoom_slider.setValue(1.2);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        //=================== new content =================================
        
        // initial settings
        colorPicker.setValue(Color.RED);
        protractor.setVisible(false);
        drawingMark = null;     // mark which is currently being drawn
        protractor.setCursor(Cursor.MOVE);
        
        // setting spinner (changing width of line/circle or size of text)
        // arguments(min, max, initialValue, amountToStepBy)
        double d = Settings.LINE_STROKE_NORMAL;
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 8, d, 1);
        grosorSpinner.setValueFactory(valueFactory);
        
        // binding -> spinner is active only with tools: draw line, draw circle, add text (so is label)
        grosorSpinner.disableProperty().bind(Bindings.or(Bindings.or(pintarLineaButton.selectedProperty(), pintarCirculoButton.selectedProperty()), anotarTextoButton.selectedProperty()).not());
        grosorLabel.disableProperty().bind(Bindings.or(Bindings.or(pintarLineaButton.selectedProperty(), pintarCirculoButton.selectedProperty()), anotarTextoButton.selectedProperty()).not());
        
        // setting selected tool to mark point
        marcarPuntoButton.setSelected(true);
        tool = Tool.MARK_POINT;
        instructionLabel.setText(Instructions.MARK_POINT_INSTR);
        pane.setCursor(Cursor.HAND);
        
        // arrayList storing recent actions to enable undoing them
        recentActions = new ArrayList<>();
        observableRecentActions = FXCollections.observableArrayList(recentActions);
        
        // binding -> undo button is active only if there are actions stored in recent actions
        IntegerBinding actionsGroupSize = Bindings.size(observableRecentActions);
        BooleanBinding groupPopulated = actionsGroupSize.greaterThan(0);       
        deshacerMenuItem.disableProperty().bind(groupPopulated.not());
        
        // binding -> clear all button is active only if there are any objects
        IntegerBinding marksGroupSize = Bindings.size(zoomGroup.getChildren());
        BooleanBinding markGroupPopulated = marksGroupSize.greaterThan(1);       
        limpiarButton.disableProperty().bind(markGroupPopulated.not());
        
        // Tooltips (descriptions displayed when cursor is hovered over button for a while)
        marcarPuntoButton.setTooltip(new Tooltip(Instructions.MARK_POINT_INSTR));
        pintarLineaButton.setTooltip(new Tooltip(Instructions.DRAW_LINE_INSTR));
        pintarCirculoButton.setTooltip(new Tooltip(Instructions.DRAW_CIRCLE_INSTR));
        anotarTextoButton.setTooltip(new Tooltip(Instructions.ADD_TEXT_INSTR));
        marcarExtremosButton.setTooltip(new Tooltip(Instructions.EXTREMES_INSTR));
        seleccionarColorButton.setTooltip(new Tooltip(Instructions.SELECTION_INSTR));
        cambiarColorButton.setTooltip(new Tooltip(Instructions.CHANGE_COLOR_INSTR));
        eliminarMarcaButton.setTooltip(new Tooltip(Instructions.DELETE_INSTR));
        
        // Seting color names in color picker to Spanish
        Locale.setDefault(new Locale("es"));
        
        // Here ands initialize()
        // :O
    }

    // ============== Getters & setters =================
    
    public ColorPicker getColorPicker() {
        return colorPicker;
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
    
    // ====================== Tools and other buttons =========================
    @FXML
    private void marcarPuntoClicked(ActionEvent event) {
        tool = Tool.MARK_POINT;
        pane.setCursor(Cursor.HAND);
        instructionLabel.setText(Instructions.MARK_POINT_INSTR);
        setAllTransparent();
    }
    @FXML
    private void seleccionarClicked(ActionEvent event) {
        tool = Tool.SELECTION;
        pane.setCursor(Cursor.DEFAULT);
        instructionLabel.setText(Instructions.SELECTION_INSTR);
        setAllNotTransparent();
    }
    @FXML
    private void cambiarColorButtonClicked(ActionEvent event) {
        tool = Tool.CHANGE_COLOR;
        pane.setCursor(Cursor.DEFAULT);
        instructionLabel.setText(Instructions.CHANGE_COLOR_INSTR);
        setAllNotTransparent();
    }
    @FXML
    private void lineaClicked(ActionEvent event) {
        tool = Tool.DRAW_LINE;
        pane.setCursor(Cursor.CROSSHAIR);
        instructionLabel.setText(Instructions.DRAW_LINE_INSTR);
        grosorLabel.setText("Grosor:");
        setAllTransparent();
        double d = Settings.LINE_STROKE_NORMAL;
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 8, d, 1);
        grosorSpinner.setValueFactory(valueFactory);
    }
    @FXML
    private void circuloMenuButtonClicked(ActionEvent event) {
        setAllTransparent();
        tool = Tool.DRAW_CIRCLE;
        instructionLabel.setText(Instructions.DRAW_CIRCLE_INSTR);
        grosorLabel.setText("Grosor:");
        pane.setCursor(Cursor.CROSSHAIR);
        double d = Settings.LINE_STROKE_NORMAL;
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 8, d, 1);
        grosorSpinner.setValueFactory(valueFactory);
    }
    @FXML
    private void anotarTextoButtonClicked(ActionEvent event) {
        setAllTransparent();
        tool = Tool.ADD_TEXT;
        instructionLabel.setText(Instructions.ADD_TEXT_INSTR);
        grosorLabel.setText("Tama??o:");
        pane.setCursor(Cursor.DEFAULT);
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(10, 50, 30, 10);
        grosorSpinner.setValueFactory(valueFactory);
    }
    @FXML
    private void eliminarMarcaButtonClicked(ActionEvent event) {
        setAllNotTransparent();
        tool = Tool.DELETE;
        instructionLabel.setText(Instructions.DELETE_INSTR);
        pane.setCursor(Cursor.DEFAULT);
    }
    @FXML
    private void extremosMenuItemClicked(ActionEvent event) {
        setAllTransparent();
        tool = Tool.EXTREMES;
        instructionLabel.setText(Instructions.EXTREMES_INSTR);
        pane.setCursor(Cursor.CROSSHAIR);
    }
        
    @FXML
    private void limpiarButtonClicked(ActionEvent event) {
        
        if (askConfirmation()){
            zoomGroup.getChildren().remove(1, zoomGroup.getChildren().size());
            observableRecentActions.clear();
        }
    }
    
    @FXML
    private void activarToggleButtonClicked(ActionEvent event) {
        if (activarToggleButton.isSelected()) {
            protractor.setVisible(true);
            
            // calculations of coordinates of the top left corner of the Viewport
            double scrollH = map_scrollpane.getHvalue();
            double scrollV = map_scrollpane.getVvalue();
            double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
            double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
            double zoomScale = zoom_slider.getValue();
            Bounds viewportBounds = map_scrollpane.getViewportBounds();

            double shiftX = (mapWidth - viewportBounds.getWidth() / zoomScale) * scrollH;
            double shiftY = (mapHeight - viewportBounds.getHeight() / zoomScale) * scrollV;

            // translate the protractor
            protractor.setTranslateX(30 + shiftX);
            protractor.setTranslateY(30 + shiftY);
            
        } else {
            protractor.setVisible(false);
        }
    }
    
    @FXML
    private void deshacerClicked(ActionEvent event) {
        // invert last action in list of recent actions
        Action lastAction = observableRecentActions.get(observableRecentActions.size() - 1);
        lastAction.undo();
        observableRecentActions.remove(lastAction);
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
        
    // =============== Event handlers ================================
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
            finalizeLine();
        } else if (drawingMark instanceof CircleExtended) {
            finalizeCircle();
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
        } else if (tool == Tool.EXTREMES) {
            markExtremes(event);
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
    
    @FXML
    private void protractorReleased(MouseEvent event) {
        
    }

    @FXML
    private void protractorDragged(MouseEvent event) {
        double shiftX = event.getSceneX() - initialX;
        double shiftY = event.getSceneY() - initialY;
        protractor.setTranslateX(baseX + shiftX);
        protractor.setTranslateY(baseY + shiftY);
        event.consume();
    }

    @FXML
    private void protractorClicked(MouseEvent event) {
        event.consume();
    }

    @FXML
    private void protractorPressed(MouseEvent event) {
        initialX = event.getSceneX();
        initialY = event.getSceneY();
        baseX = protractor.getTranslateX();
        baseY = protractor.getTranslateY();
        event.consume();
    }

    @FXML
    private void SliderScrolled(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            double sliderVal = zoom_slider.getValue();
            zoom_slider.setValue(sliderVal += 0.1);
        } else {
            double sliderVal = zoom_slider.getValue();
            zoom_slider.setValue(sliderVal -= 0.1);
        }
    }
    
    // ================== Additional functions =====================
    
    private void marcarPunto(MouseEvent event) {
        Point point = new Point(Settings.RADIUS_NORMAL, Settings.RADIUS_BIG, this);
        zoomGroup.getChildren().add(point);
        ActionNewMark action = new ActionNewMark(this, point);      // saving the action in order to be able to UNDO
        saveAction(action);
        
        // draw a point
        point.setCenterX(event.getX());
        point.setCenterY(event.getY());
        event.consume();
        
        point.initializeHandlers();
    }

    private void initializeLine(MouseEvent event) {
        // Function called when mouse pressed in drawing line mode
        double widthNormal = grosorSpinner.getValue();
        double widthBig = Settings.LINE_STROKE_BIG;
        LineExtended line = new LineExtended(event.getX(), event.getY(), event.getX(), event.getY(), widthNormal, widthBig, this);
        
        zoomGroup.getChildren().add(line);
        drawingMark = line;
    }
    private void finalizeLine() {
        // Function called when mouse released
        LineExtended line = (LineExtended)drawingMark;
        line.initializeHandlers();
        ActionNewMark action = new ActionNewMark(this, line);      // saving the action in order to be able to UNDO
        saveAction(action);
        drawingMark = null;
    }
    
    private void initializeCircle(MouseEvent event) {
        // Function called when mouse pressed
        double widthNormal = grosorSpinner.getValue();
        double widthBig = Settings.LINE_STROKE_BIG;
        CircleExtended circle = new CircleExtended(event.getX(), event.getY(), widthNormal, widthBig, this);
        
        zoomGroup.getChildren().add(circle);
        drawingMark = circle;
        pane.setCursor(Cursor.H_RESIZE);
    }
    private void finalizeCircle() {
        // Function called when mouse released
        CircleExtended circle = (CircleExtended)drawingMark;
        circle.initializeHandlers();
        ActionNewMark action = new ActionNewMark(this, circle);      // saving the action in order to be able to UNDO
        saveAction(action);
        drawingMark = null;
        pane.setCursor(Cursor.CROSSHAIR);
    }
    
    private void addText(MouseEvent event) {
        TextFieldExtended textField = new TextFieldExtended(event.getX(), event.getY(), this);
        zoomGroup.getChildren().add(textField);
        textField.requestFocus();
        drawingMark = textField;
    }
    
    private void markExtremes(MouseEvent event) {
        double widthNormal = 3;
        double widthBig = Settings.LINE_STROKE_BIG;
        double x = event.getX();
        double y = event.getY();
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        
        LineExtended lineH = new LineExtended(8, y, mapWidth-8, y, widthNormal, widthBig, this);
        LineExtended lineV = new LineExtended(x, 8, x, mapHeight-8, widthNormal, widthBig, this);
        lineH.initializeHandlers();
        lineV.initializeHandlers();
        zoomGroup.getChildren().add(lineH);
        zoomGroup.getChildren().add(lineV);
        
        ActionNewExtreme action = new ActionNewExtreme(this, lineV, lineH);
        saveAction(action);
    }

    private void setAllTransparent() {
        for (int i=1; i<zoomGroup.getChildren().size(); i++)    // starts with i=1 because on index 0 is Pane
            zoomGroup.getChildren().get(i).setMouseTransparent(true);
    }
    
    private void setAllNotTransparent() {
        for (int i=1; i<zoomGroup.getChildren().size(); i++)    // starts with i=1 because on index 0 is Pane
            zoomGroup.getChildren().get(i).setMouseTransparent(false);
    }
    
    public void saveAction(Action action) {
        if (observableRecentActions.size() == Settings.ACTIONS_SAVED) {
            // if list is full, remove item from head
            observableRecentActions.remove(0);   
        }
        observableRecentActions.add(action);
    }
    
    public boolean askConfirmation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acci??n");
        alert.setHeaderText("Acci??n de limpiar la carta es irreversible");
        alert.setContentText("??Seguro que quieres continuar?");
        
        ButtonType buttonContinue = new ButtonType("Continuar");
        ButtonType buttonCancel = new ButtonType("Anular");
        
        alert.getButtonTypes().setAll(buttonCancel, buttonContinue);
        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent() && result.get() == buttonContinue);
        
    }
}

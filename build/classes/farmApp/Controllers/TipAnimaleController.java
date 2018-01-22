/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Controllers;

import farmApp.Controllers.util.ConvertorData;

import farmApp.DAO.TipAnimaleDAOInterface;
import farmApp.DAO.implement.TipAnimaleDAOImplement;
import farmApp.Entities.TipAnimale;

import farmApp.Controllers.util.Role;
import farmApp.util.Mesaje;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.LocalDateTimeTextField;

/**
 * FXML Controller class
 *
 * @author Ghetto
 */
public class TipAnimaleController implements Initializable {

    private static final Logger LOG = Logger.getLogger(TipAnimaleController.class.getName());

    @FXML
    private Pane tipAnimalePanel;
    @FXML
    private TextField tipAnimaleTip;
    @FXML
    private TextField tipAnimaleId;
    @FXML
    private CheckBox tipAnimaleActiveDa;
    @FXML
    private CheckBox tipAnimaleActiveNu;
    @FXML
    private LocalDateTimeTextField tipAnimaleDataCreare;
    @FXML
    private LocalDateTimeTextField tipAnimaleDataModificare;
    @FXML
    private TableView<TipAnimale> tipAnimaleTable;
    @FXML
    private Button tipAnimaleButtonAdauga;
    @FXML
    private Button tipAnimaleButtonModifica;
    @FXML
    private Button tipAnimaleButtonCauta;
    @FXML
    private Button tipAnimaleButtonSterge;
    @FXML
    private Button tipAnimaleButtonGoleste;
    @FXML
    private PieChart tipAnimalePieChart;

    @FXML
    private TableColumn<TipAnimale, Integer> columnId;
    @FXML
    private TableColumn<TipAnimale, Boolean> columnActiv;
    @FXML
    private TableColumn<TipAnimale, LocalDateTime> columnDataCreare;
    @FXML
    private TableColumn<TipAnimale, String> columnTipul;
    @FXML
    private TableColumn<TipAnimale, LocalDateTime> columnDataModificare;

    TipAnimaleDAOInterface tipAnimaleDao;

    Mesaje m = new Mesaje();
    @FXML
    private Label roleLabel;

    int role;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        clearForm();

        try {
            tipAnimaleDao = new TipAnimaleDAOImplement();

            Collection<TipAnimale> tipAnimaleList = tipAnimaleDao.findAll();

            columnId.setCellValueFactory(new PropertyValueFactory<TipAnimale, Integer>("animalId"));
            columnActiv.setCellValueFactory(new PropertyValueFactory<TipAnimale, Boolean>("active"));
            columnDataCreare.setCellValueFactory(new PropertyValueFactory<TipAnimale, LocalDateTime>("createDate"));
            columnTipul.setCellValueFactory(new PropertyValueFactory<TipAnimale, String>("tipAnimal"));
            columnDataModificare.setCellValueFactory(new PropertyValueFactory<TipAnimale, LocalDateTime>("updateDate"));

            tipAnimaleTable.setItems(FXCollections.observableArrayList(tipAnimaleList));

            role = Role.getInstance().getX();

            if (role == 0) {
                roleLabel.setText("Administrator");
            } else if (role == 1) {
                roleLabel.setText("Utilizator");
            } else if (role == 2) {
                roleLabel.setText("Vizitator");
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(TipAnimaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnAddClick(ActionEvent event) {

        try {

            role = Role.getInstance().getX();
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {

                tipAnimaleDao.add(readForm());
                clearForm();
                m.adaugat();
                TipAnimale lastTip = tipAnimaleDao.findLast();
                tipAnimaleTable.getItems().add(lastTip);
            }
        } catch (Exception e) {
            m.checkInput();
        }

    }

    @FXML
    private void btnModClick(ActionEvent event) {

        try {

            role = Role.getInstance().getX();
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {

                TipAnimale tipDB = tipAnimaleDao.findById(readForm().getAnimalId());

                if (tipDB != null) {

                    TipAnimale tip = readForm();

                    tipDB.setActive(tip.isActive());
                    tipDB.setAnimalId(tip.getAnimalId());
                    tipDB.setCreateDate(tip.getCreateDate());
                    tipDB.setTipAnimal(tip.getTipAnimal());
                    tipDB.setUpdateDate(tip.getUpdateDate());

                    tipAnimaleDao.update(tip);
                    clearForm();
                    m.modificat();
                    Collection<TipAnimale> tipAnimaleList = tipAnimaleDao.findAll();
                    tipAnimaleTable.setItems(FXCollections.observableArrayList(tipAnimaleList));

                }
            }

        } catch (Exception e) {

            m.checkInput();

        }

    }

    @FXML
    private void btnCautaClick(ActionEvent event) {

        try {
            TipAnimale tip = tipAnimaleDao.findById(readForm().getAnimalId());

            if (tip != null) {
                fillForm(tip);
            } else {

                m.notFound();

                clearForm();
            }

        } catch (Exception e) {

            m.notFound();

        }

    }

    @FXML
    private void btnStergeClick(ActionEvent event) {

        try {

            role = Role.getInstance().getX();
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {

                TipAnimale tipDB = tipAnimaleDao.findById(readForm().getAnimalId());

                if (tipDB != null) {

                    tipAnimaleDao.delete(tipDB);
                    clearForm();
                    m.sters();
                    Collection<TipAnimale> tipAnimaleList = tipAnimaleDao.findAll();
                    tipAnimaleTable.setItems(FXCollections.observableArrayList(tipAnimaleList));

                } else {

                    m.notFound();

                }
            }
        } catch (Exception e) {

            m.notFound();

        }

    }

    @FXML
    private void btnGolesteClick(ActionEvent event) {

        clearForm();

    }

    private TipAnimale readForm() {

        TipAnimale tip = new TipAnimale();

         ConvertorData dc = new ConvertorData();

        tip.setAnimalId(Integer.parseInt(tipAnimaleId.getText()));
        if (tipAnimaleActiveDa.isSelected()) {
            tip.setActive(true);
        }

        if (tipAnimaleActiveNu.isSelected()) {
            tip.setActive(false);
        }

        tip.setTipAnimal(tipAnimaleTip.getText());

        tip.setCreateDate(dc.convertToDatabaseColumn(tipAnimaleDataCreare.getLocalDateTime()));
        tip.setUpdateDate(dc.convertToDatabaseColumn(tipAnimaleDataModificare.getLocalDateTime()));

        return tip;
    }

    private void clearForm() {

        tipAnimaleId.setText("0");
        tipAnimaleActiveNu.setSelected(false);
        tipAnimaleActiveDa.setSelected(false);
        tipAnimaleTip.setText("");
        tipAnimaleDataCreare.setLocalDateTime(LocalDateTime.now());
        tipAnimaleDataModificare.setLocalDateTime(LocalDateTime.now());

    }

    private void fillForm(TipAnimale tip) {

          ConvertorData dc = new ConvertorData();

        tipAnimaleId.setText("" + tip.getAnimalId());

        if (tip.isActive()) {
            tipAnimaleActiveDa.setSelected(true);
        } else {

            tipAnimaleActiveNu.setSelected(true);
        }

        tipAnimaleTip.setText(tip.getTipAnimal());
        tipAnimaleDataCreare.setLocalDateTime(dc.convertToEntityAttribute(tip.getCreateDate()));
        tipAnimaleDataModificare.setLocalDateTime(dc.convertToEntityAttribute(tip.getUpdateDate()));

    }

    @FXML
    private void checkDa(ActionEvent event) {

        if (tipAnimaleActiveDa.isSelected()) {
            tipAnimaleActiveNu.setSelected(false);
        }

    }

    @FXML
    private void checkNu(ActionEvent event) {

        if (tipAnimaleActiveNu.isSelected()) {
            tipAnimaleActiveDa.setSelected(false);
        }

    }

}

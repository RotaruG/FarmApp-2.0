/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Controllers;

import farmApp.Controllers.util.ConvertorData;

import farmApp.DAO.PersonalDAOInterface;
import farmApp.DAO.implement.PersonalDAOImplement;
import farmApp.Entities.Personal;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxtras.scene.control.LocalDateTimeTextField;

/**
 * FXML Controller class
 *
 * @author Ghetto
 */
public class personalTabController implements Initializable {

    private static final Logger LOG = Logger.getLogger(personalTabController.class.getName());

    @FXML
    private TextField personalId;
    @FXML
    private TextField personalNume;
    @FXML
    private TextField personalPreume;
    @FXML
    private TextField personalFunctia;
    @FXML
    private CheckBox personalActivDa;
    @FXML
    private CheckBox personalActivNu;
    @FXML
    private LocalDateTimeTextField personalDataAngajare;
    @FXML
    private LocalDateTimeTextField personalDataConcediere;
    @FXML
    private TableView<Personal> personalTable;
    @FXML
    private Button personalButtonAdauga;
    @FXML
    private Button personalButtonModifica;
    @FXML
    private Button personalButtonCauta;
    @FXML
    private Button personalButtonSterge;
    @FXML
    private Button personalButtonGoleste;
    @FXML
    private TableColumn<Personal, Integer> columnId;
    @FXML
    private TableColumn<Personal, Boolean> columnActiv;
    @FXML
    private TableColumn<Personal, String> columnNume;
    @FXML
    private TableColumn<Personal, String> columnPrenume;
    @FXML
    private TableColumn<Personal, String> columnFunctia;
    @FXML
    private TableColumn<Personal, LocalDateTime> columnDataAngajare;
    @FXML
    private TableColumn<Personal, LocalDateTime> columnDataConcediere;

    PersonalDAOInterface personalDao;

    Mesaje m = new Mesaje();

    int role;
    @FXML
    private Label roleLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            clearFormPersonal();

            personalDao = new PersonalDAOImplement();

            Collection<Personal> personalList = personalDao.findAll();

            Personal lastPerson = personalDao.findLast();

            columnId.setCellValueFactory(new PropertyValueFactory<Personal, Integer>("id"));
            columnActiv.setCellValueFactory(new PropertyValueFactory<Personal, Boolean>("statut"));
            columnNume.setCellValueFactory(new PropertyValueFactory<Personal, String>("nume"));
            columnPrenume.setCellValueFactory(new PropertyValueFactory<Personal, String>("prenume"));
            columnFunctia.setCellValueFactory(new PropertyValueFactory<Personal, String>("functia"));
            columnDataAngajare.setCellValueFactory(new PropertyValueFactory<Personal, LocalDateTime>("dataAngajare"));
            columnDataConcediere.setCellValueFactory(new PropertyValueFactory<Personal, LocalDateTime>("dataConcediere"));

            personalTable.setItems(FXCollections.observableArrayList(personalList));

            role = Role.getInstance().getX();

            if (role == 0) {
                roleLabel.setText("Administrator");
            } else if (role == 1) {
                roleLabel.setText("Utilizator");
            } else if (role == 2) {
                roleLabel.setText("Vizitator");
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(personalTabController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnAdauga(ActionEvent event) {

        role = Role.getInstance().getX();

        try {
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {
                personalDao.add(readformPersonal());
                m.adaugat();
                clearFormPersonal();
                Personal lastPerson = personalDao.findLast();
                personalTable.getItems().add(lastPerson);
            }
        } catch (Exception ex) {

            m.checkInput();

        }

    }

    @FXML
    private void btnModifica(ActionEvent event) throws SQLException {
        role = Role.getInstance().getX();
        try {
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {

                Personal pbd = personalDao.findById(readformPersonal().getId());
                if (pbd != null) {
                    Personal p = readformPersonal();

                    pbd.setDataAngajare(p.getDataAngajare());
                    pbd.setDataConcediere(p.getDataConcediere());
                    pbd.setFunctia(p.getFunctia());
                    pbd.setId(p.getId());
                    pbd.setNume(p.getNume());
                    pbd.setPrenume(p.getPrenume());
                    pbd.setStatut(p.getStatut());

                    personalDao.update(p);
                    m.modificat();
                    Collection<Personal> personalList = personalDao.findAll();
                    personalTable.setItems(FXCollections.observableArrayList(personalList));
                    clearFormPersonal();

                }
            }
        } catch (SQLException ex) {
            m.checkInput();

        }

    }

    @FXML
    private void btnCauta(ActionEvent event) throws SQLException {

        try {
            Personal p = new Personal();
            p = personalDao.findById(readformPersonal().getId());
            if (p != null) {
                fillForm(p);
            } else {

                m.notFound();

            }

        } catch (Exception e) {
            m.notFound();
            clearFormPersonal();
        }

    }

    @FXML
    private void btnSterge(ActionEvent event) throws SQLException {

        role = Role.getInstance().getX();

        try {

            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {
                Personal p = new Personal();
                p = personalDao.findById(readformPersonal().getId());
                if (p != null) {
                    personalDao.delete(p);
                    m.sters();
                    clearFormPersonal();
                } else {

                    m.notFound();

                }
            }
        } catch (Exception ex) {
            m.notFound();
            clearFormPersonal();
        }

    }

    @FXML
    private void btnGoleste(ActionEvent event) throws SQLException {

        clearFormPersonal();

    }

    private Personal readformPersonal() {

        Personal p = new Personal();

        ConvertorData dc = new ConvertorData();

        p.setDataAngajare(dc.convertToDatabaseColumn(personalDataAngajare.getLocalDateTime()));
        p.setDataConcediere(dc.convertToDatabaseColumn(personalDataConcediere.getLocalDateTime()));
        p.setFunctia(personalFunctia.getText());
        p.setId(Integer.parseInt(personalId.getText()));
        p.setNume(personalNume.getText());
        p.setPrenume(personalPreume.getText());

        if (personalActivDa.isSelected()) {
            p.setStatut(true);
        } else if (personalActivNu.isSelected()) {
            p.setStatut(false);
        }

        return p;

    }

    private void fillForm(Personal p) {

        ConvertorData dc = new ConvertorData();

        personalPreume.setText(p.getPrenume());
        personalNume.setText(p.getNume());
        personalId.setText("" + p.getId());
        personalFunctia.setText(p.getFunctia());
        personalDataConcediere.setLocalDateTime(dc.convertToEntityAttribute(p.getDataConcediere()));
        personalDataAngajare.setLocalDateTime(dc.convertToEntityAttribute(p.getDataAngajare()));
        if (p.getStatut()) {
            personalActivDa.setSelected(true);
        } else {

            personalActivNu.setSelected(true);

        }

    }

    private void clearFormPersonal() throws SQLException {

        personalPreume.setText("");
        personalNume.setText("");
        personalId.setText("0");
        personalFunctia.setText("");
        personalDataConcediere.setLocalDateTime(LocalDateTime.now());
        personalDataAngajare.setLocalDateTime(LocalDateTime.now());
        personalActivDa.setSelected(false);
        personalActivNu.setSelected(false);

    }

    @FXML
    private void activDa(ActionEvent event) {

        if (personalActivDa.isSelected()) {
            personalActivNu.setSelected(false);
        }

    }

    @FXML
    private void activNu(ActionEvent event) {
        if (personalActivNu.isSelected()) {
            personalActivDa.setSelected(false);
        }
    }

}

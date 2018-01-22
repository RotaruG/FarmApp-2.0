/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Controllers;

import farmApp.Controllers.util.Role;
import farmApp.DAO.AmplasareDAOInterface;
import farmApp.DAO.implement.AmplasareaDAOImplement;
import farmApp.Entities.Amplasarea;
import farmApp.util.Mesaje;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Ghetto
 */
public class AmplasarePanelController implements Initializable {

    private static final Logger LOG = Logger.getLogger(AmplasarePanelController.class.getName());

    @FXML
    private Pane amplasarePanel;
    @FXML
    private TextField amplasareId;
    @FXML
    private TextField amplasareCladirea;
    @FXML
    private TextField amplasareCamera;
    @FXML
    private TextField amplasareDetaliul1;
    @FXML
    private TextField amplasareDetaliul2;
    @FXML
    private TextField amplasareSector;
    @FXML
    private TextField amplasareLocatia;
    @FXML
    private TextField amplasareDetaliul3;
    @FXML
    private Button buttonAmplasareAdauga;
    @FXML
    private Button buttonAmplasareModifica;
    @FXML
    private Button buttonAmplasareCauta;
    @FXML
    private Button buttonAmplasareSterge;
    @FXML
    private Button buttonAmplasareGoleste;
    @FXML
    private TableView<Amplasarea> amplasareTable;
    @FXML
    private TableColumn<Amplasarea, Integer> columnId;
    @FXML
    private TableColumn<Amplasarea, String> columnCladirea;
    @FXML
    private TableColumn<Amplasarea, String> columnCamera;
    @FXML
    private TableColumn<Amplasarea, String> columnDetaliul1;
    @FXML
    private TableColumn<Amplasarea, String> columnDetaliul2;
    @FXML
    private TableColumn<Amplasarea, String> columnDetaliul3;
    @FXML
    private TableColumn<Amplasarea, String> columnLocatia;
    @FXML
    private TableColumn<Amplasarea, String> columnSector;

    AmplasareDAOInterface amplasareaDao;

    Mesaje m = new Mesaje();

    int role;
    @FXML
    private Label roleLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        clearForm();

        try {
            amplasareaDao = new AmplasareaDAOImplement();

            Collection<Amplasarea> amplasareaList = amplasareaDao.findAll();

            columnId.setCellValueFactory(new PropertyValueFactory<Amplasarea, Integer>("id"));
            columnCladirea.setCellValueFactory(new PropertyValueFactory<Amplasarea, String>("building"));
            columnCamera.setCellValueFactory(new PropertyValueFactory<Amplasarea, String>("cell"));
            columnDetaliul1.setCellValueFactory(new PropertyValueFactory<Amplasarea, String>("firstDetail"));
            columnDetaliul2.setCellValueFactory(new PropertyValueFactory<Amplasarea, String>("secondDetail"));
            columnDetaliul3.setCellValueFactory(new PropertyValueFactory<Amplasarea, String>("thirdDetail"));
            columnLocatia.setCellValueFactory(new PropertyValueFactory<Amplasarea, String>("shortLocation"));
            columnSector.setCellValueFactory(new PropertyValueFactory<Amplasarea, String>("sector"));

            amplasareTable.setItems(FXCollections.observableArrayList(amplasareaList));

            role = Role.getInstance().getX();
            
            if (role==0) {
               roleLabel.setText("Administrator");
            }else if(role==1){
               roleLabel.setText("Utilizator");
            }else if(role==2){
               roleLabel.setText("Vizitator");
            }
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(AmplasarePanelController.class.getName()).log(Level.SEVERE, null, ex);
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

                amplasareaDao.add(readForm());
                clearForm();

                Amplasarea lastAmpl = amplasareaDao.findLast();
                amplasareTable.getItems().add(lastAmpl);

                m.adaugat();

            }
        } catch (Exception e) {

            m.checkInput();

        }

    }

    @FXML
    private void btnModifica(ActionEvent event) {

        role = Role.getInstance().getX();

        try {

            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {
                Amplasarea aDB = amplasareaDao.findById(readForm().getId());

                if (aDB != null) {
                    Amplasarea a = readForm();

                    aDB.setBuilding(a.getBuilding());
                    aDB.setCell(a.getCell());
                    aDB.setFirstDetail(a.getFirstDetail());
                    aDB.setId(a.getId());
                    aDB.setSecondDetail(a.getSecondDetail());
                    aDB.setSector(a.getSector());
                    aDB.setShortLocation(a.getShortLocation());
                    aDB.setThirdDetail(a.getThirdDetail());

                    amplasareaDao.update(a);

                    clearForm();

                    m.modificat();

                    Collection<Amplasarea> amplasareaList = amplasareaDao.findAll();
                    amplasareTable.setItems(FXCollections.observableArrayList(amplasareaList));

                } else {

                    m.notFound();

                }
            }

        } catch (Exception e) {

            m.checkInput();

        }

    }

    @FXML
    private void btnCauta(ActionEvent event) {

        try {
            Amplasarea ampl = new Amplasarea();
            ampl = amplasareaDao.findById(readForm().getId());
            if (ampl != null) {
                fillForm(ampl);
            } else {

                m.notFound();

            }

        } catch (Exception e) {

            m.notFound();

        }

    }

    @FXML
    private void btnSterge(ActionEvent event) {

        role = Role.getInstance().getX();

        try {

            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {

                Amplasarea a = amplasareaDao.findById(readForm().getId());

                if (a != null) {

                    amplasareaDao.delete(a);

                    clearForm();

                    m.sters();

                    Collection<Amplasarea> amplasareaList = amplasareaDao.findAll();
                    amplasareTable.setItems(FXCollections.observableArrayList(amplasareaList));

                }
            }
        } catch (Exception e) {

            m.notFound();

        }

    }

    @FXML
    private void btnGoleste(ActionEvent event) {

        clearForm();

    }

    private Amplasarea readForm() {

        Amplasarea a = new Amplasarea();

        a.setBuilding(amplasareCladirea.getText());
        a.setCell(amplasareCamera.getText());
        a.setFirstDetail(amplasareDetaliul1.getText());
        a.setId(Integer.parseInt(amplasareId.getText()));
        a.setSecondDetail(amplasareDetaliul3.getText());
        a.setSector(amplasareSector.getText());
        a.setShortLocation(amplasareCladirea.getText() + "-" + amplasareSector.getText() + "-"
                + amplasareCamera.getText() + "-" + amplasareDetaliul1.getText() + "-" + amplasareDetaliul2.getText() + "-" + amplasareDetaliul3.getText());
        a.setThirdDetail(amplasareDetaliul3.getText());

        return a;

    }

    private Amplasarea fillForm(Amplasarea ampl) {

        amplasareCladirea.setText(ampl.getBuilding().trim());
        amplasareCamera.setText(ampl.getCell().trim());
        amplasareDetaliul1.setText(ampl.getFirstDetail().trim());
        amplasareDetaliul2.setText(ampl.getSecondDetail().trim());
        amplasareDetaliul3.setText(ampl.getThirdDetail().trim());
        amplasareSector.setText(ampl.getSector().trim());
        amplasareLocatia.setText(ampl.getShortLocation().trim());
        amplasareId.setText("" + ampl.getId());

        return ampl;

    }

    private void clearForm() {

        amplasareCladirea.setText("");
        amplasareCamera.setText("");
        amplasareDetaliul1.setText("");
        amplasareDetaliul2.setText("");
        amplasareDetaliul3.setText("");
        amplasareSector.setText("");
        amplasareLocatia.setText("");
        amplasareId.setText("0");

    }

}

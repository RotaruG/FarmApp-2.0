/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Controllers;

import farmApp.Controllers.util.ConvertorData;

import farmApp.DAO.VeterinarDAOInterface;
import farmApp.DAO.implement.VeterinarDAOImplement;
import farmApp.Entities.Veterinar;

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
public class VetPanelController implements Initializable {

    private static final Logger LOG = Logger.getLogger(VetPanelController.class.getName());

    @FXML
    private Pane vetPanel;
    @FXML
    private TableView<Veterinar> vetTable;
    @FXML
    private TextField vetID;
    @FXML
    private TextField vetAction;
    @FXML
    private TextField vetInfo;
    @FXML
    private TextField vetName;
    @FXML
    private TextField vetIdAnimal;
    @FXML
    private LocalDateTimeTextField vetData;
    @FXML
    private Button vetButtonAdauga;
    @FXML
    private Button vetButtonModifica;
    @FXML
    private Button vetButtonCauta;
    @FXML
    private Button vetButtonSterge;
    @FXML
    private Button vetButtonGoleste;
    @FXML
    private TableColumn<Veterinar, Integer> columnId;
    @FXML
    private TableColumn<Veterinar, LocalDateTime> columnData;
    @FXML
    private TableColumn<Veterinar, String> columnActiunea;
    @FXML
    private TableColumn<Veterinar, String> columnInfo;
    @FXML
    private TableColumn<Veterinar, String> columnVet;
    @FXML
    private TableColumn<Veterinar, Integer> columnIDAnimal;

    VeterinarDAOInterface vetDao;

    Mesaje m = new Mesaje();

    int role;
    @FXML
    private Label roleLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            vetDao = new VeterinarDAOImplement();

            clearForm();

            Collection<Veterinar> vetList = vetDao.findAll();

            columnId.setCellValueFactory(new PropertyValueFactory<Veterinar, Integer>("animalStockId"));
            columnData.setCellValueFactory(new PropertyValueFactory<Veterinar, LocalDateTime>("created"));
            columnActiunea.setCellValueFactory(new PropertyValueFactory<Veterinar, String>("action"));
            columnInfo.setCellValueFactory(new PropertyValueFactory<Veterinar, String>("description"));
            columnVet.setCellValueFactory(new PropertyValueFactory<Veterinar, String>("username"));
            columnIDAnimal.setCellValueFactory(new PropertyValueFactory<Veterinar, Integer>("stockId"));

            vetTable.setItems(FXCollections.observableArrayList(vetList));
            role = Role.getInstance().getX();
             if (role==0) {
               roleLabel.setText("Administrator");
            }else if(role==1){
               roleLabel.setText("Utilizator");
            }else if(role==2){
               roleLabel.setText("Vizitator");
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(VetPanelController.class.getName()).log(Level.SEVERE, null, ex);
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

                vetDao.add(readForm());
                m.adaugat();
                clearForm();
                Veterinar lastVet = vetDao.findLast();
                vetTable.getItems().add(lastVet);
            }
        } catch (Exception e) {

            m.checkInput();

        }

    }

    @FXML
    private void btnModifica(ActionEvent event) {

        try {

            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {
                Veterinar vet = vetDao.findById(readForm().getAnimalStockId());
                if (vet != null) {
                    LOG.info("IN IF");
                    Veterinar v = readForm();
                    vet.setAction(v.getAction());
                    vet.setAnimalStockId(v.getAnimalStockId());
                    vet.setCreated(v.getCreated());
                    vet.setDescription(v.getDescription());
                    vet.setStockId(v.getStockId());
                    vet.setUsername(v.getUsername());

                    vetDao.update(vet);
                    m.modificat();
                    clearForm();
                    Collection<Veterinar> vetList = vetDao.findAll();
                    vetTable.setItems(FXCollections.observableArrayList(vetList));

                }
            }
        } catch (Exception e) {
            m.notFound();
            clearForm();
        }

    }

    @FXML
    private void btnCauta(ActionEvent event) {

        try {
            Veterinar vet = vetDao.findById(readForm().getAnimalStockId());

            if (vet != null) {

                fillForm(vet);

            } else {

                m.notFound();

            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnSterge(ActionEvent event) {

        try {

            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {

                Veterinar vet = vetDao.findById(readForm().getAnimalStockId());

                if (vet != null) {

                    vetDao.delete(vet);
                    m.sters();
                    clearForm();
                    Collection<Veterinar> vetList = vetDao.findAll();
                    vetTable.setItems(FXCollections.observableArrayList(vetList));

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

    private Veterinar readForm() {

        Veterinar vet = new Veterinar();

        ConvertorData dc = new ConvertorData();

        vet.setAction(vetAction.getText());
        vet.setAnimalStockId(Integer.parseInt(vetID.getText()));
        vet.setCreated(dc.convertToDatabaseColumn(vetData.getLocalDateTime()));
        vet.setDescription(vetInfo.getText());
        vet.setStockId(Integer.parseInt(vetIdAnimal.getText()));
        vet.setUsername(vetName.getText());

        return vet;

    }

    private void clearForm() {

        vetAction.setText("");
        vetID.setText("0");
        vetIdAnimal.setText("0");
        vetInfo.setText("");
        vetData.setLocalDateTime(LocalDateTime.now());
        vetName.setText("");

    }

    private void fillForm(Veterinar vet) {

          ConvertorData dc = new ConvertorData();

        vetAction.setText(vet.getAction());
        vetID.setText("" + vet.getAnimalStockId());
        vetIdAnimal.setText("" + vet.getStockId());
        vetInfo.setText(vet.getDescription());
        vetData.setLocalDateTime(dc.convertToEntityAttribute(vet.getCreated()));
        vetName.setText(vet.getUsername());

    }

}

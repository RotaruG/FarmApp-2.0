/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Controllers;


import farmApp.Controllers.util.ConvertorData;
import farmApp.DAO.UsersDAOInterface;
import farmApp.DAO.implement.UsersDAOImplement;
import farmApp.Entities.Users;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.LocalDateTimeTextField;
import farmApp.Controllers.util.Role;
/**
 * FXML Controller class
 *
 * @author Ghetto
 */
public class UsersPanelController implements Initializable {

    private static final Logger LOG = Logger.getLogger(UsersPanelController.class.getName());

    
    @FXML
    private Pane usersPanel;
    @FXML
    private TextField usersUsername;
    @FXML
    private TextField usersPasswordConfirm;
    @FXML
    private TextField usersPassword;
    @FXML
    private CheckBox usersActiveDa;
    @FXML
    private CheckBox usersActiveNu;
    @FXML
    private LocalDateTimeTextField usersDataCreare;
    @FXML
    private LocalDateTimeTextField usersDataExpirare;
    @FXML
    private LocalDateTimeTextField usersDateModificare;
    @FXML
    private CheckBox usersEnabledDa;
    @FXML
    private CheckBox usersEnabledNu;
    @FXML
    private ComboBox<String> checkBoxUsersRolul;
    @FXML
    private TableView<Users> usersTable;
    @FXML
    private Button usersButtonAdauga;
    @FXML
    private Button usersButtonModifica;
    @FXML
    private Button usersButtonCauta;
    @FXML
    private Button usersButtonSterge;
    @FXML
    private Button usersButtonGoleste;
    @FXML
    private Label passwordLabel;
    @FXML
    private TableColumn<Users, String> columnUsername;
    @FXML
    private TableColumn<Users, Boolean> columnActiv;
    @FXML
    private TableColumn<Users, LocalDateTime> columnCreare;
    @FXML
    private TableColumn<Users, LocalDateTime> columnExpirare;
    @FXML
    private TableColumn<Users, Boolean> columnActivat;
    @FXML
    private TableColumn<Users, LocalDateTime> columnModificare;
    @FXML
    private TableColumn<Users, Integer> columnRolul;

    UsersDAOInterface usersDao;

    Mesaje m = new Mesaje();
    
    Users u = new Users();
    
    int role;
    
    
    
      
@Override
    public void initialize(URL location, ResourceBundle resources) {
         
        try {
            

                        
            clearForm();
            usersDao = new UsersDAOImplement();

            ObservableList<String> list = FXCollections.observableArrayList(
                    "Admin",
                    "SuperUser",
                    "User"
            );
            checkBoxUsersRolul.getItems().addAll(list);

            Collection<Users> userList = usersDao.findAll();

            columnUsername.setCellValueFactory(new PropertyValueFactory<Users, String>("userName"));
            columnActiv.setCellValueFactory(new PropertyValueFactory<Users, Boolean>("activat"));
            columnCreare.setCellValueFactory(new PropertyValueFactory<Users, LocalDateTime>("dataCreare"));
            columnExpirare.setCellValueFactory(new PropertyValueFactory<Users, LocalDateTime>("dataExpirare"));
            columnActivat.setCellValueFactory(new PropertyValueFactory<Users, Boolean>("enabled"));
            columnModificare.setCellValueFactory(new PropertyValueFactory<Users, LocalDateTime>("updated"));
            columnRolul.setCellValueFactory(new PropertyValueFactory<Users, Integer>("role"));

            usersTable.setItems(FXCollections.observableArrayList(userList));
            
            role = Role.getInstance().getX();
            if (role!=0) {
                usersPanel.setDisable(true);
            }
            
           

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(UsersPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
   
    

    @FXML
    private void btnAdauga(ActionEvent event) {

        try {

            if (usersPassword.getText().equals(new String(usersPasswordConfirm.getText()))) {
                usersDao.add(readForm());
                m.adaugat();
                clearForm();
                Collection<Users> userList = usersDao.findAll();
                usersTable.setItems(FXCollections.observableArrayList(userList));
//                Users lastUser = usersDao.findLast();
//                usersTable.getItems().add(lastUser);

            } else {

               m.wrongPass();
                usersPassword.setText("");
                usersPasswordConfirm.setText("");
            }

        } catch (Exception e) {

            m.checkInput();

        }
    }

    
   
    
    @FXML
    private void btnModifica(ActionEvent event) throws SQLException {

        try {
            
            LOG.info("Interior la try");
            Users uDd = usersDao.findByUsername(readForm().getUserName());
            System.out.println("uDb="+uDd);
            if (uDd != null) {
                LOG.info("IN IF");
                Users u = readForm();

                uDd.setActivat(u.getActivat());
                uDd.setDataCreare(u.getDataCreare());
                uDd.setDataExpirare(u.getDataExpirare());
                uDd.setEnabled(u.getEnabled());
                uDd.setPassword(u.getPassword());
                uDd.setPasswordConfirm(u.getPasswordConfirm());
                uDd.setRole(u.getRole());
                uDd.setUpdated(u.getUpdated());
                

                usersDao.update(u);
                m.modificat();
                clearForm();
                Collection<Users> userList = usersDao.findAll();
                usersTable.setItems(FXCollections.observableArrayList(userList));

            }else {
            
                m.checkInput();
                clearForm();
            }

        } catch (Exception e) {
             m.checkInput();
            clearForm();
        }
    }

    @FXML
    private void btnCauta(ActionEvent event) {

        try {
            Users uDb = usersDao.findByUsername(readForm().getUserName());

            if (uDb != null) {

                fillForm(uDb);

                      

            } else {

               m.notFound();

            }
        } catch (Exception ex) {
            m.notFound();
        }

    }

    @FXML
    private void btnSterge(ActionEvent event) {

        try {

            Users uDb = usersDao.findByUsername(readForm().getUserName());

            if (uDb != null) {

                usersDao.delete(uDb);
                m.sters();
                clearForm();
                Collection<Users> userList = usersDao.findAll();
                usersTable.setItems(FXCollections.observableArrayList(userList));

            }

        } catch (Exception e) {

            m.notFound();

        }

    }

    @FXML
    private void btnGoleste(ActionEvent event) {

        clearForm();

    }
    
    

    private Users readForm() {

        Users u = new Users();

        
        ConvertorData dc = new ConvertorData();
        

        u.setUserName(usersUsername.getText());

        if (usersActiveDa.isSelected()) {
            u.setActivat(true);
        }

        if (usersActiveNu.isSelected()) {
            u.setActivat(false);
        }

        u.setDataCreare(dc.convertToDatabaseColumn(usersDataCreare.getLocalDateTime()));
        u.setDataExpirare(dc.convertToDatabaseColumn(usersDataExpirare.getLocalDateTime()));
        u.setUpdated(dc.convertToDatabaseColumn(usersDateModificare.getLocalDateTime()));

        if (usersEnabledDa.isSelected()) {
            u.setEnabled(true);
        } else if (usersEnabledNu.isSelected()) {
            u.setEnabled(false);
        }

        u.setPassword(usersPassword.getText());
        u.setPasswordConfirm(usersPasswordConfirm.getText());
        u.setRole(checkBoxUsersRolul.getSelectionModel().getSelectedIndex());

        return u;

    }

    private void clearForm() {

        usersUsername.setText("");
        usersActiveDa.setSelected(false);
        usersActiveNu.setSelected(false);
        usersDataCreare.setLocalDateTime(LocalDateTime.now());
        usersDataExpirare.setLocalDateTime(LocalDateTime.now());
        usersDateModificare.setLocalDateTime(LocalDateTime.now());
        usersEnabledDa.setSelected(false);
        usersEnabledNu.setSelected(false);
        usersPassword.setText("");
        usersPasswordConfirm.setText("");
        checkBoxUsersRolul.setPromptText("Alege Rolul");

    }

    private void fillForm(Users uDb) {

          ConvertorData dc = new ConvertorData();

        usersUsername.setText(uDb.getUserName());

        if (uDb.getActivat()) {
            usersActiveDa.setSelected(true);
        } else if (uDb.getActivat()) {
            usersActiveNu.setSelected(true);
        }
        usersDataCreare.setLocalDateTime(dc.convertToEntityAttribute(uDb.getDataCreare()));
        usersDataExpirare.setLocalDateTime(dc.convertToEntityAttribute(uDb.getDataExpirare()));
        usersDateModificare.setLocalDateTime(dc.convertToEntityAttribute(uDb.getUpdated()));

        if (uDb.getEnabled()) {
            usersEnabledDa.setSelected(true);
        } else if (uDb.getEnabled()) {
            usersEnabledNu.setSelected(true);
        }

        usersPassword.setText(uDb.getPassword());
        usersPasswordConfirm.setText(uDb.getPasswordConfirm());
        checkBoxUsersRolul.getSelectionModel().select(uDb.getRole());

    }

    @FXML
    private void activDa(ActionEvent event) {
   
        if (usersActiveDa.isSelected()) {
            usersActiveNu.setSelected(false);
        }
    
    
    }

    @FXML
    private void activNu(ActionEvent event) {
    
        if (usersActiveNu.isSelected()) {
            usersActiveDa.setSelected(false);
        }
    
    
    }

    @FXML
    private void activatDa(ActionEvent event) {
    
        if (usersEnabledDa.isSelected()) {
            usersEnabledNu.setSelected(false);
        }
    
    }

    @FXML
    private void activatNu(ActionEvent event) {
    
         if (usersEnabledNu.isSelected()) {
            usersEnabledDa.setSelected(false);
        }
    
    
    }

 
    

   


}

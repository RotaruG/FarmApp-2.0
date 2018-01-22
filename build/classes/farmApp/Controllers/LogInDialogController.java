/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Controllers;

import farmApp.Entities.Users;
import farmApp.Controllers.util.CheckUserNameAndPassword;
import farmApp.Controllers.util.Role;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ghetto
 */
public class LogInDialogController implements Initializable {

    private static final Logger LOG = Logger.getLogger(LogInDialogController.class.getName());

    @FXML
    private DialogPane logInDialog;
    @FXML
    private AnchorPane LogInDialogAnchor;
    @FXML
    private TextField logInUsername;
    @FXML
    private Button logInButton;
    @FXML
    private Button logInCancelButton;
    @FXML
    private PasswordField logInPassword;

    Users u = new Users();

    int role;

    Boolean expirat;

    Boolean activat;

    Boolean enabled;

    Control globalController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
              
    }

   
    @FXML
    private void btnLog(ActionEvent event) {
   
          try {

                    u.setUserName(logInUsername.getText());

                    u.setPassword(new String(logInPassword.getText()));

                    CheckUserNameAndPassword check = new CheckUserNameAndPassword(u);
                    
                 
                   
                    if (check.getInfo(u)) {
                        LOG.info("Succes");
                        LOG.info("rolul = " + check.getRole(u));
                       
                        Role.init(check.getRole(u)).setX(check.getRole(u));
                        

                        expirat = check.getExpirationDate(u);
                        System.out.println("EXPIRAT????????" + expirat);
                        activat = check.getActive(u);
                        System.out.println("ACTIVAT?????" + activat);
                        enabled = check.getEnabled(u);
                        System.out.println("ENABLED?????" + enabled);

                        if (expirat) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.setContentText("Profilul este expirat!");

                            alert.showAndWait();
                        } else {

                            if (activat) {

                                if (enabled) {

                                    Parent root = FXMLLoader.load(getClass().getResource("/farmApp/FXML/MainFXML.fxml"));
                                    Stage stage = new Stage();
                                    BorderPane bp = new BorderPane();
                                    Scene scene = new Scene(bp);
                                    scene.setRoot(root);
                                    stage.setScene(scene);
                                    stage.show();

                                    LogInDialogAnchor.getScene().getWindow().hide();

                                } else {

                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Profilul nu este activat!");

                                    alert.showAndWait();
                                }

                            } else {

                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("");
                                alert.setHeaderText(null);
                                alert.setContentText("Profilul nu este activ!");

                                alert.showAndWait();
                            }

                        }

                    } else {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.setContentText("Numele de utilizator sau parola nu corespund!");

                        alert.showAndWait();

                    }
                    
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    LOG.info("Error!");
                }

            }
      

}

package farmApp.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Ghetto
 */
public class Mesaje {

    public Mesaje() {

    }

    public void adaugat() {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Adaugat cu succes!");

        alert.showAndWait();

    }

    public void fileSaved() {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Fisier salvat cu succes!");

        alert.showAndWait();

    } public void fileNotSaved() {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Eroare la salvarea fisierului!");

        alert.showAndWait();

    }

    public void checkInput() {

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Verificati corectitudinea datelor introduse!");

        alert.showAndWait();

    }

    public void sters() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Sters cu succes!");

        alert.showAndWait();
    }

    public void conectionError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Eroare la conexiune!");

        alert.showAndWait();
    }

    public void notFound() {

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Nu a fost gasit!");

        alert.showAndWait();

    }

    public void modificat() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Modificat cu succes!");

        alert.showAndWait();
    }

    public void wrongPass() {

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Parolele nu corespund!");

        alert.showAndWait();

    }
}

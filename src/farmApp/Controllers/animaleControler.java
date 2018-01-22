/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.Controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPTable;
import farmApp.DAO.AmplasareDAOInterface;
import farmApp.DAO.AnimaleDAOInterface;
import farmApp.DAO.TipAnimaleDAOInterface;
import farmApp.DAO.implement.AmplasareaDAOImplement;
import farmApp.DAO.implement.AnimaleDAOImplement;
import farmApp.DAO.implement.TipAnimaleDAOImplement;
import farmApp.DB.MyDataSource;
import farmApp.Entities.Amplasarea;
import farmApp.Entities.Animale;
import farmApp.Entities.TipAnimale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import jfxtras.scene.control.LocalDateTimeTextField;
import java.io.FileOutputStream;
import javafx.scene.control.PopupControl;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Row;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import farmApp.Controllers.util.ConvertorData;


import farmApp.Controllers.util.Role;

import farmApp.util.Mesaje;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Ghetto
 */
public class animaleControler implements Initializable {

    private static final Logger LOG = Logger.getLogger(animaleControler.class.getName());

    @FXML
    private AnchorPane animalePane;
    @FXML
    private TextField animalId;
    @FXML
    private TextField animalCode;
    @FXML
    private TextField animalMotiv;
    @FXML
    private TextField animalVinzator;
    @FXML
    private TextField animalKg;
    @FXML
    private CheckBox animaleActivDa;
    @FXML
    private CheckBox animaleActivNu;
    @FXML
    private Button buttonAnimaleCautaId;
    @FXML
    private Button buttonAnimaleSterge;
    @FXML
    private Button buttonAnimaleGoleste;
    @FXML
    private Button buttonAnimaleAdauga;
    @FXML
    private Button buttonAnimaleModifica;
    @FXML
    private TableView<Animale> animaleTable;
    @FXML
    private Button buttonAnimaleCautaCod;
    @FXML
    private LocalDateTimeTextField animaleDataPrimire;
    @FXML
    private LocalDateTimeTextField animaleDataLivrare;
    @FXML
    private PieChart animalePieChart;
    @FXML
    private ComboBox<TipAnimale> animaleTip;
    @FXML
    private ComboBox<Amplasarea> AnimaleLocatie;
    @FXML
    private AnchorPane animaleAnchor;
    @FXML
    private TableColumn<Animale, Integer> columnId;
    @FXML
    private TableColumn<Animale, Boolean> columnActiv;
    @FXML
    private TableColumn<Animale, String> columnCod;
    @FXML
    private TableColumn<Animale, LocalDateTime> columnDataLivrare;
    @FXML
    private TableColumn<Animale, LocalDateTime> columnDataPrimire;
    @FXML
    private TableColumn<Animale, String> columnMotiv;
    @FXML
    private TableColumn<Animale, String> columnVinzator;
    @FXML
    private TableColumn<Animale, Double> columnKG;
    @FXML
    private TableColumn<Animale, Integer> columnTipul;
    @FXML
    private TableColumn<Animale, Integer> columnLocatia;

    AnimaleDAOInterface animaleDao;
    AmplasareDAOInterface amplasareaDao;
    TipAnimaleDAOInterface tipAnimaleDao;
    @FXML
    private Button btnExcel;
    @FXML
    private Button btnPDF;

    Mesaje m = new Mesaje();

    int role;
    @FXML
    private Label roleLabel;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        clearFormAnimale();

        try {
            animaleDao = new AnimaleDAOImplement();
            amplasareaDao = new AmplasareaDAOImplement();
            tipAnimaleDao = new TipAnimaleDAOImplement();

            if (animaleActivDa.isSelected()) {
                animaleActivNu.setSelected(false);
            }

            if (animaleActivNu.isSelected()) {
                animaleActivDa.setSelected(false);
            }

            Collection<Animale> animaleList = animaleDao.findAll();

            columnId.setCellValueFactory(new PropertyValueFactory<Animale, Integer>("stockId"));
            columnActiv.setCellValueFactory(new PropertyValueFactory<Animale, Boolean>("active"));
            columnCod.setCellValueFactory(new PropertyValueFactory<Animale, String>("animalCode"));
            columnDataLivrare.setCellValueFactory(new PropertyValueFactory<Animale, LocalDateTime>("deliveryDate"));
            columnDataPrimire.setCellValueFactory(new PropertyValueFactory<Animale, LocalDateTime>("receivedDate"));
            columnMotiv.setCellValueFactory(new PropertyValueFactory<Animale, String>("reason"));
            columnVinzator.setCellValueFactory(new PropertyValueFactory<Animale, String>("sellerName"));
            columnKG.setCellValueFactory(new PropertyValueFactory<Animale, Double>("receivedWeight"));
            columnTipul.setCellValueFactory(new PropertyValueFactory<Animale, Integer>("animalId"));
            columnLocatia.setCellValueFactory(new PropertyValueFactory<Animale, Integer>("locationId"));

            animaleTable.setItems(FXCollections.observableArrayList(animaleList));

            ObservableList tip = FXCollections.observableArrayList(tipAnimaleDao.findAll());
            animaleTip.setPromptText("Alege Tipul");
            animaleTip.setItems(tip);

            ObservableList locatia = FXCollections.observableArrayList(amplasareaDao.findAll());
            AnimaleLocatie.setPromptText("Alege Locatia");
            AnimaleLocatie.setItems(locatia);

            ObservableList<PieChart.Data> pieChartData
                    = FXCollections.observableArrayList(
                            new PieChart.Data("Bovine", getNumberBovina()),
                            new PieChart.Data("Ovine", getNumberOvina()),
                            new PieChart.Data("Porcine", getNumbePorcina()));

            animalePieChart.setData(pieChartData);
            animalePieChart.setTitle("Animale");

            
            role = Role.getInstance().getX();

            if (role == 0) {
                roleLabel.setText("Administrator");
            } else if (role == 1) {
                roleLabel.setText("Utilizator");
            } else if (role == 2) {
                roleLabel.setText("Vizitator");
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {

        }

    }

    @FXML

    private void btnCautaIdClicked(ActionEvent event) throws SQLException {

        try {

            Animale a = new Animale();
            a = animaleDao.findById(readformAnimale().getStockId());
            if (a != null) {
                fillForm(a);
            } else {

                m.notFound();

            }

        } catch (SQLException ex) {
            m.notFound();
            clearFormAnimale();
        }

    }

    @FXML
    private void btnStergeClicked(ActionEvent event) {

        role = Role.getInstance().getX();

        try {
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");

                alert.showAndWait();
            } else {
                Animale a = animaleDao.findById(readformAnimale().getStockId());
                if (a != null) {
                    animaleDao.delete(a);
                    clearFormAnimale();
                    m.sters();
                    Collection<Animale> animaleList = animaleDao.findAll();
                    animaleTable.setItems(FXCollections.observableArrayList(animaleList));
                }
            }

        } catch (SQLException ex) {
            LOG.info("Nu a fost gasit!");
            m.notFound();
            clearFormAnimale();
        }

    }

    @FXML
    private void btnGolesteClicked(ActionEvent event) {

        clearFormAnimale();

    }

    @FXML
    private void btnAdaugaClicked(ActionEvent event) {

        role = Role.getInstance().getX();

        try {

            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");
                alert.showAndWait();
            } else {

                animaleDao.add(readformAnimale());
                LOG.info("Adaugat!!!");
                clearFormAnimale();
                Animale lastAnimal = animaleDao.findLast();
                animaleTable.getItems().add(lastAnimal);
                m.adaugat();
                
                  ObservableList<PieChart.Data> pieChartData
                    = FXCollections.observableArrayList(
                            new PieChart.Data("Bovine", getNumberBovina()),
                            new PieChart.Data("Ovine", getNumberOvina()),
                            new PieChart.Data("Porcine", getNumbePorcina()));

            animalePieChart.setData(pieChartData);
            animalePieChart.setTitle("Animale");
            }

        } catch (Exception e) {

            m.checkInput();

        }

    }

    @FXML
    private void btnModificaClicked(ActionEvent event) {

        role = Role.getInstance().getX();

        try {
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");
                alert.showAndWait();
            } else {

                Animale abd = animaleDao.findById(readformAnimale().getStockId());
                if (abd != null) {
                    Animale a = readformAnimale();

                    abd.setActive(a.isActive());
                    abd.setAnimalCode(a.getAnimalCode());
                    abd.setAnimalId(a.getAnimalId());
                    abd.setDeliveryDate(a.getDeliveryDate());
                    abd.setReceivedDate(a.getReceivedDate());
                    abd.setLocationId(a.getLocationId());
                    abd.setReason(a.getReason());
                    abd.setSellerName(a.getSellerName());
                    abd.setReceivedWeight(a.getReceivedWeight());
                    abd.setStockId(a.getStockId());

                    animaleDao.update(a);
                    LOG.info("Modificat!");
                    m.modificat();
                    clearFormAnimale();
                    Collection<Animale> animaleList = animaleDao.findAll();
                    animaleTable.setItems(FXCollections.observableArrayList(animaleList));

                }
            }

        } catch (SQLException ex) {
            m.checkInput();
            clearFormAnimale();
        }

    }

    @FXML
    private void btnCautaCodClicked(ActionEvent event) {

        try {

            Animale a = animaleDao.findByCode(readformAnimale().getAnimalCode());
            if (a != null) {
                fillForm(a);
            }

        } catch (SQLException ex) {
            m.notFound();
            clearFormAnimale();
        }

    }

    private Animale readformAnimale() {

         ConvertorData dc = new ConvertorData();

        Animale a = new Animale();

        a.setStockId(Integer.parseInt(animalId.getText()));

        if (animaleActivDa.isSelected()) {
            a.setActive(true);
        }

        if (animaleActivNu.isSelected()) {
            a.setActive(false);
        }

        a.setAnimalCode(animalCode.getText().trim());
        a.setDeliveryDate(dc.convertToDatabaseColumn(animaleDataLivrare.getLocalDateTime()));
        a.setReceivedDate(dc.convertToDatabaseColumn(animaleDataPrimire.getLocalDateTime()));
        a.setReason(animalMotiv.getText());
        a.setSellerName(animalVinzator.getText());
        a.setReceivedWeight(Double.parseDouble(animalKg.getText()));
        a.setAnimalId(animaleTip.getSelectionModel().getSelectedIndex() + 1);
        a.setLocationId(AnimaleLocatie.getSelectionModel().getSelectedIndex() + 1);

        return a;

    }

    private void fillForm(Animale a) throws SQLException {

          ConvertorData dc = new ConvertorData();

        animalCode.setText(a.getAnimalCode());
        if (a.getDeliveryDate() == null) {
            animaleDataLivrare.setLocalDateTime(LocalDateTime.MAX);
        } else {
            animaleDataLivrare.setLocalDateTime(dc.convertToEntityAttribute(a.getDeliveryDate()));
        }
        if (a.getReceivedDate() == null) {
            animaleDataPrimire.setLocalDateTime(LocalDateTime.now());  //.setDateTimePermissive(LocalDateTime.now());
        } else {
            animaleDataPrimire.setLocalDateTime(dc.convertToEntityAttribute(a.getReceivedDate()));
        }

        animalMotiv.setText(a.getReason());

        animalVinzator.setText(a.getSellerName());

        animalId.setText("" + a.getStockId());

        animalKg.setText(Double.toString(a.getReceivedWeight()));

        if (a.isActive()) {
            animaleActivDa.setSelected(true);
        } else {
            animaleActivNu.setSelected(true);
        }

        AnimaleLocatie.getSelectionModel().select(a.getLocationId());
        animaleTip.getSelectionModel().select(a.getAnimalId());

    }

    private void clearFormAnimale() {

        animalCode.setText("MD");
        animaleDataLivrare.setLocalDateTime(LocalDateTime.now());
        animaleDataPrimire.setLocalDateTime(LocalDateTime.now());
        animalMotiv.setText("");
        animalVinzator.setText("");
        animalId.setText("0");
        animalKg.setText("0.0");
        animaleActivDa.setSelected(false);
        animaleActivNu.setSelected(false);
        AnimaleLocatie.setPromptText("Alege Locatia");
        animaleTip.setPromptText("Alege Tipul");

    }

    @FXML
    private void daSelected(ActionEvent event) {

        if (animaleActivDa.isSelected()) {
            animaleActivNu.setSelected(false);
        }

    }

    @FXML
    private void nuSelected(ActionEvent event) {

        if (animaleActivNu.isSelected()) {
            animaleActivDa.setSelected(false);
        }

    }

    private int getNumberBovina() throws SQLException, ClassNotFoundException, IOException {

        Animale animal = null;
        Connection conn;
        MyDataSource mds = MyDataSource.getInstance();
        PreparedStatement pstat;
        Integer nr = 0;

        String sql = "SELECT * FROM stock WHERE animal_id=1";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {
            animal = new Animale(rs.getInt(1), rs.getBoolean(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getTimestamp(5), rs.getString(6), rs.getString(7),
                    rs.getDouble(8), rs.getInt(9), rs.getInt(10));
            nr++;
        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return nr;

    }

    private int getNumberOvina() throws ClassNotFoundException, SQLException, IOException {

        Animale animal = null;
        Connection conn;
        MyDataSource mds = MyDataSource.getInstance();
        PreparedStatement pstat;
        Integer nr = 0;

        String sql = "SELECT * FROM stock WHERE animal_id=2";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {
            animal = new Animale(rs.getInt(1), rs.getBoolean(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getTimestamp(5), rs.getString(6), rs.getString(7),
                    rs.getDouble(8), rs.getInt(9), rs.getInt(10));
            nr++;
        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return nr;
    }

    private double getNumbePorcina() throws ClassNotFoundException, SQLException, IOException {

        Animale animal = null;
        Connection conn;
        MyDataSource mds = MyDataSource.getInstance();
        PreparedStatement pstat;
        Integer nr = 0;

        String sql = "SELECT * FROM stock WHERE animal_id=3";

        conn = mds.getConnection();
        pstat = conn.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();

        while (rs.next()) {
            animal = new Animale(rs.getInt(1), rs.getBoolean(2), rs.getString(3),
                    rs.getTimestamp(4), rs.getTimestamp(5), rs.getString(6), rs.getString(7),
                    rs.getDouble(8), rs.getInt(9), rs.getInt(10));
            nr++;
        }
        rs.close();
        pstat.close();
        mds.closeConnection();

        return nr;

    }

    @FXML
    private void btnExcel(ActionEvent event) throws SQLException, IOException {

        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions
                = new FileChooser.ExtensionFilter(
                        "Excel", "*.xls");
        fc.getExtensionFilters().add(fileExtensions);

        Window window = new PopupControl();
        File path = fc.showSaveDialog(window);
        String path1 = path.getAbsolutePath();
        role = Role.getInstance().getX();

        try {
            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");
                alert.showAndWait();
            } else {

                String query = "SELECT * FROM stock";
                Connection conn;
                MyDataSource mds = MyDataSource.getInstance();
                conn = mds.getConnection();
                PreparedStatement pstat;
                pstat = conn.prepareStatement(query);
                ResultSet rs = pstat.executeQuery();
                rs = pstat.executeQuery();
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Animale");
                HSSFRow header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Activ?");
                header.createCell(2).setCellValue("Cod");
                header.createCell(3).setCellValue("Data Livrare");
                header.createCell(4).setCellValue("Data Primire");
                header.createCell(5).setCellValue("Motiv");
                header.createCell(6).setCellValue("Vinzator");
                header.createCell(7).setCellValue("KG");
                header.createCell(8).setCellValue("Tipul");
                header.createCell(9).setCellValue("Locatia");
                int index = 1;
                while (rs.next()) {
                    Row row = sheet.createRow(index);
                    row.createCell(0).setCellValue(rs.getInt(1));
                    row.createCell(1).setCellValue(rs.getBoolean(2));
                    row.createCell(2).setCellValue(rs.getString(3));
                    row.createCell(3).setCellValue(rs.getTimestamp(4));
                    row.createCell(4).setCellValue(rs.getTimestamp(5));
                    row.createCell(5).setCellValue(rs.getString(6));
                    row.createCell(6).setCellValue(rs.getString(7));
                    row.createCell(7).setCellValue(rs.getDouble(8));
                    row.createCell(8).setCellValue(rs.getInt(9));
                    row.createCell(9).setCellValue(rs.getInt(10));

                    index++;
                }

                FileOutputStream fileOutputStream = new FileOutputStream(path1);
                wb.write(fileOutputStream);
                fileOutputStream.close();

                m.fileSaved();
            }
        } catch (SQLException | FileNotFoundException e) {
            m.fileNotSaved();
        } catch (IOException | ClassNotFoundException e) {
            m.fileNotSaved();
        }

    }

    @FXML
    private void btnPDF(ActionEvent event) {

        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions
                = new FileChooser.ExtensionFilter(
                        "PDF", "*.pdf");
        fc.getExtensionFilters().add(fileExtensions);

        Window window = new PopupControl();
        File path = fc.showSaveDialog(window);
        String path1 = path.getAbsolutePath();

        Document document = new Document(PageSize.A4.rotate());

        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.ITALIC);
        PdfPageEvent pdf = new PdfPageEventHelper();

        role = Role.getInstance().getX();

        try {

            if (role == 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Nu aveti acces!");
                alert.showAndWait();
            } else {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path1));
                Rotate rotation = new Rotate();
                writer.setPageEvent(rotation);
                LOG.info("path=" + path1);
                writer.setPageEvent(pdf);

                document.open();

                Paragraph paragraphlista = new Paragraph();
                paragraphlista.setFont(font);
                paragraphlista.add("Lista Animale:");
                document.add(paragraphlista);
                Paragraph tempsituation = new Paragraph();
                tempsituation.add(" ");
                document.add(tempsituation);
                float[] columnWidths = {2, 3, 8, 6, 6, 4, 5, 6, 2, 2};
                PdfPTable tabel = new PdfPTable(columnWidths);

                tabel.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));

                tabel.addCell("ID");
                tabel.addCell("Activ?");
                tabel.addCell("Cod");
                tabel.addCell("Data Livrare");
                tabel.addCell("Data Primire");
                tabel.addCell("Motiv");
                tabel.addCell("Vinzator");
                tabel.addCell("KG");
                tabel.addCell("Tipul");
                tabel.addCell("Locatia");

                tabel.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);

                for (Animale animale : animaleTable.getItems()) {
                    Integer id = animale.getStockId();
                    Boolean active = animale.isActive();
                    String cod = animale.getAnimalCode();
                    Timestamp livrare = animale.getDeliveryDate();
                    Timestamp primire = animale.getReceivedDate();
                    String motiv = animale.getReason();
                    String vinzator = animale.getSellerName();
                    Double kg = animale.getReceivedWeight();
                    Integer tip = animale.getAnimalId();
                    Integer loc = animale.getLocationId();

                    tabel.addCell(String.valueOf(id));
                    tabel.addCell(String.valueOf(active));
                    tabel.addCell(String.valueOf(cod));
                    tabel.addCell(String.valueOf(livrare));
                    tabel.addCell(String.valueOf(primire));
                    tabel.addCell(String.valueOf(motiv));
                    tabel.addCell(String.valueOf(vinzator));
                    tabel.addCell(String.valueOf(kg));
                    tabel.addCell(String.valueOf(tip));
                    tabel.addCell(String.valueOf(loc));

                }

                document.add(tabel);
                document.close();
                m.fileSaved();
            }

        } catch (DocumentException | IOException e) {
            m.fileNotSaved();
        }

    }

    @FXML
    private void mouseClicked(MouseEvent e) {

    }

    @FXML
    private void locatiaSelected(ActionEvent event) {

    }

    @FXML
    private void cliked(MouseEvent event) throws SQLException {

        ObservableList tip = FXCollections.observableArrayList(tipAnimaleDao.findAll());
        animaleTip.setItems(tip);

    }

    @FXML
    private void locClick(MouseEvent event) throws SQLException {
    
          ObservableList loc = FXCollections.observableArrayList(amplasareaDao.findAll());
        AnimaleLocatie.setItems(loc);
    
    }

    

    public class Rotate extends PdfPageEventHelper {

        protected PdfNumber rotation = PdfPage.LANDSCAPE;

        public void setRotation(PdfNumber rotation) {
            this.rotation = rotation;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            writer.addPageDictEntry(PdfName.ROTATE, rotation);
        }
    }

}

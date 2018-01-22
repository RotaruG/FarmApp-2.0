/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmApp.DAO.implement;

import farmApp.DAO.AnimaleToExcel;
import farmApp.Entities.Animale;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ghetto
 */
public class AnimaleToExcelImpl implements AnimaleToExcel {

    @Override
    public void scrie(List<Animale> lista, String fileName) throws IOException {
        
        Workbook workbook = new XSSFWorkbook();
        Sheet exameneSheet = workbook.createSheet("Examene");
        int rowIndex = 0;
        for(Animale animale : lista){
            Row row = exameneSheet.createRow(rowIndex++);
            int cellIndex = 0;
           
            row.createCell(cellIndex++).setCellValue(animale.getStockId());
            row.createCell(cellIndex++).setCellValue(animale.isActive());
            row.createCell(cellIndex++).setCellValue(animale.getAnimalCode());
            row.createCell(cellIndex++).setCellValue(animale.getDeliveryDate());
            row.createCell(cellIndex++).setCellValue(animale.getReceivedDate());
            row.createCell(cellIndex++).setCellValue(animale.getReason());
            row.createCell(cellIndex++).setCellValue(animale.getSellerName());
            row.createCell(cellIndex++).setCellValue(animale.getReceivedWeight());
            row.createCell(cellIndex++).setCellValue(animale.getAnimalId());
            row.createCell(cellIndex++).setCellValue(animale.getLocationId());
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
            Cell cell = row.createCell(cellIndex++);
            
            cell.setCellStyle(cellStyle);
            
           // row.createCell(cellIndex++).setCellValue(examen.getDataExamen());
        }
       
       FileOutputStream fos = new FileOutputStream(fileName);
       workbook.write(fos);
       fos.close();
     
    }

    @Override
    public List<Animale> citeste(String fileName) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

package group.msg.jsf_ejb;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.interfaces.PdfVersion;
import group.msg.entities.Animal;
import group.msg.entities.Employee;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.List;


@Stateless
public class EJB implements Serializable{

    public InputStream objToPdf(List<Employee> lst){
        Document document = new Document();
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            try {
                PdfWriter.getInstance(document, outputStream);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.open();


                    for(Employee e:lst) {
                        String line=e.getName()+" "+e.getRole()+" "+e.getCnp();
                        Paragraph subPara = new Paragraph(line);
                        try {
                            document.add(subPara);
                        } catch (DocumentException exc) {
                            exc.printStackTrace();
                        }
                    }
            document.close();

        ByteArrayInputStream inputStream=new ByteArrayInputStream(outputStream.toByteArray());
        return inputStream;
    }
    public InputStream objToExcel(List<Employee> lst){
        String[] columns = {"Name", "Role", "CNP"};
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        Workbook workbook = new HSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Employee");


        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        int rowNum = 1;
        for(Employee employee: lst) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(employee.getName());

            row.createCell(1)
                    .setCellValue(employee.getRole());

            row.createCell(2)
                    .setCellValue(employee.getCnp());

        }
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream inputStream=new ByteArrayInputStream(outputStream.toByteArray());
        return inputStream;
    }


}

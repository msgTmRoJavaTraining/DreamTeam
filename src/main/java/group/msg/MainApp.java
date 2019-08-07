package group.msg;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainApp {
    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        String FILE="employee.pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();
        //addMetaData(document);
        Paragraph subPara = new Paragraph("Subcategory 1");
        document.add(subPara);
        //addTitlePage(document);
        // addContent(document);
        document.close();
    }
}

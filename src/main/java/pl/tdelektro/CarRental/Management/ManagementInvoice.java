package pl.tdelektro.CarRental.Management;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
class ManagementInvoice {

    void createInvoice(ManagementReservation managementReservation) throws IOException, DocumentException {
        Document document = new Document();
        String fileName = managementReservation.getReservationId().toString() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        Image image = Image.getInstance("src/main/resources/banner.jpg");
        image.scaleToFit(163, 132);
        document.add(image);

        Paragraph invoiceText = new Paragraph("INVOICE");
        invoiceText.setSpacingBefore(30);
        invoiceText.setSpacingAfter(50);
        invoiceText.setAlignment(PdfPCell.ALIGN_CENTER);
        document.add(invoiceText);

        String[] invoiceData = {
                "RESERVATION ID    :   " + managementReservation.getReservationId(),
                "CUSTOMER              :   " + managementReservation.getCustomerEmail(),
                " ",
                "RENTED CAR            :   " + managementReservation.getCarId().toString(),
                "RENT START DATE  :   " + managementReservation.getStartDate(),
                "RENT END DATE      :   " + managementReservation.getEndDate(),
                " ",
                "TOTAL COST            :   " + managementReservation.getTotalReservationCost() + " $"
        };

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBorder(PdfPCell.ALIGN_RIGHT);


        for (int i = 0; i < invoiceData.length - 1; i++) {
            Paragraph paragraph = new Paragraph(invoiceData[i]);
            document.add(paragraph);
        }
        table.addCell(createCell(invoiceData[invoiceData.length-1], true));

        document.add(table);
        document.close();
    }

    PdfPCell createCell(String text, boolean bolded) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        if (bolded) {
            cell.setPaddingTop(15);
            cell.setPaddingBottom(15);
            cell.setArabicOptions(1);
        }
        return cell;
    }

}

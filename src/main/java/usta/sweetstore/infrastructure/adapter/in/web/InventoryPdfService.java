package usta.sweetstore.infrastructure.adapter.in.web;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.enterprise.context.ApplicationScoped;

import usta.sweetstore.domain.model.Product;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@ApplicationScoped
public class InventoryPdfService {


    public byte[] generateInventoryReport(
            List<Product> products
    ) {

        try {

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();


            Document document =
                    new Document();


            PdfWriter.getInstance(
                    document,
                    output
            );


            document.open();


            Font titleFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            20
                    );


            Font subtitleFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA,
                            10
                    );


            Font headerFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            10
                    );


            Font bodyFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA,
                            9
                    );


            Paragraph title =
                    new Paragraph(
                            "SweetStore - Reporte de Inventario",
                            titleFont
                    );


            title.setAlignment(
                    Element.ALIGN_CENTER
            );


            document.add(title);


            String currentDate =
                    LocalDate.now()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "dd/MM/yyyy"
                                            )
                            );


            Paragraph date =
                    new Paragraph(
                            "Fecha de generación: "
                                    + currentDate,
                            subtitleFont
                    );


            date.setAlignment(
                    Element.ALIGN_CENTER
            );


            date.setSpacingAfter(25);


            document.add(date);


            PdfPTable table =
                    new PdfPTable(6);


            table.setWidthPercentage(100);


            table.setWidths(
                    new float[]{
                            2.5f,
                            2f,
                            1.5f,
                            1f,
                            1.2f,
                            1.5f
                    }
            );


            addHeader(
                    table,
                    "Producto",
                    headerFont
            );

            addHeader(
                    table,
                    "Categoría",
                    headerFont
            );

            addHeader(
                    table,
                    "Precio",
                    headerFont
            );

            addHeader(
                    table,
                    "Stock",
                    headerFont
            );

            addHeader(
                    table,
                    "Stock mínimo",
                    headerFont
            );

            addHeader(
                    table,
                    "Estado",
                    headerFont
            );


            for (Product product : products) {


                addCell(
                        table,
                        product.getName(),
                        bodyFont
                );


                addCell(
                        table,
                        product.getCategory()
                                .getName(),
                        bodyFont
                );


                addCell(
                        table,
                        "$ "
                                + product
                                .getPrice(),
                        bodyFont
                );


                addCell(
                        table,
                        String.valueOf(
                                product.getStock()
                        ),
                        bodyFont
                );


                addCell(
                        table,
                        String.valueOf(
                                product
                                        .getMinimumStock()
                        ),
                        bodyFont
                );


                addCell(
                        table,
                        product.hasLowStock()
                                ? "Stock bajo"
                                : "Disponible",
                        bodyFont
                );

            }


            document.add(table);


            document.close();


            return output.toByteArray();


        } catch (Exception e) {

            throw new RuntimeException(
                    "No fue posible generar el reporte PDF.",
                    e
            );
        }
    }


    private void addHeader(
            PdfPTable table,
            String text,
            Font font
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font
                        )
                );


        cell.setPadding(8);

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );


        table.addCell(cell);
    }


    private void addCell(
            PdfPTable table,
            String text,
            Font font
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text != null
                                        ? text
                                        : "",
                                font
                        )
                );


        cell.setPadding(7);


        table.addCell(cell);
    }
}
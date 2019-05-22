package com.example.demo.utils;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public final class PdfUtils {
	
    private static final Font fontTitle = new Font(FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font fontHeader = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
    private static final Font fontCell = new Font(FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
    private static final Font fontTotal = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLDITALIC, BaseColor.BLACK);
    private static final DecimalFormat formatPrix = new DecimalFormat("####0.00");
    
    public static Element createHeaderClient(Client client) {

        PdfPTable tableClient = new PdfPTable(1);
        
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Phrase(client.getNom().toUpperCase() + " " + client.getPrenom(), fontHeader));
        cell.addElement(new Phrase(client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fontCell));
        
        cell.setPaddingBottom(6f);
        cell.setPaddingTop(2f);
        cell.setPaddingLeft(10f);
        cell.setPaddingRight(10f);
        tableClient.addCell(cell);
        tableClient.setWidthPercentage(40);
        tableClient.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableClient.setSpacingAfter(25f);
        
    	return tableClient;
    }
    
    public static Element createTableFacture(Facture facture) throws DocumentException {

        // Table de la facture
        PdfPTable tableFacture = new PdfPTable(5);
        tableFacture.setWidthPercentage(100);
        tableFacture.setWidths(new float[]{0.8f, 4f, 0.8f,1.2f,1.2f});
        
        PdfPCell cell = new PdfPCell(new Phrase("Facture n° " + facture.getId(), fontTitle));
        cell.setColspan(5);
        tableFacture.addCell(cell);

        tableFacture.addCell(new PdfPCell(new Phrase("ID", fontHeader)));
        tableFacture.addCell(new PdfPCell(new Phrase("Libellé", fontHeader)));
        tableFacture.addCell(new PdfPCell(new Phrase("Quantité", fontHeader)));
        tableFacture.addCell(new PdfPCell(new Phrase("Prix unitaire", fontHeader)));
        tableFacture.addCell(new PdfPCell(new Phrase("Total", fontHeader)));

    	for (LigneFacture ligneFacture : facture.getLigneFactures()) {
    		
    		tableFacture.addCell(new PdfPCell(new Phrase(ligneFacture.getArticle().getId().toString(), fontCell)));
    		tableFacture.addCell(new PdfPCell(new Phrase(ligneFacture.getArticle().getLibelle(), fontCell)));
    		tableFacture.addCell(new PdfPCell(new Phrase(Integer.toString(ligneFacture.getQuantite()), fontCell)));
    		tableFacture.addCell(new PdfPCell(new Phrase(formatPrix.format(ligneFacture.getArticle().getPrix()), fontCell)));
    		tableFacture.addCell(new PdfPCell(new Phrase(formatPrix.format(ligneFacture.getSousTotal()), fontCell)));
        }
    	
        cell = new PdfPCell(new Phrase("Total ", fontTotal));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableFacture.addCell(cell);

        cell = new PdfPCell(new Phrase(formatPrix.format(facture.getTotal()), fontTotal));
        tableFacture.addCell(cell);
        
        return tableFacture;
    }
    
}

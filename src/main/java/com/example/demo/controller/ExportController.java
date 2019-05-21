package com.example.demo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.service.ClientService;
import com.example.demo.service.FactureService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Controlleur pour réaliser les exports.
 */
@Controller
@RequestMapping("/")
public class ExportController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private FactureService factureService;

    @GetMapping("/clients/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        PrintWriter writer = response.getWriter();
        List<Client> allClients = clientService.findAllClients();
        LocalDate now = LocalDate.now();
        writer.println("Id;Nom;Prenom;Date de Naissance;Age");
        
        for (Client c : allClients) {
            writer.println(c.getId() + ";"
            		+ "\"" + c.getNom() + "\";"
            		+ "\"" + c.getPrenom() + "\";"
            		+ c.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            		+ ";" + Period.between(c.getDateNaissance(), now).getYears());
        }

    }

    @GetMapping("/clients/xlsx")
    public void clientsXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.xlsx\"");

        List<Client> allClients = clientService.findAllClients();

        OutputStream fileOutputStream = response.getOutputStream();
        
    	Workbook workbook = new XSSFWorkbook();
    	Sheet sheet = workbook.createSheet("Clients");
    	Row headerRow = sheet.createRow(0);
    	headerRow.createCell(0).setCellValue("ID");
    	headerRow.createCell(1).setCellValue("Nom");
    	headerRow.createCell(2).setCellValue("Prénom");
    	headerRow.createCell(3).setCellValue("Date de naissance");
    	headerRow.createCell(4).setCellValue("Age");
    	
    	int row = 1;
        for (Client c : allClients) {
        	Row rowClient = sheet.createRow(row);
        	rowClient.createCell(0).setCellValue(c.getId());
        	rowClient.createCell(1).setCellValue(c.getNom());
        	rowClient.createCell(2).setCellValue(c.getPrenom());
        	rowClient.createCell(3).setCellValue(c.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        	// workbook.createCellStyle().setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd-MM-yyyy"));
        	rowClient.createCell(4).setCellValue(Period.between(c.getDateNaissance(), LocalDate.now()).getYears());
        	row++;
        }
        
        for(int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
        
    	workbook.write(fileOutputStream);
    	workbook.close();

    }

    @RequestMapping(value = "/clients/{id}/factures/xlsx")
    public void clientFactureXLSX(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"facture_client_" + id + ".xlsx\"");

        Client client = clientService.findById(id);

        OutputStream fileOutputStream = response.getOutputStream();
        
    	Workbook workbook = new XSSFWorkbook();
    	
    	CellStyle stylePrix = workbook.createCellStyle();
    	DataFormat format = workbook.createDataFormat();
    	stylePrix.setDataFormat(format.getFormat("0.00€"));
    	
    	for (Facture facture : client.getFactures()) {
    		
        	Sheet sheet = workbook.createSheet("Facture_" + facture.getId());
        	Row headerRow = sheet.createRow(0);
        	headerRow.createCell(0).setCellValue("ID");
        	headerRow.createCell(1).setCellValue("Libellé");
        	headerRow.createCell(2).setCellValue("Quantité");
        	headerRow.createCell(3).setCellValue("Prix unitaire");
        	headerRow.createCell(4).setCellValue("Total");
        	
        	int row = 1;
        	for (LigneFacture ligneFacture : facture.getLigneFactures()) {
            	Row rowFacture = sheet.createRow(row);
            	rowFacture.createCell(0).setCellValue(ligneFacture.getArticle().getId());
            	rowFacture.createCell(1).setCellValue(ligneFacture.getArticle().getLibelle());
            	rowFacture.createCell(2).setCellValue(ligneFacture.getQuantite());
            	rowFacture.createCell(3).setCellValue(ligneFacture.getArticle().getPrix());
            	rowFacture.createCell(4).setCellValue(ligneFacture.getSousTotal());

            	rowFacture.getCell(3).setCellStyle(stylePrix);
            	rowFacture.getCell(4).setCellStyle(stylePrix);
            	
            	row++;
        	}
        	
            for(int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }
        
        	sheet.createRow(row);
        	CellRangeAddress mergedRegion = new CellRangeAddress(row, row, 0, 3);
        	sheet.addMergedRegion(mergedRegion);
        	Cell cellTotal = sheet.getRow(row).createCell(0);
        	cellTotal.setCellValue("Total :");
        	CellUtil.setCellStyleProperty(cellTotal, CellUtil.ALIGNMENT, HorizontalAlignment.RIGHT); 
        	
        	
        	sheet.getRow(row).createCell(4).setCellValue(facture.getTotal());
        	sheet.getRow(row).getCell(4).setCellStyle(stylePrix);
    	}

    	workbook.write(fileOutputStream);
    	workbook.close();

    }

    @GetMapping("/factures/{id}/pdf")
    public void facturePDF(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"facture_" + id + ".pdf\"");

        Facture facture = factureService.findById(id);

        OutputStream fileOutputStream = response.getOutputStream();

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
        
        DecimalFormat formatPrix = new DecimalFormat("####0.00");

        document.open();
        writer.setCompressionLevel(0);

        Font fontTitle = new Font(FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
        Font fontHeader = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
        Font fontCell = new Font(FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
        Font fontTotal = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLDITALIC, BaseColor.BLACK);

        PdfPTable tableHead = new PdfPTable(1);
        
        PdfPCell cell = new PdfPCell(new Phrase(facture.getClient().getNom().toUpperCase() + " " + facture.getClient().getPrenom(), fontHeader));
        cell.setPadding(10f);
        tableHead.addCell(cell);
        tableHead.setWidthPercentage(40);
        tableHead.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableHead.setSpacingAfter(25f);
        document.add(tableHead);
        
        // Table de la facture
        PdfPTable tableFacture = new PdfPTable(5);
        tableFacture.setWidthPercentage(100);
        tableFacture.setWidths(new float[]{0.8f, 4f, 0.8f,1.2f,1.2f});
        
        cell = new PdfPCell(new Phrase("Facture n° " + id, fontTitle));
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
        
        document.add(tableFacture);
       
        document.close();
    }    
    
}

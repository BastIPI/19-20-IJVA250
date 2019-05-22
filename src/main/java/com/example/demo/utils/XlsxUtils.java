package com.example.demo.utils;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;

public final class XlsxUtils {

	private XlsxUtils() {}
	
	public static void createClasseurClients(Workbook workbook, List<Client> clients) {

		// Créer la feuille
    	Sheet sheet = workbook.createSheet("Clients");
    	
    	// Ligne de header
    	Row row = sheet.createRow(0);
    	row.createCell(0).setCellValue("ID");
    	row.createCell(1).setCellValue("Nom");
    	row.createCell(2).setCellValue("Prénom");
    	row.createCell(3).setCellValue("Date de naissance");
    	row.createCell(4).setCellValue("Age");
    	
    	// Boucle sur les clients
        for (Client client : clients) {
        	row = sheet.createRow(row.getRowNum() + 1);
        	createLigneClient(workbook, row, client);
        }
        
        // Redimensionnement automatique des colonnes
        for(int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
	}
	
	public static void createClasseurClientFactures(Workbook workbook, Client client) {
		
		// Création de la feuille des infos clients
		createFeuilleClient(workbook, client);
    	
		// Créations des feuilles de facture
    	for (Facture facture : client.getFactures()) {
    		createFeuilleFacture(workbook, facture);
    	}
	}
	
	public static void createClasseurFactures(Workbook workbook, List<Client> clients) {

    	for (Client client : clients) {
    		XlsxUtils.createClasseurClientFactures(workbook, client);
    	}

	}
	
	private static void createFeuilleFacture(Workbook workbook, Facture facture) {
		
		// Création de la feuille
    	Sheet sheet = workbook.createSheet("Facture n° " + facture.getId());
    	
    	// Ligne de header
    	Row row = sheet.createRow(0);
    	row.createCell(0).setCellValue("ID");
    	row.createCell(1).setCellValue("Libellé");
    	row.createCell(2).setCellValue("Quantité");
    	row.createCell(3).setCellValue("Prix unitaire");
    	row.createCell(4).setCellValue("Total");

    	formatFontBold(workbook, row.getCell(0));
    	formatFontBold(workbook, row.getCell(1));
    	formatFontBold(workbook, row.getCell(2));
    	formatFontBold(workbook, row.getCell(3));
    	formatFontBold(workbook, row.getCell(4));
    	
    	// Boucle sur les lignes de la facture
    	for (LigneFacture ligneFacture : facture.getLigneFactures()) {
        	row = sheet.createRow(row.getRowNum() + 1);
        	createLigneFacture(workbook, row, ligneFacture);
    	}
    	
    	// Dimensionnement auto des colonnes
        for(int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
    
        // Création de la ligne de total
    	row = sheet.createRow(row.getRowNum() + 1);
    	CellRangeAddress mergedRegion = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 3);
    	sheet.addMergedRegion(mergedRegion);
    	Cell cellTotal = row.createCell(0);
    	cellTotal.setCellValue("Total :");
    	formatFontBold(workbook, cellTotal);
    	CellUtil.setCellStyleProperty(cellTotal, CellUtil.ALIGNMENT, HorizontalAlignment.RIGHT); 
    	
    	row.createCell(4).setCellValue(facture.getTotal());
    	XlsxUtils.formatPrix(workbook, row.getCell(4));
	}
	
	private static void createFeuilleClient(Workbook workbook, Client client) {
		
		// Création de la feuille
    	Sheet sheet = workbook.createSheet(client.getNom() + " " + client.getPrenom());
    	
    	// Remplissage
    	Row row = sheet.createRow(0);
    	row.createCell(0).setCellValue("Nom :");
    	formatFontBold(workbook, row.getCell(0));
    	row.createCell(1).setCellValue(client.getNom());

    	row = sheet.createRow(row.getRowNum() + 1);
    	row.createCell(0).setCellValue("Prénom :");
    	formatFontBold(workbook, row.getCell(0));
    	row.createCell(1).setCellValue(client.getPrenom());

    	row = sheet.createRow(row.getRowNum() + 1);
    	row.createCell(0).setCellValue("Date de naissance :");
    	formatFontBold(workbook, row.getCell(0));
    	row.createCell(1).setCellValue(client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

    	row = sheet.createRow(row.getRowNum() + 1);
    	row.createCell(0).setCellValue("Age :");
    	formatFontBold(workbook, row.getCell(0));
    	row.createCell(1).setCellValue(client.getAge());

        // Redimensionnement automatique des colonnes
        for(int i = 0; i < 2; i++) {
            sheet.autoSizeColumn(i);
        }
    	
	}
	
	private static void createLigneFacture(Workbook workbook, Row row, LigneFacture ligneFacture) {

		// Insertion des valeurs
    	row.createCell(0).setCellValue(ligneFacture.getArticle().getId());
    	row.createCell(1).setCellValue(ligneFacture.getArticle().getLibelle());
    	row.createCell(2).setCellValue(ligneFacture.getQuantite());
    	row.createCell(3).setCellValue(ligneFacture.getArticle().getPrix());
    	row.createCell(4).setCellValue(ligneFacture.getSousTotal());

    	// Mise en forme des cellules au format prix
    	XlsxUtils.formatPrix(workbook, row.getCell(3));
    	XlsxUtils.formatPrix(workbook, row.getCell(4));
		
	}
	
	private static void createLigneClient(Workbook workbook, Row row, Client client) {

    	row.createCell(0).setCellValue(client.getId());
    	row.createCell(1).setCellValue(client.getNom());
    	row.createCell(2).setCellValue(client.getPrenom());
    	row.createCell(3).setCellValue(client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    	// workbook.createCellStyle().setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd-MM-yyyy"));
    	row.createCell(4).setCellValue(client.getAge());
    	
	}
	
	private static void formatPrix(Workbook workbook, Cell cell) {
    	CellStyle stylePrix = workbook.createCellStyle();
    	DataFormat format = workbook.createDataFormat();
    	stylePrix.setDataFormat(format.getFormat("0.00€"));
    	
		cell.setCellStyle(stylePrix);
	}

	private static void formatFontBold(Workbook workbook, Cell cell) {
    	CellStyle style = workbook.createCellStyle();

    	Font font = workbook.createFont();

    	font.setFontName("Courier New");
    	font.setBold(true);
    	
    	style.setFont(font);
    	cell.setCellStyle(style);
	}
	
}

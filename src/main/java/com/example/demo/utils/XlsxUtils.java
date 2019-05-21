package com.example.demo.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;

public final class XlsxUtils {

	private XlsxUtils() {}
	
	public static void formatPrix(Workbook workbook, Cell cell) {
    	CellStyle stylePrix = workbook.createCellStyle();
    	DataFormat format = workbook.createDataFormat();
    	stylePrix.setDataFormat(format.getFormat("0.00€"));
    	
		cell.setCellStyle(stylePrix);
	}
	
	public static void createFeuilleFacture(Workbook workbook, Facture facture) {
		
		// Création de la feuille
    	Sheet sheet = workbook.createSheet("Facture n° " + facture.getId());
    	
    	// Ligne de header
    	Row row = sheet.createRow(0);
    	row.createCell(0).setCellValue("ID");
    	row.createCell(1).setCellValue("Libellé");
    	row.createCell(2).setCellValue("Quantité");
    	row.createCell(3).setCellValue("Prix unitaire");
    	row.createCell(4).setCellValue("Total");
    	
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
    	CellUtil.setCellStyleProperty(cellTotal, CellUtil.ALIGNMENT, HorizontalAlignment.RIGHT); 
    	
    	row.createCell(4).setCellValue(facture.getTotal());
    	XlsxUtils.formatPrix(workbook, row.getCell(4));
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
	
	
	
}

package com.example.demo.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Client;
import com.example.demo.utils.CsvAuto;
import com.example.demo.utils.XlsxUtils;

@Service
@Transactional
public class ExportService {

	@Autowired
	ClientService clientService;
	
    public void clientsCSV(Writer writer) throws IOException {
    	PrintWriter printWriter = new PrintWriter(writer);
	    List<Client> allClients = clientService.findAllClients();
	    
	    /* Version 1 : ajout des lignes manuellement

	    FichierCsv exportClients = new FichierCsv();
	    
	    exportClients.nouvelleLigne("Id;Nom;Prenom;Date de Naissance;Age");
	    
	    for (Client c : allClients) {
	    	exportClients.nouvelleLigne();
	    	exportClients.insert(c.getId(), false);
	    	exportClients.insert(c.getNom(), true);
	    	exportClients.insert(c.getPrenom(), true);
	    	exportClients.insert(c.getDateNaissance(), false);
	    	exportClients.insert(c.getAge(), false);
	    }
	    
	    exportClients.write(printWriter);
	    */
	    
	    /* Version 2 : classe permettant de générer un CSV à partir de n'importe
	     * quel type d'objet, en fournissant les getters à utiliser
	     * Avis perso : plein d'emmerdes possibles, peut-être pas une solution envisageable dans un contexte plus large
	     * Mais c'était amusant à faire
	     */
	    CsvAuto exportClients = new CsvAuto(allClients);
	    exportClients.addCol("ID", "getId");
	    exportClients.addCol("Nom", "getNom");
	    exportClients.addCol("Prenom", "getPrenom");
	    exportClients.addCol("Date de naissance", "getDateNaissance");
	    exportClients.addCol("Age", "getAge");
	    exportClients.write(printWriter);
	    
	    
    }
    
    public void createClasseurClients(OutputStream outputStream) throws IOException {

        OutputStream fileOutputStream = outputStream;
        
        List<Client> clients = clientService.findAllClients();
        
    	Workbook workbook = new XSSFWorkbook();
    	XlsxUtils.createClasseurClients(workbook, clients);

    	workbook.write(fileOutputStream);
    	workbook.close();
    }

    
    public void createClasseurClientFactures(OutputStream outputStream, Long id) throws IOException {

        OutputStream fileOutputStream = outputStream;

        Client client = clientService.findById(id);
        
    	Workbook workbook = new XSSFWorkbook();
    	XlsxUtils.createClasseurClientFactures(workbook, client);

    	workbook.write(fileOutputStream);
    	workbook.close();
    }
    
    public void createClasseurFactures(OutputStream outputStream) throws IOException {

        OutputStream fileOutputStream = outputStream;

        List<Client> clients = clientService.findAllClients();
        
    	Workbook workbook = new XSSFWorkbook();
    	XlsxUtils.createClasseurFactures(workbook, clients);
    	
    	workbook.write(fileOutputStream);
    	workbook.close();
    	
    }
}

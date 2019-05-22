package com.example.demo.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.service.ClientService;
import com.example.demo.service.ExportService;
import com.example.demo.service.FactureService;
import com.example.demo.utils.PdfUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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

    @Autowired
    private ExportService exportService;

    @GetMapping("/clients/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        
        exportService.clientsCSV(response.getWriter());
    }

    @GetMapping("/clients/xlsx")
    public void clientsXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.xlsx\"");

        exportService.createClasseurClients(response.getOutputStream());       
    }

    @RequestMapping(value = "/clients/{id}/factures/xlsx")
    public void clientFactureXLSX(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Client client = clientService.findById(id);
    	
    	response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures_" + client.getNom().replaceAll("[^A-Za-z0-9]","") + ".xlsx\"");

        exportService.createClasseurClientFactures(response.getOutputStream(), id);
    }

    @RequestMapping(value = "/factures/xlsx")
    public void facturesXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures.xlsx\"");

    	exportService.createClasseurFactures(response.getOutputStream());
    }

    @GetMapping("/factures/{id}/pdf")
    public void facturePDF(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {

    	// Récupération de la facture
        Facture facture = factureService.findById(id);
    	
        // Définition de la réponse
    	response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"facture_" + id + ".pdf\"");

        // Ouverture du stream et du document
        OutputStream fileOutputStream = response.getOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
        document.open();
        writer.setCompressionLevel(0);

        // Insertion des données
        document.add(PdfUtils.createHeaderClient(facture.getClient()));
        document.add(PdfUtils.createTableFacture(facture));
       
        // Fermeture du document
        document.close();
    }    
    
}

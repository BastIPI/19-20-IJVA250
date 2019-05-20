package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.service.ArticleService;
import com.example.demo.service.ClientService;
import com.example.demo.service.FactureService;

/**
 * Controller principale pour affichage des clients / factures sur la page d'acceuil.
 */
@Controller
public class HomeController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private FactureService factureService;

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");

        List<Client> clients = clientService.findAllClients();
        List<Article> articles = articleService.findAllArticles();
        List<Facture> factures = factureService.findAllFactures();
        
        modelAndView.addObject("clients", clients);
        modelAndView.addObject("articles", articles);
        modelAndView.addObject("factures", factures);

        return modelAndView;
    }
}

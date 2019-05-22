package com.example.demo.utils;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FichierCsv {
	
	private List<String> lignes;
	
	public FichierCsv() {
		lignes = new ArrayList<String>();
	}

	/**
	 * Crée une nouvelle ligne à la suite du fichier CSV 
	 *
	 */
	public void nouvelleLigne() {
		lignes.add("");
	}

	/**
	 * Crée une nouvelle ligne à la suite du fichier CSV 
	 *
	 * @param  ligne 	Une chaine de caractères à insérer dans la ligne créée
	 */
	public void nouvelleLigne(String ligne) {
		lignes.add(ligne);
	}

	/**
	 * Crée une nouvelle ligne à la suite du fichier CSV 
	 *
	 * @param  valeurs 	Une liste de valeurs au format String à insérer dans la ligne
	 */
	public void nouvelleLigne(List<String> valeurs) {
		String nouvelleLigne = "";
		for (String valeur : valeurs) {
			if (!nouvelleLigne.isEmpty()) {
				nouvelleLigne.concat(";");
			}
			nouvelleLigne.concat(valeur);
		}
		lignes.add(nouvelleLigne);
	}

	/**
	 * Insert une valeur à la suite de la dernière ligne du fichier CSV 
	 *
	 * @param  valeur 	la valeur au format String à insérer
	 * @param  quotes 	ajoute ou non des guillemets autour de la valeur
	 */
	public void insert(String valeur, Boolean quotes) {
		int index = lignes.size() - 1;
		if (!lignes.get(index).isEmpty()) {
			lignes.set(index, lignes.get(index).concat(";"));
		}
		valeur = (quotes ? "\"" + valeur + "\"" : valeur);
		lignes.set(index, lignes.get(index).concat(valeur));
	}


	/**
	 * Insert une valeur à la suite de la dernière ligne du fichier CSV 
	 *
	 * @param  valeur 	la valeur au format Long à insérer
	 * @param  quotes 	ajoute ou non des guillemets autour de la valeur
	 */
	public void insert(Long valeurLong, Boolean quotes) {
		insert(valeurLong.toString(), quotes);
	}


	/**
	 * Insert une valeur à la suite de la dernière ligne du fichier CSV 
	 *
	 * @param  valeur 	la valeur au format int à insérer
	 * @param  quotes 	ajoute ou non des guillemets autour de la valeur
	 */
	public void insert(int valeurInt, Boolean quotes) {
		insert(valeurInt + "", quotes);
	}


	/**
	 * Insert une valeur à la suite de la dernière ligne du fichier CSV 
	 *
	 * @param  valeur 	la valeur au format LocalDate à insérer
	 * @param  quotes 	ajoute ou non des guillemets autour de la valeur
	 */
	public void insert(LocalDate date, Boolean quotes) {
		insert(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), quotes);
	}


	/**
	 * Ecrit l'ensemble des lignes du CSV dans le writer spécifié
	 *
	 * @param  PrintWriter 	le writer à utiliser
	 */
	public void write(PrintWriter printWriter) {
		for (String ligne : lignes) {
			printWriter.println(ligne);
		}
	}
	
}

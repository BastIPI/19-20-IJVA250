package com.example.demo.utils;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvAuto {

	private List<String> headers;
	private List<String> getters;
	
	private List<String> lignes;
	
	private List<?> data;

	/**
	 * Constructeur 
	 *
	 * @param  data 	Une liste d'objets à utiliser pour créer le fichier CSV
	 */
	public CsvAuto(List<? extends Object> data) {
		this.data = data;
		this.headers = new ArrayList<String>();
		this.getters = new ArrayList<String>();

		this.lignes = new ArrayList<String>();
	}

	/**
	 * Ajoute une colonne au fichier CSV 
	 *
	 * @param  header 	L'intitulé de la colonne
	 * @param  getter 	Le nom de la méthode de l'objet a appeler pour obtenir la valeur désirée
	 * 
	 */
	public void addCol(String header, String getter) {
		headers.add(header);
		getters.add(getter);
	}

	/**
	 * Ecrit l'ensemble des lignes du CSV dans le writer spécifié
	 *
	 * @param  PrintWriter 	le writer à utiliser
	 */
	public void write(PrintWriter printWriter) {
		Boolean erreurLigne = false;
		
		// Ligne de header : on boucle sur la liste "headers"
		String ligneHeader = "";
		for (String header : headers) {
			if (!ligneHeader.isEmpty()) {
				ligneHeader = ligneHeader + ";";
			}
			ligneHeader = ligneHeader + "\"" + header + "\"";
		}
		printWriter.println(ligneHeader);
		
		// Lignes des valeurs : on boucle sur les objets en entrée
		for (Object o : data) {
			String ligneResult = "";
			
			// On boucle sur les "getters"
			for (String getter : getters) {
				
				try {
					
					// Récupération de la méthode du getter et de son résultat
					Method method = o.getClass().getDeclaredMethod(getter);
					Object result = o.getClass().getDeclaredMethod(getter).invoke(o);
					String valeur;
					
					// Selon le type de retour du getter, on applique différentes modifications pour la conversion en chaîne du résultat
					switch (method.getReturnType().getSimpleName()) {
						case "String":
							valeur = "\"" + (String)result + "\"";
							break;
						case "LocalDate":
							LocalDate resultDate = (LocalDate)result;
							valeur = resultDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
							break;
						default:
							valeur = result.toString();
					}

					// Ajout de la valeur dans la ligne
					if (!ligneResult.isEmpty()) {
						ligneResult = ligneResult + ";";
					}
					ligneResult = ligneResult + valeur;
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					
					// Si une exception arrive, on lève un flag et sort de la boucle
					e.printStackTrace();
					erreurLigne = true;
					break;
				}

			}

			// Si aucune erreur lors du traitement, on écrit la ligne
			if (!erreurLigne) {
				printWriter.println(ligneResult);
			}
		}
		
	}
		
	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<String> getGetters() {
		return getters;
	}

	public void setGetters(List<String> getters) {
		this.getters = getters;
	}

	public List<String> getLignes() {
		return lignes;
	}

	public void setLignes(List<String> lignes) {
		this.lignes = lignes;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	
}

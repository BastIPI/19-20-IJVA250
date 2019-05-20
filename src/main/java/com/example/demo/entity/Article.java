package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column
    private String libelle;

    @Column
    private Double prix;

    @OneToMany(mappedBy="article")
    private Set<LigneFacture> ligneFactures;

    public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}

	public Long getId() {
		return id;
	}

	public Set<LigneFacture> getLigneFactures() {
		return ligneFactures;
	}

	public void setLigneFactures(Set<LigneFacture> ligneFactures) {
		this.ligneFactures = ligneFactures;
	}

}

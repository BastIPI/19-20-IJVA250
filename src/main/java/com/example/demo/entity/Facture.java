package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne
    private Client client;

    @OneToMany(mappedBy="facture")
    private Set<LigneFacture> ligneFactures;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Set<LigneFacture> getLigneFactures() {
		return ligneFactures;
	}

	public void setLignesFacture(Set<LigneFacture> lignes) {
		this.ligneFactures = lignes;
	}

	public Long getId() {
		return id;
	}
}

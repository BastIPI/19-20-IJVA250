package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Facture;
import com.example.demo.repository.FactureRepository;

@Service
@Transactional
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;

    public List<Facture> findAllFactures() {
        return factureRepository.findAll();
    }

	public Facture findById(long id) {
		return factureRepository.findById(id).get();
	}
}

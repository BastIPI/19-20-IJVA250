package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.LigneFacture;
import com.example.demo.repository.LigneFactureRepository;

@Service
@Transactional
public class LigneFactureService {

    @Autowired
    private LigneFactureRepository ligneFactureRepository;

    public List<LigneFacture> findAllFactures() {
        return ligneFactureRepository.findAll();
    }
}

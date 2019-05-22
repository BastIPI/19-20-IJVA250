package com.example.demo.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Facture;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
	
}

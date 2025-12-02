package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, String> {
    Optional<Gerente> findByEmail(String email);
    Optional<Gerente> findByCpfAndAtivoTrue(String cpf);
    List<Gerente> findAllByAtivoTrue();
}

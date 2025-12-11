package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.TaxaResponseDTO;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxaService {

    private final TaxaRepository taxaRepository;

    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    @Transactional(readOnly = true)
    public List<TaxaResponseDTO> listarTaxas(){
        return taxaRepository.findAll()
                .stream()
                .map(TaxaResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('GERENTE', 'ADMIN')")
    public TaxaResponseDTO buscarTaxaPorId(String id){
        return TaxaResponseDTO.fromEntity(buscarTaxa(id));
    }

    // registrarTaxa

    @PreAuthorize("hasRole('GERENTE')")
    public void deletarTaxa(String id){
        if (!taxaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("taxa");
        }
        taxaRepository.deleteById(id);
    }

    private Taxa buscarTaxa(String id) {
        return taxaRepository.findById(id)
                .orElseThrow( () -> new EntidadeNaoEncontradaException("taxa"));
    }

}
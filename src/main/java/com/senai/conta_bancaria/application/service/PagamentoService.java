package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.PagamentoRegistroDTO;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDTO;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    @PreAuthorize("hasRole('GERENTE', 'CLIENTE')")
    public List<PagamentoResponseDTO> listarPagamentos(){
        return pagamentoRepository
                .findAll()
                .stream()
                .map(PagamentoResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'CLIENTE')")
    public PagamentoResponseDTO buscarPagamentoPorBoleto(String boleto){
        var pagamento = buscarPagamentoBoleto(boleto);
        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    // terminar
    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDTO efetuarPagamento(PagamentoRegistroDTO dto) {
        return null;
    }


    private Pagamento buscarPagamentoBoleto(String boleto) {
        var pagamento = pagamentoRepository.findByBoleto(boleto)
                .orElseThrow( () -> new EntidadeNaoEncontradaException("pagamento"));
        return pagamento;
    }

}
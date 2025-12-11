package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.CodigoRegistroDTO;
import com.senai.conta_bancaria.application.dto.CodigoResponseDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.CodigoAutenticacao;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import com.senai.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CodigoAutenticacaoService {

    private final CodigoAutenticacaoRepository codigoAutenticacaoRepository;
    private final ClienteRepository clienteRepository;

    public CodigoResponseDTO registrarCodigo(CodigoRegistroDTO dto){
        Cliente cliente = clienteRepository.findByCpfAndAtivoTrue(dto.cliente())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));

        CodigoAutenticacao codigoAutenticacao = CodigoAutenticacao.builder()
                .codigo(dto.codigo())
                .expiraEm(dto.expiraEm())
                .validado(false)
                .cliente(cliente)
                .build();

        return CodigoResponseDTO.fromEntity(codigoAutenticacaoRepository.save(codigoAutenticacao));
    }

    // validar código
}
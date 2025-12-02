package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.*;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.exception.RendimentoInvalidoException;
import com.senai.conta_bancaria.domain.exception.TipoDeContaInvalidaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {
    private final ContaRepository repository;

    @PreAuthorize("hasRole('GERENTE')")
    @Transactional(readOnly = true)
    public List<ContaResumoDTO> listarTodasContas() {
        return repository
                .findAllByAtivaTrue()
                .stream()
                .map(ContaResumoDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('GERENTE', 'CLIENTE')")
    @Transactional(readOnly = true)
    public ContaResumoDTO buscarContaPorNumero(String numero) {
        return ContaResumoDTO.fromEntity(
                buscarContaAtivaPorNumero(numero)
        );
    }

    @PreAuthorize("hasRole('GERENTE')")
    public ContaResumoDTO atualizarConta(String numeroConta, ContaAtualizacaoDTO dto) {
        Conta conta = buscarContaAtivaPorNumero(numeroConta);

        if (conta instanceof ContaPoupanca poupanca) {
            poupanca.setRendimento(dto.rendimento());
        } else if (conta instanceof ContaCorrente corrente) {
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        } else {
            throw new TipoDeContaInvalidaException("");
        }

        conta.setSaldo(dto.saldo());

        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('GERENTE')")
    public void deletarConta(String numeroConta) {
        Conta conta = buscarContaAtivaPorNumero(numeroConta);

        conta.setAtiva(false);
        repository.save(conta);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO sacar(String numeroConta, ValorSaqueDepositoDTO dto) {
        Conta conta = buscarContaAtivaPorNumero(numeroConta);

        conta.sacar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO depositar(String numeroConta, ValorSaqueDepositoDTO dto) {
        Conta conta = buscarContaAtivaPorNumero(numeroConta);

        conta.depositar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO transferir(String numeroConta, TransferenciaDTO dto) {
        Conta contaOrigem = buscarContaAtivaPorNumero(numeroConta);
        Conta contaDestino = buscarContaAtivaPorNumero(dto.contaDestino());

        contaOrigem.transferir(dto.valor(), contaDestino);

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));
    }


    private Conta buscarContaAtivaPorNumero(String numeroConta) {
        return repository.findByNumeroAndAtivaTrue(numeroConta)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("conta"));
    }

    @PreAuthorize("hasRole('GERENTE')")
    public ContaResumoDTO aplicarRendimento(String numeroConta) {
        Conta conta = buscarContaAtivaPorNumero(numeroConta);

        if (conta instanceof ContaPoupanca poupanca) {
            poupanca.aplicarRendimento();
            return ContaResumoDTO.fromEntity(repository.save(poupanca));
        }
        throw new RendimentoInvalidoException();
    }
}

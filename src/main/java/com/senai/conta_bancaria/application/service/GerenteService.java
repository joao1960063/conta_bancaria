package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.GerenteAtualizadoDTO;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDTO;
import com.senai.conta_bancaria.application.dto.GerenteResponseDTO;
import com.senai.conta_bancaria.domain.entity.Gerente;
import com.senai.conta_bancaria.domain.enums.Role;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenteService {

    private final GerenteRepository gerenteRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDTO registrarGerente(GerenteRegistroDTO dto) {

        var gerente = gerenteRepository
                .findByCpfAndAtivoTrue(dto.cpf())
                .orElseGet(
                        () -> gerenteRepository.save(dto.toEntity())
                );

        gerente.setSenha(passwordEncoder.encode(dto.senha()));
        gerente.setRole(Role.GERENTE);

        return GerenteResponseDTO.fromEntity(gerenteRepository.save(gerente));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<GerenteResponseDTO> listarGerentesAtivos() {
        return gerenteRepository
                .findAllByAtivoTrue()
                .stream()
                .map(GerenteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public GerenteResponseDTO bucarGerenteAtivoPorCpf(String cpf) {
        var gerente = buscarGerentePorCpfEAtivo(cpf);
        return GerenteResponseDTO.fromEntity(gerente);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDTO atualizarGerente(String cpf, GerenteAtualizadoDTO dto) {
        var gerente = buscarGerentePorCpfEAtivo(cpf);

        gerente.setNome(dto.nome());
        gerente.setCpf(dto.cpf());
        gerente.setEmail(dto.email());
        gerente.setSenha(dto.senha());

        return GerenteResponseDTO.fromEntity(gerenteRepository.save(gerente));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletarGerente(String cpf) {
        var gerente = buscarGerentePorCpfEAtivo(cpf);

        gerente.setAtivo(false);

        gerenteRepository.save(gerente);
    }

    private Gerente buscarGerentePorCpfEAtivo(String cpf) {
        var gerente = gerenteRepository.findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("gerente"));

        return gerente;
    }
}

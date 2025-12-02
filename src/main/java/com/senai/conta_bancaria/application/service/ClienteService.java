package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteAtualizadoDTO;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.enums.Role;
import com.senai.conta_bancaria.domain.exception.ContaDoMesmoTipoException;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN', 'GERENTE')")
    public ClienteResponseDTO registrarCliente(ClienteRegistroDTO dto) {

        var cliente = clienteRepository
                .findByCpfAndAtivoTrue(dto.cpf())
                .orElseGet(
                        () -> clienteRepository.save(dto.toEntity())
                );

        var contas = cliente.getContas();
        var novaConta = dto.contaDTO().toEntity(cliente);

        boolean jaTemTipo = contas.stream()
                .anyMatch(c -> c.getClass().equals(novaConta.getClass()) && c.isAtiva());

        if (jaTemTipo)
            throw new ContaDoMesmoTipoException();

        cliente.getContas().add(novaConta);

        cliente.setSenha(passwordEncoder.encode(dto.senha()));
        cliente.setRole(Role.CLIENTE);

        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    @PreAuthorize("hasRole('ADMIN', 'GERENTE')")
    public List<ClienteResponseDTO> listarClientesAtivos() {
        return clienteRepository
                .findAllByAtivoTrue()
                .stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN', 'GERENTE')")
    public ClienteResponseDTO bucarClienteAtivoPorCpf(String cpf) {
        var cliente = buscarClientePorCpfEAtivo(cpf);

        return ClienteResponseDTO.fromEntity(cliente);
    }

    @PreAuthorize("hasRole('ADMIN', 'GERENTE')")
    public ClienteResponseDTO atualizarCliente(String cpf, ClienteAtualizadoDTO dto) {
        var cliente = buscarClientePorCpfEAtivo(cpf);

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setSenha(dto.senha());

        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    @PreAuthorize("hasRole('ADMIN', 'GERENTE')")
    public void deletarCliente(String cpf) {
        var cliente = buscarClientePorCpfEAtivo(cpf);

        cliente.setAtivo(false);
        cliente.getContas().forEach(
                conta -> conta.setAtiva(false)
        );

        clienteRepository.save(cliente);
    }

    private Cliente buscarClientePorCpfEAtivo(String cpf) {
        var cliente = clienteRepository.findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));
        return cliente;
    }
}

package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ClienteAtualizadoDTO;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.application.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Clientes", description = "Gerenciamento de clientes.")
@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    @Operation(
            summary = "Criar cliente",
            description = "Cria um cliente e sua conta bancária depois que as validações forem feitas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo válido",
                                    value = """
                                        {
                                          "nome": "Kianthe Reyna",
                                          "cpf": 12345678910,
                                          "email": "r.kianthe@email.com",
                                          "senha": "Rk@12345"
                                          "contaDTO": {
                                                "numero": "7654321",
                                                "tipo": "CORRENTE",
                                                "saldo": 100.00
                                          }
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Senha incompleta",
                                                    value = "A senha deve ter no mínimo 8 caracteres, incluindo letra maiúscula, minúscula, número e caractere especial"),
                                            @ExampleObject(
                                                    name = "CPF inválido",
                                                    value = "O CPF deve ter 11 dígitos numéricos")
                                    }
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRegistroDTO dto){
        ClienteResponseDTO novoCliente = service.registrarCliente(dto);

        return ResponseEntity.created(URI.create("/api/cliente/cpf/" + novoCliente.cpf()))
                .body(novoCliente);
    }

    @Operation(
            summary = "Listar clientes",
            description = "Retorna todos os clientes ativos do banco de dados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Clientes não encontradas.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Clientes não encontradas.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos(){
        return ResponseEntity.ok(service.listarClientesAtivos());
    }

    @Operation(
            summary = "Buscar cliente por CPF",
            description = "Retorna um cliente ativo rastreado pelo seu CPF",
            parameters = {
                    @Parameter(
                            name = "CPF",
                            description = "CPF do cliente que anseia buscar",
                            example = "12345678910"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente encontrado com sucesso."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Cliente com CPF 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> buscarClienteAtivoPorCpf(@PathVariable String cpf){
        return ResponseEntity.ok(service.bucarClienteAtivoPorCpf(cpf));
    }

    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza e salva os dados de um cliente",
            parameters = {
                    @Parameter(
                            name = "CPF",
                            description = "CPF do cliente que anseia atualizar",
                            example = "12345678910"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo válido", value = """
                                        {
                                          "nome": "Kianthe Reyna",
                                          "cpf": 12345678910,
                                          "email": "r.kianthe@email.com",
                                          "senha": "Rk@12345"
                                         }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente atualizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Senha incompleta",
                                                    value = "A senha deve ter no mínimo 8 caracteres, incluindo letra maiúscula, minúscula, número e caractere especial"),
                                            @ExampleObject(
                                                    name = "CPF inválido",
                                                    value = "O CPF deve ter 11 dígitos numéricos")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Cliente com CPF 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @PutMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable String cpf,
                                                               @Valid @RequestBody ClienteAtualizadoDTO dto) {
        return ResponseEntity.ok(service.atualizarCliente(cpf, dto));
    }

    @Operation(
            summary = "Deletar cliente",
            description = "Desativa a conta e o registro de um cliente da aplicação",
            parameters = {
                    @Parameter(
                            name = "CPF",
                            description = "CPF do cliente que anseia buscar",
                            example = "12345678910"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Cliente deletado com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Cliente com CPF 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String cpf){
        service.deletarCliente(cpf);
        return ResponseEntity.noContent().build();
    }
}

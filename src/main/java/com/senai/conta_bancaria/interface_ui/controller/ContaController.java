package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDTO;
import com.senai.conta_bancaria.application.dto.ContaResumoDTO;
import com.senai.conta_bancaria.application.dto.TransferenciaDTO;
import com.senai.conta_bancaria.application.dto.ValorSaqueDepositoDTO;
import com.senai.conta_bancaria.application.service.ContaService;
import com.senai.conta_bancaria.domain.entity.Conta;
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

import java.util.List;

@Tag(name = "Contas", description = "Gerenciamento de contas bancárias dos clientes.")
@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService service;

    @Operation(
            summary = "Listar contas",
            description = "Retorna todas as contas cadastradas do banco de dados",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de contas retornadas com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Contas não encontradas.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Contas não encontradas.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @GetMapping
    public ResponseEntity<List<ContaResumoDTO>> listarTodasContas() {
        return ResponseEntity.ok(service.listarTodasContas());
    }

    @Operation(
            summary = "Buscar conta por número",
            description = "Retorna uma conta rastreado pelo seu número",
            parameters = {
                    @Parameter(
                            name = "numero",
                            description = "Conta que anseia buscar",
                            example = "1592-8")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Conta '10394-8' não encontrada.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @GetMapping("/{numeroConta}")
    public ResponseEntity<ContaResumoDTO> buscarContaPorNumero(@PathVariable String numeroConta) {
        return ResponseEntity.ok(service.buscarContaPorNumero(numeroConta));
    }

    @Operation(
            summary = "Atualizar conta bancária",
            description = "Atualiza e salva os dados de uma conta",
            parameters = {
                    @Parameter(
                            name = "numero",
                            description = "Numero da conta que anseia atualizar",
                            example = "1592-8")
            },
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ContaAtualizacaoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de atualização",
                                    value = """
                                        {
                                          "saldo":100.0,
                                          "limite":800.0,
                                          "taxa":0.02
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta atualizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Tipo inválido",
                                                    value = "Tipo de conta desconhecido. Os únicos valores válidos são: 'CORRENTE' e 'POUPANCA'."),
                                            @ExampleObject(
                                                    name = "Saldo negativo",
                                                    value = "O saldo não pode ser negativo"),
                                            @ExampleObject(
                                                    name = "Limite negativo",
                                                    value = "O limite não pode ser negativo")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Conta '10394-8' não encontrada.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @PutMapping("/{numeroConta}")
    public ResponseEntity<ContaResumoDTO> atualizarConta(@PathVariable String numeroConta,
                                                         @Valid @RequestBody ContaAtualizacaoDTO dto) {
        return ResponseEntity.ok(service.atualizarConta(numeroConta, dto));
    }

    @Operation(
            summary = "Deletar conta",
            description = "Desativa uma conta a partir do seu numero",
            parameters = {
                    @Parameter(
                            name = "numero",
                            description = "Numero da conta que anseia desativar",
                            example = "1592-8")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Conta 1592-8 não encontrada.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @DeleteMapping("/{numeroConta}")
    public ResponseEntity<Void> deletarConta(@PathVariable String numeroConta) {
        service.deletarConta(numeroConta);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Sacar valor de uma conta",
            description = "Saca um determinado valor na conta do cliente",
            parameters = {
                    @Parameter(
                            name = "numero",
                            description = "Numero da conta que deseja sacar",
                            example = "1592-8")
            },
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de saque",
                                    value = """
                                        {
                                          "valor":5.0
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Saldo Insuficiente", value = "Saldo insuficiente para realizar a operação"),
                                            @ExampleObject(name = "Valor negativo", value = "O valor não pode ser negativo")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Conta '10394-8' não encontrada.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('CLIENTE')")
    @PostMapping("/{numeroConta}/sacar")
    public ResponseEntity<ContaResumoDTO> sacar(@PathVariable String numeroConta,
                                                @Valid @RequestBody ValorSaqueDepositoDTO dto) {
        return ResponseEntity.ok(service.sacar(numeroConta, dto));
    }

    @Operation(
            summary = "Depositar valor em uma conta",
            description = "Deposita um determinado valor na conta do cliente",
            parameters = {
                    @Parameter(
                            name = "numero",
                            description = "Numero da conta que deseja depositar",
                            example = "1592-8")
            },
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de depósito",
                                    value = """
                                        {
                                          "valor":500.0
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Valor negativo", value = "O valor não pode ser negativo")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Conta '10394-8' não encontrada.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('CLIENTE')")
    @PostMapping("/{numeroConta}/depositar")
    public ResponseEntity<ContaResumoDTO> depositar(@PathVariable String numeroConta,
                                                    @Valid @RequestBody ValorSaqueDepositoDTO dto) {
        return ResponseEntity.ok(service.depositar(numeroConta, dto));
    }

    @Operation(
            summary = "Transferir valor em uma conta",
            description = "Transfere um determinado valor de uma conta para outra conta",
            parameters = {
                    @Parameter(
                            name = "numero",
                            description = "Numero da conta que deseja trasnferir",
                            example = "1592-8")
            },
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de depósito",
                                    value = """
                                        {
                                          "numero": "1592-8",
                                          "valor":200.0
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência realizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Valor negativo", value = "O valor não pode ser negativo"),
                                            @ExampleObject(name = "Transferir para a mesma conta",
                                                    value = "Não é possível transferir para a mesma conta.")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Conta 1592-8 não encontrada.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('CLIENTE')")
    @PostMapping("/{numeroConta}/transferir")
    public ResponseEntity<ContaResumoDTO> transferir(@PathVariable String numeroConta,
                                                @Valid @RequestBody TransferenciaDTO dto) {
        return ResponseEntity.ok(service.transferir(numeroConta, dto));
    }

    @Operation(
            summary = "Aplicar rendimento da conta",
            description = "Aplica um valor em porcentagem para ser rendido em contas Poupança.",
            parameters = {
                    @Parameter(name = "numero",
                            description = "Numero da conta que deseja aplicar o rendimento",
                            example = "1592-8")
            },
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de aplicação", value = """
                                        {
                                          "rendimento":0.04
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rendimento aplicado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Rendimento em tipo de conta inválida",
                                                    value = "Rendimento pode ser aplicado somente na conta Poupança.")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Conta 1592-8 não encontrada.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('GERENTE')")
    @PostMapping("/{numeroConta}/rendimento")
    public ResponseEntity<ContaResumoDTO> aplicarRendimento(@PathVariable String numeroConta) {
        return ResponseEntity.ok(service.aplicarRendimento(numeroConta));
    }
}



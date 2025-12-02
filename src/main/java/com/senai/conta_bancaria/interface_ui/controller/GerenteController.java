package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.*;
import com.senai.conta_bancaria.application.service.GerenteService;
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

@Tag(name = "Gerentes", description = "Gerenciamento de gerentes.")
@RestController
@RequestMapping("/api/gerente")
@RequiredArgsConstructor
public class GerenteController {

    private final GerenteService service;

    @Operation(
            summary = "Criar gerente",
            description = "Cria um gerente depois que as validações forem feitas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteRegistroDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo válido",
                                    value = """
                                            {
                                              "nome": "Edra Iris",
                                              "cpf": "12345678910",
                                              "email":"i.edra@email.com",
                                              "senha": "Ie@12345",
                                            }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Gerente cadastrado com sucesso."),
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
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity <GerenteResponseDTO> registrarGerente(@Valid @RequestBody GerenteRegistroDTO dto){
        GerenteResponseDTO novoGerente = service.registrarGerente(dto);

        return ResponseEntity.created(URI.create("/api/gerente/cpf"+ novoGerente.cpf()))
                .body(novoGerente);
    }

    @Operation(
            summary = "Listar gerentes ativos",
            description = "Retornar todos os gerentes ativos no banco de dados",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de gerentes retornada com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerentes não encontradas.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Gerentes não encontradas.")
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GerenteResponseDTO>> listarGerentesAtivos(){
        return ResponseEntity.ok(service.listarGerentesAtivos());
    }

    @Operation(
            summary = "Buscar gerente por CPF",
            description = "Retorna um gerente ativo rastreado pelo seu CPF",
            parameters = {
                    @Parameter(
                            name = "CPF",
                            description = "CPF do gerente que anseia buscar",
                            example = "12345678910"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Gerente encontrado com sucesso!"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Gerente com CPF 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<GerenteResponseDTO> bucarGerenteAtivoPorCpf(@PathVariable String cpf){
        return ResponseEntity.ok(service.bucarGerenteAtivoPorCpf(cpf));
    }

    @Operation(
            summary = "Atualizar gerente",
            description = "Atualiza e salva os dados de um gerente",
            parameters = {
                    @Parameter(
                            name = "CPF",
                            description = "CPF do gerente que deseja atualizar",
                            example = "12345678910"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteRegistroDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo válido",
                                    value = """
                                        {
                                          "nome": "Edra Iris",
                                          "cpf": "12345678910",
                                          "email":"i.edra@email.com",
                                          "senha": "Ie@12345",
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Gerente atualizado com sucesso."),
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
                            description = "Gerente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Gerente com CPF 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity <GerenteResponseDTO> atualizarGerente(@PathVariable String cpf,
                                                                @Valid @RequestBody GerenteAtualizadoDTO dto){
        return ResponseEntity.ok(service.atualizarGerente(cpf,dto));
    }

    @Operation(
            summary = "Deletar gerente",
            description = "Desativa a conta e o registro de um gerente da aplicação",
            parameters = {
                    @Parameter(
                            name = "CPF",
                            description = "CPF do gerente que anseia buscar",
                            example = "12345678910"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Gerente deletado com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Gerente com CPF 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @DeleteMapping("/{cpf}")
    public ResponseEntity <Void> deletarGerente(@PathVariable String cpf){
        service.deletarGerente(cpf);
        return ResponseEntity.noContent().build();
    }
}

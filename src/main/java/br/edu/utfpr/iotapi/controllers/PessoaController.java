package br.edu.utfpr.iotapi.controllers;

import br.edu.utfpr.iotapi.core.CurrentUserEmail;
import br.edu.utfpr.iotapi.dto.DeletePessoaDTO;
import br.edu.utfpr.iotapi.dto.GetPessoaDTO;
import br.edu.utfpr.iotapi.dto.UpdatePessoaDTO;
import br.edu.utfpr.iotapi.dto.gateway.GetGatewayDTO;
import br.edu.utfpr.iotapi.exceptions.NotFoundException;
import br.edu.utfpr.iotapi.models.Pessoa;
import br.edu.utfpr.iotapi.services.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @GetMapping()
    public ResponseEntity<GetPessoaDTO> getById(@CurrentUserEmail String email) {
        var pessoa = pessoaService.getByEmail(email);

        return ResponseEntity.ok().body(pessoa);
    }

    @GetMapping("/{id}/gateway")
    public List<GetGatewayDTO> getGatewaysByPessoaId(@PathVariable("id") long id) {
        return pessoaService.getGatewaysByPessoaId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> update(@PathVariable("id") long id, @Valid @RequestBody UpdatePessoaDTO dto)
            throws NotFoundException {
        var res = pessoaService.update(id, dto);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pessoa> delete(@PathVariable("id") long id, @Valid @RequestBody DeletePessoaDTO dto)
            throws NotFoundException {
        pessoaService.checkPassword(id, dto.senha());

        pessoaService.delete(id);
        return ResponseEntity.ok().build();
    }
}

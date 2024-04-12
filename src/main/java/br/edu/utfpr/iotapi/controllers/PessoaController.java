package br.edu.utfpr.iotapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.edu.utfpr.iotapi.dto.CreatePessoaDTO;
import br.edu.utfpr.iotapi.dto.DeletePessoaDTO;
import br.edu.utfpr.iotapi.exceptions.NotFoundException;
import br.edu.utfpr.iotapi.exceptions.WrongPasswordException;
import br.edu.utfpr.iotapi.models.Pessoa;
import br.edu.utfpr.iotapi.services.PessoaService;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
  @Autowired
  private PessoaService pessoaService;

  @GetMapping
  public List<Pessoa> getAll() {
    return pessoaService.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Pessoa> getById(@PathVariable("id") long id) {
    var person = pessoaService.getById(id);

    return person.isPresent()
        ? ResponseEntity.ok().body(person.get())
        : ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<Pessoa> create(@RequestBody CreatePessoaDTO dto) {
    try {
      var res = pessoaService.create(dto);

      return ResponseEntity.status(HttpStatus.CREATED).body(res);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Pessoa> update(@PathVariable("id") long id, @RequestBody CreatePessoaDTO dto) {
    try {
      var res = pessoaService.update(dto, id);
      return ResponseEntity.ok().body(res);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, e.getMessage(), e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Pessoa> delete(@PathVariable("id") long id, @RequestBody DeletePessoaDTO dto) {
    try {
      pessoaService.checkPassword(id, dto.senha());

      pessoaService.delete(id);
      return ResponseEntity.ok().build();
    } catch (NotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, e.getMessage(), e);
    } catch (WrongPasswordException e) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, e.getMessage(), e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }
}

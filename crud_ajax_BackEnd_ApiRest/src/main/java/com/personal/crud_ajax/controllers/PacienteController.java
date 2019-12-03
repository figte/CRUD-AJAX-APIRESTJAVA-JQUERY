package com.personal.crud_ajax.controllers;

import java.util.List;

import com.personal.crud_ajax.models.Paciente;
import com.personal.crud_ajax.repositories.IPaciente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * PacienteController
 */
@Controller
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    IPaciente rPaciente;

      //listar registros
      @GetMapping(value="all",produces = MediaType.APPLICATION_JSON_VALUE)
      @ResponseBody
      public List<Paciente> getAllPaciente() {
          return (List<Paciente>) rPaciente.findAll();
      }

    
}
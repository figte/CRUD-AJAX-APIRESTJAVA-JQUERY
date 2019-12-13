package com.personal.crud_ajax.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.personal.crud_ajax.models.Consulta;
import com.personal.crud_ajax.models.DetallesConsulta;
import com.personal.crud_ajax.models.Doctor;
import com.personal.crud_ajax.models.Paciente;
import com.personal.crud_ajax.repositories.IPaciente;
import com.personal.crud_ajax.services.ConsultaService;
import com.personal.crud_ajax.services.DoctorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * ConsultaController
 */
@Controller
@RequestMapping("consultas")
public class ConsultaController {

    @Autowired
    ConsultaService consultaService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    IPaciente pacienteRepository;

    public static List<DetallesConsulta> detalles=new ArrayList<>();


    public ConsultaController() {
        detalles=new ArrayList<>();
    }


    // @PostMapping(value="getDoctores", produces = MediaType.APPLICATION_JSON_VALUE)
    // @ResponseBody
    // @CrossOrigin
    // public Object getDoctores( ) {
    //     //TODO: process POST request
    //     return doctorService.getAll();
    // }
    @GetMapping(value="getDoctores", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public Object getDoctores( ) {
        List<HashMap<String,Object>> registros=new ArrayList<>();

        List<Doctor> l= doctorService.getAll();

        for (Doctor i : l) {
            HashMap<String,Object> object=new HashMap<>();
            
            object.put("id", i.getId());
            object.put("nombre", i.getNombre());
            object.put("direccion", i.getDireccion());
            object.put("especialidad", i.getEspecialidad().getEspecialidad());
            object.put("operacion","<button type='button' class='btn btn-primary agregarDoctor'  data-dismiss='modal'>Agregar</button>");
               
            registros.add(object);
        }

        //TODO: process POST request
        return Collections.singletonMap("data", registros);
    }

    @GetMapping(value="getPacientes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public Object getPacientes( ) {
        List<HashMap<String,Object>> registros=new ArrayList<>();

        List<Paciente> l=(List<Paciente>) pacienteRepository.findAll();

        for (Paciente paciente : l) {
            HashMap<String,Object> object=new HashMap<>();
            
            object.put("id", paciente.getId());
            object.put("nombre", paciente.getNombre());
            object.put("direccion", paciente.getDireccion());
            object.put("operacion","<button type='button' class='btn btn-primary agregarPaciente'  data-dismiss='modal'>Agregar</button>");
               
            registros.add(object);
        }

        //TODO: process POST request
        return Collections.singletonMap("data", registros);
    }

    @PostMapping(value="agregarDetalle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public Object  agregaDetalle(@RequestParam String sintoma) {
       //creando objeto de detalle y agregandole la data recibida en la peticion
        DetallesConsulta entity=new DetallesConsulta();
        entity.setSintoma(sintoma);

        //agregando el onjeto detalle a las lista
        detalles.add(entity);

        HashMap<String,String> json= new HashMap<String,String>();
        json.put("mensaje", "Detalle agregado correctamente");
        json.put("status", "200");

        return  json;
    }    

    @GetMapping(value="detalles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public Object getDetalles() {
        return detalles;
    }

    @PostMapping(value="resetDetalles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public Object resetDetalles() {
        detalles=new ArrayList<>();
        return "lista reseteada";
    }
    
    //listar registros
    @GetMapping(value="all",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Consulta> getAllConsulta() {

        return (List<Consulta>) consultaService.getAll();
    }

       //guardar
       @PostMapping(value="save")
       @ResponseBody
       @CrossOrigin
       public HashMap<String,String> save(@RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
                           @RequestParam String diagnostico,
                           @RequestParam Integer idDoctor,
                           @RequestParam Integer idPaciente) {
         
           HashMap<String,String> jsonReturn=new HashMap<>();
           
           Consulta entity=new Consulta(); //creando objeto 
           //asignando datos al objeto 
            entity.setFecha(fecha);
           // entity.setSintomas(sintomas);
            entity.setDiagnostico(diagnostico);
            entity.setDoctor(consultaService.getDoctor(idDoctor));
            entity.setPaciente(pacienteRepository.findById(idPaciente).get());

            for (DetallesConsulta detallesConsulta : detalles) {
                detallesConsulta.setConsulta(entity);
            }
            
            entity.setDetallesConsultas(detalles);
           //manejando cualquier excepcion de error
           try {


               consultaService.saveOrUpdate(entity); //guardando registro de doctor
   
               jsonReturn.put("estado", "OK");
               jsonReturn.put("mensaje", "Registro guardado");
   
               return jsonReturn;
           } catch (Exception e) {
               jsonReturn.put("estado", "ERROR");
               jsonReturn.put("mensaje", "Registro no guardado, "+e.getMessage());
               
               return jsonReturn;
           }
       }
   
       //modificar
       @GetMapping(value="update/{id}")
       @ResponseBody
       public HashMap<String,String> update(@RequestParam Integer id,
                           @RequestParam Date fecha,
                           @RequestParam String sintomas,
                           @RequestParam String diagnostico,
                           @RequestParam Integer idDoctor) {
         
           HashMap<String,String> jsonReturn=new HashMap<>();
           
           Consulta entity=new Consulta(); //creando objeto 
           //asignando datos al objeto 
            entity.setId(id);
            entity.setFecha(fecha);
           // entity.setSintomas(sintomas);
            entity.setDiagnostico(diagnostico);
            entity.setDoctor(consultaService.getDoctor(idDoctor));
           //manejando cualquier excepcion de error
           try {
               consultaService.saveOrUpdate(entity); //guardando registro de doctor
   
               jsonReturn.put("estado", "OK");
               jsonReturn.put("mensaje", "Registro actualizado");
   
               return jsonReturn;
           } catch (Exception e) {
               jsonReturn.put("estado", "ERROR");
               jsonReturn.put("mensaje", "Registro no actualizado, "+e.getMessage());
               
               return jsonReturn;
           }
       }

        //eliminar
    @GetMapping(value="delete/{id}")
    @ResponseBody
    public HashMap<String,String> delete(@PathVariable Integer id) {
        HashMap<String,String> jsonReturn=new HashMap<>();
        try {
            //buscando registro
            Consulta entity=consultaService.getConsulta(id);
            //eliminando registro
            consultaService.delete(entity);

            jsonReturn.put("estado", "OK");
            jsonReturn.put("mensaje", "Registro eliminado");
            return jsonReturn;
        } catch (Exception e) {
            jsonReturn.put("estado", "ERROR");
            jsonReturn.put("mensaje", "Registro no eliminado, "+e.getMessage());
            
            return jsonReturn;
        }
        
    }
       
    
}
package com.personal.crud_ajax.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.personal.crud_ajax.models.Consulta;
import com.personal.crud_ajax.services.ConsultaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ConsultaController
 */
@Controller
@RequestMapping("consultas")
public class ConsultaController {

    @Autowired
    ConsultaService consultaService;

    //listar registros
    @GetMapping(value="all",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Consulta> getAllConsulta() {

        return (List<Consulta>) consultaService.getAll();
    }

       //guardar
       @GetMapping(value="save")
       @ResponseBody
       public HashMap<String,String> save(@RequestParam Date fecha,
                           @RequestParam String sintomas,
                           @RequestParam String diagnostico,
                           @RequestParam Integer idDoctor) {
         
           HashMap<String,String> jsonReturn=new HashMap<>();
           
           Consulta entity=new Consulta(); //creando objeto 
           //asignando datos al objeto 
            entity.setFecha(fecha);
            entity.setSintomas(sintomas);
            entity.setDiagnostico(diagnostico);
            entity.setDoctor(consultaService.getDoctor(idDoctor));
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
            entity.setSintomas(sintomas);
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
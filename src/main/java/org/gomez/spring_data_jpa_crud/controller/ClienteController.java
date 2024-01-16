package org.gomez.spring_data_jpa_crud.controller;

import org.gomez.spring_data_jpa_crud.model.entity.Cliente;
import org.gomez.spring_data_jpa_crud.model.service.IClienteService;
import org.gomez.spring_data_jpa_crud.util.paginable.PageRender;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;
    private final static String UPLOAD_FOLDER = "/uploads";
    private final Logger log = LoggerFactory.getLogger(getClass());

    // Método para ver la foto asociada a un cliente
    @GetMapping(value = "/upload/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        // Resuelve la ruta de la foto y la convierte en un recurso
        Path pathFoto = Paths.get(UPLOAD_FOLDER).resolve(filename).toAbsolutePath();
        log.info("PathFoto: " + pathFoto);
        Resource resource = null;
        try {
            resource = new UrlResource(pathFoto.toUri());
            // Verifica la existencia y legibilidad del recurso
            if (!resource.exists() && !resource.isReadable()) {
                throw new RuntimeException("Error: No se puede cargar la imagen " + pathFoto.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Devuelve la respuesta con el recurso de la foto
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""
                + resource.getFilename() + "\"").body(resource);
    }

    // Método para visualizar detalles de un cliente
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model,
                      RedirectAttributes flash) {
        // Busca el cliente por ID
        Cliente cliente = clienteService.findOne(id);
        // Si el cliente no existe, redirige y muestra un mensaje de error
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/listar";
        }
        // Agrega el cliente al modelo y retorna la vista de detalles
        model.put("cliente", cliente);
        model.put("Titulo", "Detalle Cliente: " + cliente.getNombre());
        return "ver";
    }

    // Método para listar clientes paginados
    @GetMapping(value = "/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        // Configura la paginación y obtiene la lista de clientes
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        // Configura el objeto PageRender para la paginación en la vista
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        // Agrega los atributos al modelo y retorna la vista de lista de clientes
        model.addAttribute("titulo", "Listado de Clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    // Método para mostrar el formulario de creación de cliente
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        // Crea un nuevo objeto Cliente y lo agrega al modelo
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    // Método para guardar un nuevo cliente o actualizar uno existente
    @PostMapping(value = "/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
                          RedirectAttributes flash, SessionStatus status, @RequestParam("file") MultipartFile foto) {
        // Maneja errores de validación
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        // Procesa la foto si se proporciona
        if (!foto.isEmpty()) {
            // Elimina la foto anterior si el cliente ya tenía una
            if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
                Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
                File archivo = rootPath.toFile();
                if (archivo.exists() && archivo.canRead()) {
                    if (archivo.delete()) {
                        archivo.delete();
                    }
                }
            }

            // Guarda la nueva foto con un nombre único
            String uniqueFileName = UUID.randomUUID().toString() + "-" + foto.getOriginalFilename();
            Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(uniqueFileName);
            Path rootAbsolute = rootPath.toAbsolutePath();
            log.info("rootPath: " + rootPath);
            log.info("rootAbsolutePath: " + rootAbsolute);
            try {
                // Copia el contenido del archivo de la foto al nuevo archivo en el sistema de archivos
                Files.copy(foto.getInputStream(), rootAbsolute);
                // Actualiza el nombre de la foto en el objeto cliente
                cliente.setFoto(uniqueFileName);
                // Mensaje informativo sobre la subida exitosa
                flash.addAttribute("info", "Se ha subido correctamente '" + uniqueFileName + "'");
                // Guarda el cliente en la base de datos
                this.clienteService.save(cliente);
                // Completa la sesión y agrega un mensaje de éxito
                status.setComplete();
                flash.addFlashAttribute("success", (cliente.getId() != null) ? "Cliente editado con éxito" : "Cliente creado con éxito");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Redirige a la lista de clientes
        return "redirect:listar";
    }

    // Método para mostrar el formulario de edición de cliente
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        // Verifica que el ID sea válido
        if (id > 0) {
            // Busca el cliente por ID
            Cliente cliente = clienteService.findOne(id);
            // Si el cliente no existe, redirige y muestra un mensaje de error
            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del Cliente no existe en la Base de Datos");
                return "redirect:/listar";
            }
            // Agrega el cliente al modelo y retorna la vista de formulario de edición
            model.put("cliente", cliente);
            model.put("titulo", "Editar Cliente");
            return "form";
        } else {
            // Si el ID no es válido, redirige y muestra un mensaje de error
            flash.addFlashAttribute("error", "El ID del Cliente no puede ser cero");
            return "redirect:/listar";
        }
    }

    // Método para eliminar un cliente
    @RequestMapping(value = "eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        // Verifica que el ID sea válido
        if (id > 0) {
            // Busca el cliente por ID
            Cliente cliente = clienteService.findOne(id);
            // Elimina el cliente de la base de datos
            clienteService.delete(id);
            // Agrega un mensaje de éxito
            flash.addFlashAttribute("success", "Cliente Eliminado con Éxito");
            // Elimina la foto asociada al cliente en el sistema de archivos
            Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
            File archivo = rootPath.toFile();
            if (archivo.exists() && archivo.canRead()) {
                if (archivo.delete()) {
                    flash.addFlashAttribute("info", "Foto eliminada: " + cliente.getFoto() +
                            " se eliminó correctamente");
                }
            }
        }
        // Redirige a la lista de clientes
        return "redirect:/listar";
    }
}

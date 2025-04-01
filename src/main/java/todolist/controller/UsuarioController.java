package todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import todolist.dto.UsuarioData;
import todolist.service.UsuarioService;
import todolist.authentication.ManagerUserSession;
import todolist.service.UsuarioServiceException;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ManagerUserSession managerUserSession;

    public UsuarioController(UsuarioService usuarioService, ManagerUserSession managerUserSession) {
        this.usuarioService = usuarioService;
        this.managerUserSession = managerUserSession;
    }

    // Nuevo endpoint para actualizar estado de usuario
    @PutMapping("/registered/{id}/status")
    @ResponseBody
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id,
            @RequestParam boolean enabled
    ) {
        Long loggedUserId = managerUserSession.usuarioLogeado();

        // Verificar autenticación
        if (loggedUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            usuarioService.toggleUserStatus(id, enabled);
            return ResponseEntity.ok().build();
        } catch (UsuarioServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/registered")
    public String listUsuarios(Model model) {
        Long usuarioId = managerUserSession.usuarioLogeado();

        // Verificar autenticación
        boolean loggedIn = (usuarioId != null);
        model.addAttribute("loggedIn", loggedIn);

        if (!loggedIn) {
            return "redirect:/login";
        }

        // Añadir datos del usuario logeado
        UsuarioData usuario = usuarioService.findById(usuarioId);
        model.addAttribute("usuario", usuario);

        // Añadir lista de usuarios
        model.addAttribute("usuarios", usuarioService.findAllUsuarios());

        return "usersList";
    }

    @GetMapping("/registered/{id}")
    public String verUsuario(@PathVariable Long id, Model model) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();

        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        try {
            UsuarioData usuario = usuarioService.findById(id);
            model.addAttribute("loggedIn", true);
            model.addAttribute("usuarioLogeado", usuarioService.findById(usuarioLogeadoId));
            model.addAttribute("usuario", usuario);
            return "usersDescription";

        } catch (RuntimeException e) {
            return "redirect:/registered"; // Redirige si hay error
        }
    }
}
package todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import todolist.dto.UsuarioData;
import todolist.service.UsuarioService;
import todolist.authentication.ManagerUserSession;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ManagerUserSession managerUserSession;

    public UsuarioController(UsuarioService usuarioService, ManagerUserSession managerUserSession) {
        this.usuarioService = usuarioService;
        this.managerUserSession = managerUserSession;
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

        return "listaUsuarios";
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
            return "descripcionUsuario";

        } catch (RuntimeException e) {
            return "redirect:/registered"; // Redirige si hay error
        }
    }
}
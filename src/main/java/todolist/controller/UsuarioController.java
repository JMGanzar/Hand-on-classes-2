package todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        if (usuarioId == null) { // Si no está autenticado, redirige a login
            return "redirect:/login";
        }
        model.addAttribute("usuarios", usuarioService.findAllUsuarios());
        return "listaUsuarios";
    }
}
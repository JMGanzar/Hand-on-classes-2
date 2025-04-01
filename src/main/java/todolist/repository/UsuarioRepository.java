package todolist.repository;

import todolist.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    // Método para verificar si existe un usuario con el campo admin = true/false
    boolean existsByAdmin(boolean admin);
}
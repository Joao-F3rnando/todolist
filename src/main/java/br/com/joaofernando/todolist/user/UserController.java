package br.com.joaofernando.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * O userModel vai vir do body (corpo) da requisição
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var validateUser = this.userRepository.findByUsername(userModel.getUsername());

        if (validateUser != null) {
            // Mensagem de erro
            // Status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já está cadastrado");
        }

        // Criptografa a senha, e troca no objeto do usuário
        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    // Rota para testar a validação de username criada
    @PostMapping("/validate")
    public UserModel validate(@RequestBody UserModel userModel) {
        var teste = this.userRepository.findByUsername(userModel.getUsername());
        return teste;
    }
}

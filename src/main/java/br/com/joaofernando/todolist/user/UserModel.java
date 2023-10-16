package br.com.joaofernando.todolist.user;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Coloca os getters e setters das variaveis

@Entity(name = "tb_users")
// @getter - coloca somente os getters
// @setter - coloca somente os setters

public class UserModel {
    // Se não colocar nada, é público por padrão
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    // @Column(name = "usuários") //Se quiser dizer que a coluna terá outro nome
    @Column(unique = true)
    private String username;
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDate createdAt;
}

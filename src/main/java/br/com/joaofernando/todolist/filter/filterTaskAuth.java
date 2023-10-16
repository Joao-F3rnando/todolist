package br.com.joaofernando.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.joaofernando.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class filterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tasks/")) {
            // Pegara autenticação (usuario e senha)
            var authorization = request.getHeader("authorization");
            if (authorization != null) {
                var authEncoded = authorization = authorization.substring("Basic".length()).trim();

                var authDecode = Base64.getDecoder().decode(authEncoded);
                var authString = new String(authDecode);

                String[] credentials = authString.split(":");
                // Validar Usuário
                var user = this.userRepository.findByUsername(credentials[0]);

                if (user == null) {
                    response.sendError(401, "Usuário não autorizado");
                } else {
                    // Validar a senha
                    var passwordVerify = BCrypt.verifyer().verify(credentials[1].toCharArray(), user.getPassword());
                    if (passwordVerify.verified) {
                        request.setAttribute("idUser", user.getId());
                        filterChain.doFilter(request, response);
                    } else {
                        response.sendError(401, "Senha incorreta");
                    }
                }
            } else {
                response.sendError(401, "As credenciais não foram enviadas");
            }

        } else {
            filterChain.doFilter(request, response);
        }

    }

}

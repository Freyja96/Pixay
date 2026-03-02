package es.daw.pixaymvc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Habilita @PreAuthorize y @PostAuthorize
@RequiredArgsConstructor
//@EnableWebSecurity // No es necesario con Spring Boot 3.x / Spring Security 6.x
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                /*
                CSRF (Cross-Site Request Forgery) es un tipo de ataque donde un sitio malicioso puede hacer que un navegador autenticado
                (por ejemplo, con una cookie activa) haga una petición no deseada a otro sitio en el que el usuario está logueado.
                Es un ataque clásico en aplicaciones web basadas en sesiones y cookies.
                Su objetivo es hacer que el navegador del usuario realice una acción sin su consentimiento aprovechando que ya tiene una sesión abierta.
                 */
                /*
                ¿Por qué NO necesitas CSRF en una aplicación web monolítica?
                    - Si usas cookies para la autenticación
                    - Si hay formularios web
                 */
                //.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                // Le dice a Spring Security cómo debe manejar las sesiones HTTP
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // permitir iframes (para H2)
                // Esto actúa antes del controlador. Para decir qué urls son públicas, cuáles necesitan autorización, etc.
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/login", "/h2-console/**").permitAll() // pública para login/register
                                .anyRequest().authenticated() //enviar jwt
                )
                .formLogin(login -> login.loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/error"))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Sin este bean, Spring no sabrá cómo inyectar AuthenticationManager en tus clases.
    // Lo usamos en AuthController
    // No lo necesitas si todo el proceso de autenticación lo maneja Spring automáticamente, como cuando usas formLogin()
    // En una API REST con JWT, donde tú haces la autenticación manualmente y devuelves un token (como tú estás haciendo), sí lo necesitas.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

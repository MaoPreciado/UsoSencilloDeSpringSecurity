package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //indica al contenedor de spring que esta es una clase de config de spring security
public class SecurityConfig {

    //SecurityFilterChain es una interfaz para configurar la seguridad. Esta interfaz
    //debe recibir si o si HttpSecurity, un objeto crucial para crear configuracion y parametros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/v1/index2").permitAll()
                                .anyRequest().authenticated())
                .formLogin(form -> form
                        .defaultSuccessUrl("/v1/session", true)
                        .permitAll())
                .sessionManagement(session ->{session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .expiredUrl("/login");
                    session.sessionFixation().migrateSession();
                });

        return http.build();

        /*El codigo de arriba es un cosumer, la forma moderna de trabajar con spring security. Aqui le decimos a atravez de un objeto
         * de tipo HttpSecurity y la propiedad authorizaeHttpRequest que permita a los enpoints dentro del requestMatcher el acceso sin
         * seguridad, para todos los demas sera necesario una autentificacion a traves del formulario (formLogin).
         * el consumer dentro de formlogin esta personalizado para redireccionar a una url especifica, si lo quiero dejar custom podria
         * ser simplemente asi formLogin(Customizer.withDefaults())*/

        //No estamos haciendo .csrf().disable() por que no nos interesa desactivar la proteccion estandar de spring para el trabajo
        //con usuarios y formularios. Esto nos protege contra Cross-Site Request Forgery (Falsificación de Petición en Sitios Cruzados).

    }

    //Este metodo nos permite recuperar la informacion de la sesion del usuario a traves de SessionRegistry y su impl
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();

    }
}

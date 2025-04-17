package com.BH_Spring.api_test;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/saludo")
@Slf4j
public class SaludoController {

    //EN LA PRACTICA ESTO NO SE HACE!!!! -> Un controlador debe de ser tread-safe 
    private String nombre;
    private String email;
    /* 
    // Método para establecer el nombre del usuario autenticado
    private void setNombre(Authentication authentication){
    
        //Asignación por defecto por si no encuentra el token
        this.nombre = "ANONIMO";
        this.email = "ANONIMO@ANONIMO.A";
        if(authentication.getPrincipal()  instanceof Jwt token){
            //Si la autenticación es un token JWT, asigna a la variable nombre la concatenación de nombre y apellido
            this.nombre = token.getClaim("given_name") + " " + token.getClaim("family_name");
            this.email = token.getClaim("email");
        }

    }
    */

    private void setNombre(Authentication authentication){
        //this.nombre = authentication.getClaim("given_name") + " " + token.getClaim("family_name");
        this.nombre = "ANONIMO";
        this.email = authentication.getName();
    }
    
    // Mapeo para la ruta /admin, saludo a rol admin_client_role
    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin_client_role')") //si el usuario posee el rol enviara el saludo
    //@PreAuthorize("hasAuthority('admin_client_role')")
    public String hola(Authentication authentication) {
        setNombre(authentication);
        log.info("Petición GET a /saludo/admin"); //Mensaje en log para corroborar envio de la solicitud
        return "¡Hola Administrador : " + nombre + " - " + email + "!";
    }

    @GetMapping("/user") //Saludo a rol user_client_rol
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')") //si el usuario posee el rol enviara el saludo
    public String holaUser(Authentication authentication) {
        setNombre(authentication);
        log.info("Petición GET a /saludo/user"); //Mensaje en log para corroborar envio de la solicitud
        return "¡Hola Usuario : " + nombre + " - " + email + "!";
    }
    /* 
    // Mapeo para la ruta /admin, saludo a rol admin_client_role
    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin_client_role')") //si el usuario posee el rol enviara el saludo
    public String hola() {
        log.info("Petición GET a /saludo/admin"); //Mensaje en log para corroborar envio de la solicitud
        return "¡Hola Administrador!";
    }

    @GetMapping("/user") //Saludo a rol user_client_rol
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')") //si el usuario posee el rol enviara el saludo
    public String holaUser() {
        log.info("Petición GET a /saludo/user"); //Mensaje en log para corroborar envio de la solicitud
        return "¡Hola Usuario!";
    }
    */
    @GetMapping("/public") //Saludo a cualquiera que envie solicitud
    @PreAuthorize("permitAll()") //permite a cualquiera que quiera verlo
    public String holaPublic() {
        log.info("Petición GET a /saludo/public"); //Mensaje en log para corroborar envio de la solicitud
        return "Hola Mundo!";
    }

     @GetMapping("/test")
    public ResponseEntity<?> testRoles(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return ResponseEntity.ok(authorities);
    }
}

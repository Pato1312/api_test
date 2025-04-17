package com.BH_Spring.api_test;

/*
 * Prueba de autentificación con roles de spring Security
 * Utiliza el puerto 9090
 * Por ahora esta configurado para realm spring-boot-realm-dev
 * Esta en el puerto 8080 en el esquema "publico" (no es public)
 * Se necesita que este funcionando keycloak para utilizar este programa
 * Si se requiere cambiar el realm, se debe cambiar en el archivo application.properties
 * en las propiedades de URI y de resource-id (nombre del cliente)
 * Tambien en el controlador habria que modificar los roles a los del nuevo cliente 
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ApiTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTestApplication.class, args);
		log.info("Aplicación iniciada");
	}
	

}

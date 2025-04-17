package com.BH_Spring.api_test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken>{
    
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principal-attribute}")
    private String principalAttribute; //email

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId; //spring-client-api-rest
    /* 
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt token){
        log.info("Convirtiendo token: {}", token.getClaims());
        Collection<GrantedAuthority> authorities = Stream.concat(
                                                                jwtGrantedAuthoritiesConverter.convert(token).stream(),
                                                                extractResourceRoles(token).stream()
                                                                ).toList();
        
        log.info("Authorities extraídas: {}", authorities);
        
        return new JwtAuthenticationToken(token, authorities, getPrincipleName(token));
    }
    */

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt token) {
        log.info("Convirtiendo token: {}", token.getClaims());
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(jwtGrantedAuthoritiesConverter.convert(token));
        authorities.addAll(extractResourceRoles(token));

        log.info("Authorities extraídas: {}", authorities);
        
        return new JwtAuthenticationToken(token, authorities, getPrincipleName(token));
    }


    private String getPrincipleName(Jwt token) {
        
        String claimName = JwtClaimNames.SUB;
        if (principalAttribute != null) {
            claimName = principalAttribute;
        }
        log.info("ClaimName: {}", claimName);
        log.info("email : " + token.getClaim(claimName));
        return token.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt token) {
        
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        log.info("Intentando extraer roles desde resource_access");
        
        // Obtener el claim resource_access
        resourceAccess = token.getClaim("resource_access");
    
        if (resourceAccess == null) {
            log.info("No se encontró 'resource_access' en el token");
            return List.of();
        }
    
        log.info("Se encontró 'resource_access': {}", resourceAccess);
    
        // Obtener el objeto del cliente específico
        resource = (Map<String, Object>) resourceAccess.get(resourceId);
    
        if (resource == null) {
            log.info("No se encontró resourceId : '{}' ", resourceId);
            return List.of();
        }
    
        log.info("Se encontró el resourceId : '{}' ", resourceId);
    
        // Obtener los roles dentro del cliente
        resourceRoles = (Collection<String>) resource.get("roles");
    
        if (resourceRoles == null) {
            log.info("No se encontraron roles");
            return List.of();
        }
    
        log.info("Se encontraron los roles: {}", resourceRoles);
    
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}

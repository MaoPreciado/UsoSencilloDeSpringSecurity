package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class CustomerController {

    @Autowired  //obj para acceder a la session del usuario
    private SessionRegistry sessionRegistry;

    @GetMapping("/index")
    public String index(){
        return "Hello world";
    }

    @GetMapping("/index2")
    public String index2(){
        return "Hello word without security";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){

        String sessionId = "";
        User userObject = null; //obj de spring security y representa un usuario;

        List<Object> allListSession = sessionRegistry.getAllPrincipals();

        for (Object object: allListSession) {
            if (object instanceof User) {
                userObject = (User) object;
            }
            //params: user y boolean
            List<SessionInformation> sessionInformation = sessionRegistry.getAllSessions(object, false);

            for (SessionInformation information: sessionInformation) {
                sessionId = information.getSessionId();
            }
        }
        Map<String,Object> response = new HashMap<>();
        response.put("response","Hello World");
        response.put("sessionId", sessionId);
        response.put("userObject", userObject );

        //responseEntity por defecto requeire un servicio o un map, este map servira para construir el Json();
        return  ResponseEntity.ok(response);
    }
}

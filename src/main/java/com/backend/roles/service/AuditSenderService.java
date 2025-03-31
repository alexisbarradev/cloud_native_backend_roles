package com.backend.roles.service;

import com.backend.roles.model.RoleAuditPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuditSenderService {

    private static final String FUNCTION_URL = "https://funcionrolaudit.azurewebsites.net/api/RolAuditNotifier";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Asegura que el ObjectMapper soporte fechas como OffsetDateTime
    @PostConstruct
    public void setupObjectMapper() {
        objectMapper.findAndRegisterModules(); // 📌 Activa módulos para fechas y zonas horarias
    }

    public void sendAudit(RoleAuditPayload payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 💡 Imprime el JSON real para verificar que no está vacío ni malformado
            String jsonPayload = objectMapper.writeValueAsString(payload);
            System.out.println("➡️ JSON que se enviará a Azure Function:");
            System.out.println(jsonPayload);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers); // Usa JSON crudo como String

            ResponseEntity<String> response = restTemplate.postForEntity(FUNCTION_URL, request, String.class);

            System.out.println("✅ Auditoría enviada: " + response.getStatusCode());
            System.out.println("📝 Respuesta de Azure: " + response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Error al enviar auditoría: " + e.getMessage());
        }
    }
}

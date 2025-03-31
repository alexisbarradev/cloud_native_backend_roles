package com.backend.roles.service;

import com.backend.roles.model.User;
import com.backend.roles.repository.UserRepository;
import com.backend.roles.model.RoleAuditPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditSenderService auditSenderService;

    public boolean updateRole(String rut, String newRole) {
        Optional<User> userOpt = userRepository.findById(rut);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String currentRole = user.getRole();

            if (!currentRole.equalsIgnoreCase(newRole)) {
                System.out.println("🔄 El rol ha cambiado de " + currentRole + " a " + newRole);

                // Crear el payload
                RoleAuditPayload payload = new RoleAuditPayload();
                payload.setRut(user.getRut());
                payload.setOldRole(currentRole);
                payload.setNewRole(newRole);
                payload.setModifiedBy("admin"); // 💡 reemplaza por el usuario autenticado si es necesario
                payload.setTimestamp(OffsetDateTime.now(ZoneId.of("America/Santiago")));

                // Enviar auditoría
                auditSenderService.sendAudit(payload);

                // Guardar nuevo rol
                user.setRole(newRole);
                userRepository.save(user);

                return true;
            } else {
                System.out.println("⚠️ El nuevo rol es igual al actual. No se realizó ningún cambio.");
                return true;
            }
        }

        System.out.println("❌ Usuario no encontrado con RUT: " + rut);
        return false;
    }

    public Optional<User> findUserByRut(String rut) {
        return userRepository.findById(rut);
    }
}


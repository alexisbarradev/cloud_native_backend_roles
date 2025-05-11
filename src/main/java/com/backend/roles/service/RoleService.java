package com.backend.roles.service;

import com.backend.roles.model.User;
import com.backend.roles.model.Role;
import com.backend.roles.model.RoleAuditPayload;
import com.backend.roles.repository.UserRepository;
import com.backend.roles.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditSenderService auditSenderService;

    public boolean updateRole(String rut, String newRoleName) {
        Optional<User> userOpt = userRepository.findById(rut);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Role currentRole = user.getRole();

            if (!currentRole.getName().equalsIgnoreCase(newRoleName)) {
                System.out.println("🔄 El rol ha cambiado de " + currentRole.getName() + " a " + newRoleName);

                Optional<Role> newRoleOpt = roleRepository.findByName(newRoleName);
                if (newRoleOpt.isEmpty()) {
                    System.out.println("❌ Rol no encontrado: " + newRoleName);
                    return false;
                }

                Role newRole = newRoleOpt.get();

                RoleAuditPayload payload = new RoleAuditPayload();
                payload.setRut(user.getRut());
                payload.setOldRole(currentRole.getName());
                payload.setNewRole(newRole.getName());
                payload.setModifiedBy("admin");
                payload.setTimestamp(OffsetDateTime.now(ZoneId.of("America/Santiago")));

                auditSenderService.sendAudit(payload);

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

    // ---------------------------
    // 🔧 CAMBIO APLICADO:
    // - Elimina un rol y remueve el rol de todos los usuarios que lo tengan
    // - A los usuarios afectados se les deja sin rol (null) o se les podría asignar un rol por defecto si prefieres
    // ---------------------------
    public boolean deleteRole(String roleNameToDelete) {
        Optional<Role> roleOpt = roleRepository.findByName(roleNameToDelete);
        if (roleOpt.isEmpty()) {
            System.out.println("❌ Rol no encontrado: " + roleNameToDelete);
            return false;
        }
    
        Role roleToDelete = roleOpt.get();
    
        // Obtener el rol INACTIVO
        Optional<Role> inactiveRoleOpt = roleRepository.findByName("INACTIVO");
        if (inactiveRoleOpt.isEmpty()) {
            System.out.println("❌ Rol INACTIVO no existe. Por favor créalo primero.");
            return false;
        }
    
        Role inactiveRole = inactiveRoleOpt.get();
    
        // Cambiar el rol de los usuarios al rol INACTIVO
        List<User> usersWithRole = userRepository.findByRole(roleToDelete);
        for (User user : usersWithRole) {
            user.setRole(inactiveRole);
        }
    
        userRepository.saveAll(usersWithRole);
    
        // Finalmente, eliminar el rol
        roleRepository.delete(roleToDelete);
    
        System.out.println("✅ Rol eliminado. Usuarios afectados ahora tienen rol INACTIVO.");
        return true;
    }
    
    
}

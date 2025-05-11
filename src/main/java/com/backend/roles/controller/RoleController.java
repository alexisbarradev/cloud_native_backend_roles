package com.backend.roles.controller;

import com.backend.roles.model.Role;
import com.backend.roles.model.User;
import com.backend.roles.model.RoleAuditPayload;
import com.backend.roles.repository.RoleRepository; // ✅ Necesario para listar los roles
import com.backend.roles.service.AuditSenderService;
import com.backend.roles.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuditSenderService auditSenderService;

    @Autowired
    private RoleRepository roleRepository; // ✅ Inyectamos el repositorio de roles

    @PutMapping("/{rut}")
    public ResponseEntity<String> updateUserRole(@PathVariable String rut, @RequestParam String newRole) {
        Optional<User> userOpt = roleService.findUserByRut(rut);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        User user = userOpt.get();
        String oldRole = user.getRole().getName(); // 🔧 Obtenemos el nombre del rol actual

        if (oldRole.equalsIgnoreCase(newRole)) {
            return ResponseEntity.ok("El nuevo rol es igual al actual. No se realizó ningún cambio.");
        }

        boolean updated = roleService.updateRole(rut, newRole); // 🔧 Lógica de cambio del rol

        if (updated) {
            RoleAuditPayload auditPayload = new RoleAuditPayload(rut, oldRole, newRole, "admin");
            auditSenderService.sendAudit(auditPayload);
            return ResponseEntity.ok("Rol actualizado correctamente.");
        } else {
            return ResponseEntity.status(500).body("Error al actualizar el rol.");
        }
    }

    // ✅ Nuevo endpoint para listar todos los roles
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    // -------------------
    // 🧨 Endpoint DELETE para eliminar un rol
    // -------------------
    @DeleteMapping("/{roleName}")
    public ResponseEntity<String> deleteRole(@PathVariable String roleName) {
        boolean deleted = roleService.deleteRole(roleName);

        if (deleted) {
            return ResponseEntity.ok("Rol eliminado y usuarios actualizados.");
        } else {
            return ResponseEntity.status(404).body("Rol no encontrado.");
        }
    }

}

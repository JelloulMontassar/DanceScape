package com.dance.mo.Monitoring;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.UserRepository;
import com.dance.mo.Services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Endpoint(id = "reclamations-by-role")
public class CustomEndpointReclamationsByRole {

    private final ReclamationService reclamationService;

    @Autowired
    public CustomEndpointReclamationsByRole(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    @ReadOperation
    public Map<Role, Long> countReclamationsByRole() {
        Map<Role, Long> reclamationCountByRole = new HashMap<>();

        // Call the service to retrieve reclamations count by role
        List<Map<Role, Long>> reclamationCounts = reclamationService.countReclamationsByRole();

        // Aggregate the counts into a single map
        for (Map<Role, Long> map : reclamationCounts) {
            for (Map.Entry<Role, Long> entry : map.entrySet()) {
                Role role = entry.getKey();
                Long count = entry.getValue();

                // Sum up counts for the same role
                reclamationCountByRole.put(role, reclamationCountByRole.getOrDefault(role, 0L) + count);
            }
        }

        return reclamationCountByRole;
    }
}

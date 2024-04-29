package com.dance.mo.Monitoring;

import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.ReclamationRepository;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Endpoint(id = "custom-reclamations-per-user") // Change the ID to a new name
public class CustomEndpointReclamationsPerUser {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomEndpointReclamationsPerUser(ReclamationRepository reclamationRepository, UserRepository userRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
    }

    @ReadOperation
    public Map<String, Map<String, Integer>> countReclamationsPerUser() {
        List<Object[]> reclamationsPerUser = reclamationRepository.countReclamationsPerUser();

        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Object[] row : reclamationsPerUser) {
            Long userId = (Long) row[0];
            LocalDate reclamationDate = (LocalDate) row[1];
            Integer count = ((Number) row[2]).intValue();

            // Fetch user details by userId from UserRepository
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String fullName = user.getFirstName() + " " + user.getLastName();

                if (!result.containsKey(fullName)) {
                    result.put(fullName, new HashMap<>());
                }

                result.get(fullName).put(reclamationDate.toString(), count);
            }
        }

        return result;
    }
}

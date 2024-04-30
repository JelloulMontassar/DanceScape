package com.dance.mo.Monitoring;

import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.ReclamationRepository;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

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

                // Check if the result map already contains an entry for the user
                if (result.containsKey(fullName)) {
                    // If the user already exists in the map, check if the date exists
                    Map<String, Integer> existingEntry = result.get(fullName);
                    if (existingEntry.containsKey(reclamationDate.toString())) {
                        // If the date exists, update the count by adding it to the existing count
                        int existingCount = existingEntry.get(reclamationDate.toString());
                        existingEntry.put(reclamationDate.toString(), existingCount + count);
                    } else {
                        // If the date doesn't exist, add it with the count
                        existingEntry.put(reclamationDate.toString(), count);
                    }
                } else {
                    // If the user doesn't exist in the map, add a new entry with the date and count
                    Map<String, Integer> reclamationMap = new HashMap<>();
                    reclamationMap.put(reclamationDate.toString(), count);
                    result.put(fullName, reclamationMap);
                }
            }
        }

        // Merge entries with the same count for each day
        result = mergeEntriesWithSameCount(result);

        return result;
    }

    private Map<String, Map<String, Integer>> mergeEntriesWithSameCount(Map<String, Map<String, Integer>> result) {
        Map<Map<String, Integer>, List<String>> reversedMap = new HashMap<>();
        Map<String, Map<String, Integer>> mergedResult = new HashMap<>();

        // Reverse the map to group by value (reclamation map) and store the corresponding keys (full names)
        for (Map.Entry<String, Map<String, Integer>> entry : result.entrySet()) {
            Map<String, Integer> reclamationMap = entry.getValue();
            String fullName = entry.getKey();

            if (!reversedMap.containsKey(reclamationMap)) {
                reversedMap.put(reclamationMap, new ArrayList<>());
            }
            reversedMap.get(reclamationMap).add(fullName);
        }

        // Iterate over the reversed map to create merged entries
        for (Map.Entry<Map<String, Integer>, List<String>> entry : reversedMap.entrySet()) {
            Map<String, Integer> reclamationMap = entry.getKey();
            List<String> fullNames = entry.getValue();

            // If there is only one full name associated with the reclamation map, add it directly to the merged result
            if (fullNames.size() == 1) {
                String fullName = fullNames.get(0);
                mergedResult.put(fullName, reclamationMap);
            } else {
                // If there are multiple full names associated with the same reclamation map, merge them with a slash
                String mergedFullName = String.join(" / ", fullNames);
                mergedResult.put(mergedFullName, reclamationMap);
            }
        }

        return mergedResult;
    }


}

package com.dance.mo.Services;

import com.dance.mo.Entities.Enumarations.RelationshipStatus;
import com.dance.mo.Entities.Relationship;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.RelationshipRepository;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Relationship> getPendingFriendRequests(User user) {
        return relationshipRepository.findByReceiverAndStatus(user, RelationshipStatus.PENDING);
    }
    public void sendFriendRequest(User sender, User receiver) {
        Relationship existingRequest = relationshipRepository.findBySenderAndReceiverAndStatus(
                sender, receiver, RelationshipStatus.PENDING);

        if (existingRequest == null) {
            existingRequest = relationshipRepository.findBySenderAndReceiverAndStatus(
                    receiver, sender, RelationshipStatus.PENDING);
        }

        if (existingRequest != null) {
            throw new IllegalStateException("Friend request already sent");
        }
        Relationship relationship = new Relationship();
        relationship.setSender(sender);
        relationship.setReceiver(receiver);
        relationship.setStatus(RelationshipStatus.PENDING);
        relationshipRepository.save(relationship);
    }

    public void acceptFriendRequest(User sender, User receiver) {
        Relationship relationship = relationshipRepository.findBySenderAndReceiver(sender, receiver);
        if (relationship != null && relationship.getStatus() == RelationshipStatus.PENDING) {
            relationship.setStatus(RelationshipStatus.ACCEPTED);
            relationshipRepository.save(relationship);

            addFriend(receiver, sender);
            addFriend(sender, receiver);
        }
    }

    private void addFriend(User currentUser, User friendToAdd) {
        if (!currentUser.getFriends().contains(friendToAdd)) {
            currentUser.getFriends().add(friendToAdd);
            userRepository.save(currentUser);
        }
        if (!friendToAdd.getFriends().contains(currentUser)) {
            friendToAdd.getFriends().add(currentUser);
            userRepository.save(friendToAdd);
        }
    }

    public void blockUser(User currentUser, User userToBlock) {
        Relationship relationship = relationshipRepository.findBySenderAndReceiver(currentUser, userToBlock);
        if (relationship == null) {
            relationship = new Relationship();
            relationship.setSender(currentUser);
            relationship.setReceiver(userToBlock);
        }
        relationship.setStatus(RelationshipStatus.BLOCKED);
        relationshipRepository.save(relationship);
    }

    public void unblockUser(User currentUser, User userToUnblock) {
        Relationship relationship = relationshipRepository.findBySenderAndReceiver(currentUser, userToUnblock);
        if (relationship != null && relationship.getStatus() == RelationshipStatus.BLOCKED) {
            relationshipRepository.delete(relationship);
        }
    }

    public boolean canSendMessage(User sender, User receiver) {
        Relationship relationship1 = relationshipRepository.findBySenderAndReceiver(sender, receiver);
        boolean isAcceptedFromSenderToReceiver = relationship1 != null && relationship1.getStatus() == RelationshipStatus.ACCEPTED;

        Relationship relationship2 = relationshipRepository.findBySenderAndReceiver(receiver, sender);
        boolean isAcceptedFromReceiverToSender = relationship2 != null && relationship2.getStatus() == RelationshipStatus.ACCEPTED;

        return isAcceptedFromSenderToReceiver || isAcceptedFromReceiverToSender;
    }

}

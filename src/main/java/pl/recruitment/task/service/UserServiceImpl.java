package pl.recruitment.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.recruitment.task.exception.Message;
import pl.recruitment.task.exception.ObjectNotFoundException;
import pl.recruitment.task.model.User;
import pl.recruitment.task.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RequestHandler requestHandler;
    private final ObjectMapper jsonMapper;

    @Override
    public User getUser(String login) {
        if (login == null || login.isEmpty()) {
            log.error("Invalid url address. " + Message.INVALID_ARGUMENT);
            throw new IllegalArgumentException(Message.INVALID_ARGUMENT);
        }

        String response = requestHandler.sendRequest(login).orElseThrow(() -> new IllegalArgumentException(Message.BAD_REQUEST));

        User receivedUser = serialize(response).orElseThrow(() -> new ObjectNotFoundException(Message.INVALID_USER));
        log.info("Received user: {}", receivedUser);

        receivedUser.setCalculations(calculate(receivedUser));
        log.info("Set calculations value for user: {} to " + calculate(receivedUser), receivedUser);

        Optional<User> persistedUser = userRepository.findById(login);
        if (persistedUser.isPresent()) {
            persistedUser.get().setRequestCount(persistedUser.get().getRequestCount() + 1);
            userRepository.save(persistedUser.get());
            log.info("Updated user: {}.Set request count to: " + persistedUser.get().getRequestCount(), persistedUser.get());
        } else {
            userRepository.save(receivedUser);
            log.info("Saved new user with login :" + login + " in DB");
        }
        return receivedUser;
    }

    private Double calculate(User user) {
        return 6.0 / user.getFollowers() * (2 + user.getPublicRepos());
    }

    private Optional<User> serialize(String json) {
        try {
            return Optional.of(jsonMapper.readValue(json, User.class));
        } catch (JsonProcessingException e) {
            log.error(Message.INVALID_USER, e);
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

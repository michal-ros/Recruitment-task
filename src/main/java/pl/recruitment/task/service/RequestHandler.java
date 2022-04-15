package pl.recruitment.task.service;

import java.util.Optional;

public interface RequestHandler {

    Optional<String> sendRequest(String login);
}

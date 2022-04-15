package pl.recruitment.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.recruitment.task.model.User;
import pl.recruitment.task.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{login}")
    public ResponseEntity<User> getUser(@PathVariable String login) {
        log.info("Method getUser called");
        User user = userService.getUser(login);
        log.info("Method getUser finished");
        return ResponseEntity.ok(user);
    }
}

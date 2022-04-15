package pl.recruitment.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.recruitment.task.exception.Message;
import pl.recruitment.task.exception.ObjectNotFoundException;
import pl.recruitment.task.model.User;
import pl.recruitment.task.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    UserRepository testUserRepository;

    @Mock
    RequestHandler testRequestHandler;

    @Mock
    ObjectMapper testObjectMapper;

    @InjectMocks
    UserServiceImpl testUserService;

    private final String testResponseBody = "test response body";
    private final String testLogin = "test";

    @Test
    @SneakyThrows
    void shouldReturnCreatedUser() {
        //given
        User testUser = new User();
        testUser.setLogin(testLogin);
        testUser.setFollowers(1);
        testUser.setPublicRepos(1);

        //when
        when(testRequestHandler.sendRequest(testLogin)).thenReturn(Optional.of(testResponseBody));
        when(testObjectMapper.readValue(testResponseBody, User.class)).thenReturn(testUser);
        User testReceived = testUserService.getUser(testLogin);

        //then
        assertEquals(testReceived.getLogin(), testLogin);
        assertEquals(testReceived.getCalculations(), 18);
        verify(testUserRepository, times(1)).findById(testLogin);
    }

    @SneakyThrows
    @Test
    void shouldReturnPersistedUser() {
        //given
        User testUser = new User();
        testUser.setLogin(testLogin);
        testUser.setFollowers(1);
        testUser.setPublicRepos(1);
        testUser.setRequestCount(1);

        //when
        when(testRequestHandler.sendRequest(testLogin)).thenReturn(Optional.of(testResponseBody));
        when(testObjectMapper.readValue(testResponseBody, User.class)).thenReturn(testUser);
        when(testUserRepository.findById(testLogin)).thenReturn(Optional.of(testUser));
        User testReceived = testUserService.getUser(testLogin);

        //then
        assertEquals(testReceived.getLogin(), testLogin);
        assertEquals(testReceived.getRequestCount(), 2);
        verify(testUserRepository, times(1)).findById(testLogin);
        verify(testUserRepository, times(1)).save(testUser);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenLoginIsIncorrect() {
        assertThrows(IllegalArgumentException.class, () -> testUserService.getUser(null), Message.INVALID_ARGUMENT);
        assertThrows(IllegalArgumentException.class, () -> testUserService.getUser(""), Message.INVALID_ARGUMENT);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenServerError() {
        //when
        when(testRequestHandler.sendRequest(testLogin)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> testUserService.getUser(testLogin), Message.BAD_REQUEST);
    }

    @Test
    @SneakyThrows
    void shouldThrowObjectNotFoundExceptionWhenNotSerializeJson() {
        //when
        when(testRequestHandler.sendRequest(testLogin)).thenReturn(Optional.of(testResponseBody));
        when(testObjectMapper.readValue(testResponseBody, User.class)).thenThrow(JsonProcessingException.class);

        // then
        assertThrows(ObjectNotFoundException.class, () -> testUserService.getUser(testLogin), Message.INVALID_USER);
    }
}
package pl.recruitment.task.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.recruitment.task.exception.Message;
import pl.recruitment.task.exception.ObjectNotFoundException;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class RequestHandlerImpl implements RequestHandler {

    @Value("${source.url}")
    private String url;

    @Value("${source.request.method}")
    private String requestMethod;

    @Override
    public Optional<String> sendRequest(String login) {

        url = url.charAt(url.length() - 1) == '/' ? url.substring(0, url.length() - 1) : url;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url + "/" + login)
                .method(requestMethod, null)
                .build();

        try (Response response = client.newCall(request).execute()) {
            log.info("Request sent to: " + url);
            if (response.code() == HttpStatus.NOT_FOUND.value()) {
                log.error("Object not found: " + url);
                throw new ObjectNotFoundException(Message.NOT_FOUND);
            }
            return Optional.of(response.body().string());

        } catch (IOException e) {
            log.error("Internal server error: " + url, e);
            e.printStackTrace();
        }

        return Optional.empty();
    }
}

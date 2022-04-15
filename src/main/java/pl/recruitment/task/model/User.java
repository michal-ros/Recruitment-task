package pl.recruitment.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Transient
    private Long id;

    @Id
    private String login;

    @Transient
    private String name;

    @Transient
    private String type;

    @Transient
    @JsonProperty("avatar_url")
    private String avatarUrl;

    @Transient
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer followers;

    @Transient
    @JsonProperty(value = "public_repos", access = JsonProperty.Access.WRITE_ONLY)
    private Integer publicRepos;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double calculations;

    @Min(0)
    @JsonIgnore
    private Integer requestCount;

    @PrePersist
    private void setFirstRequestCount() {
        setRequestCount(1);
    }

}

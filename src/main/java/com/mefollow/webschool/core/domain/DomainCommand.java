package com.mefollow.webschool.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mefollow.webschool.management.user.domain.account.User;

import java.util.Date;
import java.util.function.Function;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DomainCommand {

    @JsonIgnore
    private User user;
    @JsonIgnore
    public final Date issueDate;
    @JsonIgnore
    public final String correlationId;

    public DomainCommand() {
        issueDate = new Date();
        correlationId = randomUUID().toString();
    }

    public static <D extends DomainCommand> Function<User, D> injectUser(D command) {
        return user -> {
            command.injectUser(user);
            return command;
        };
    }

    public void injectUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUserId() {
        return ofNullable(user).map(BaseModel::getId).orElseThrow();
    }

    @Override
    public String toString() {
        return "DomainCommand{" +
                "user=" + user +
                ", issueDate=" + issueDate +
                ", correlationId=" + correlationId +
                '}';
    }
}

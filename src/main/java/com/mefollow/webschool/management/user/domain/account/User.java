package com.mefollow.webschool.management.user.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mefollow.webschool.management.user.domain.account.UserAccountStatus.AWAITING_CONFIRMATION;
import static com.mefollow.webschool.management.user.domain.account.UserAccountStatus.CONFIRMED;
import static com.mefollow.webschool.management.user.util.ColorUtil.randomColor;
import static java.time.LocalDateTime.now;

@Document(collection = "users")
public class User extends BaseModel {
    private static final Language DEFAULT_LANG = Language.RUSSIAN;

    @Indexed
    @JsonProperty("email")
    private String email;
    private String name;
    private String color;
    private Gender gender;
    private String city;
    private String country;
    private Language language;
    private LocalDate birthday;
    private UserAccountStatus status;
    private LocalDateTime createdAt;

    @JsonIgnore
    private boolean defaultPassword;
    private boolean banned;
    private boolean globalAdmin;

    public User(String email) {
        this.email = email;
        this.color = randomColor();
        this.language = DEFAULT_LANG;
        this.status = AWAITING_CONFIRMATION;
        this.createdAt = now();
    }

    public static User createUser(String email, String name, Language language) {
        final var user = createUser(email);
        user.setName(name);
        if (language != null) user.setLanguage(language);
        return user;
    }

    public static User createUser(String email) {
        return new User(email);
    }

    public static User createGlobalAdmin(String email) {
        final var globalAdmin = new User(email);
        globalAdmin.setStatus(CONFIRMED);
        globalAdmin.setGlobalAdmin(true);
        return globalAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public UserAccountStatus getStatus() {
        return status;
    }

    public void setStatus(UserAccountStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(boolean defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public void setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", gender=" + gender +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", birthday='" + birthday + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", defaultPassword=" + defaultPassword +
                ", banned=" + banned +
                ", globalAdmin=" + globalAdmin +
                '}';
    }
}

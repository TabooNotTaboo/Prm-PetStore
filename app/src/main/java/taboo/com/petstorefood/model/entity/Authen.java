package taboo.com.petstorefood.model.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Authen {
    private UUID userId;
    private String password;
    private String email;
    private String userName;
    private String gender;
    private String address;
    private LocalDate dateOfBirth;
    private String fullName;
    private String phoneNumber;

    public Authen(UUID userId, String password, String email, String userName, String gender, String address, LocalDate dateOfBirth, String fullName, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.gender = gender;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

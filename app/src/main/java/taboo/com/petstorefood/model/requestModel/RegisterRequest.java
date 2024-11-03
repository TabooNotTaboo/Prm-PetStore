package taboo.com.petstorefood.model.requestModel;

import java.time.LocalDate;

public class RegisterRequest {
    private String password;
    private String email;
    private String userName;
    private String gender;
    private String address;
    private String dateOfBirth;
    private String fullName;
    private String phoneNumber;

    public RegisterRequest(String password, String email, String userName, String gender, String address, String dateOfBirth, String fullName, String phoneNumber) {
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.gender = gender;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}

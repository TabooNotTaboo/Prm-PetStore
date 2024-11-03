package taboo.com.petstorefood.model.requestModel;

public class UpdateAccountRequest {
    private String id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String address;
    private String gender;
    private String dateOfBirth;

    public UpdateAccountRequest(String id, String userName, String email, String phoneNumber, String fullName, String address, String gender, String dateOfBirth) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}

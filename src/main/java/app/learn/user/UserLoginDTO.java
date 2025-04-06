package app.learn.user;

public class UserLoginDTO {
    private String email;
    private String password;
    private UserRole userRole;
    private Long id;

    public UserLoginDTO() {

    }

    public UserLoginDTO(String email, String password, UserRole userRole, Long id) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

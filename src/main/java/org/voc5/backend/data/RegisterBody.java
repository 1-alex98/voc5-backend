package org.voc5.backend.data;

public class RegisterBody {
    private final String email;
    private final String password;

    public RegisterBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

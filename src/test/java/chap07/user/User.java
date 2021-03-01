package chap07.user;

public class User {

    private String id;
    private String pw;
    private String email;

    public User(String id, String pw1, String email) {

        this.id= id;
        this.pw = pw1;
        this.email= email;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getEmail() {
        return email;
    }
}

package scs.ubb.mpp.entity;

public class User extends Entity<Integer> {
    private String userName;
    private String password;
    private String email;
    private String address;

    public User() {
        super(-1);
    }

    public User(String user, String password) {
        super(-1);
        this.userName = user;
        this.password = password;
    }

    public User(Integer userId, String userName, String password, String email, String address) {
        super(userId);
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
    }
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return userName;
    }
}

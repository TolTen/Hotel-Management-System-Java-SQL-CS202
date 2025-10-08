package HotelManagementSystem;

public abstract class User {
    private String userName;
    private String email;
    private String password;
    private int userID;

    public User(String userName, String email, String password, int userID) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getUserID() {
        return userID;
    }


    public abstract void displayMenu();
}

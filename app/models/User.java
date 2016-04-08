package models;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.EnumValue;
import controllers.Application;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.*;

/**
 * This model class is the model class for User table in the database
 */
@Entity
public class User extends Model {

    @Id
    @Constraints.Min(10)
    public Long id;

    @Constraints.Required(message = "Email cannot be empty")
    @Constraints.Email
    @Column(unique=true)
    public String email;

    @Constraints.Required(message = "Password cannot be empty")
    public String password;

    public static Finder<Long, User> find = new Finder<Long,User>(User.class);

    public static User authenticate(String username, String password) {
        User user = User.find.where().eq("email", username).findUnique();
        if (user != null && BCrypt.checkpw(password, user.password)) {
            return user;

        } else {
            return null;
        }
    }


    @Constraints.Required(message = "No user type defined")
    @Enumerated(EnumType.STRING)
    public UserType userType;

    public enum UserType {

        @EnumValue("B")
        BLOGGER,

        @EnumValue("R")
        READER,

    }


    public void setPassword(String password){
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public static User getUserFromForm(Application.Signup signupForm){
        User user = new User();
        user.email = signupForm.email;
        user.password = BCrypt.hashpw(signupForm.password, BCrypt.gensalt());
        user.userType = signupForm.userType;
        return user;
    }
}
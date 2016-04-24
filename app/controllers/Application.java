package controllers;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.io.IOException;
import views.html.*;

/**
 * This controller contains all the actions to handle HTTP requests
 * to access the User table of the database
 */
public class Application extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result index() throws IOException {
        return ok("");
    }

    public Result createUser(){
        Form<Signup> signupForm = formFactory.form(Signup.class).bindFromRequest();
        if (signupForm.hasErrors()) {
            return badRequest(signup.render(signupForm));
        } else {
            User.db().insert(User.getUserFromForm(signupForm.get()));
            session().clear();
            session("email", signupForm.get().email);
            return redirect("/dashboard");
        }
    }

    public Result listUsers() throws IOException {
        return ok(User.find.findList().toString());
    }


    public Result login() throws IOException {
        return ok(login.render(formFactory.form(Login.class)));
    }

    public Result logout(){
        session().clear();
        return redirect("/login");
    }


    public Result signup() throws IOException {
        return ok(signup.render(formFactory.form(Signup.class)));
    }

    public Result authenticate(){
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect("/dashboard");
        }
    }
    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }

    }

    public static class Signup {

        public String email;
        public String password;
        public User.UserType userType;

        public String validate() {
            if (User.find.where().eq("email", email).findUnique() != null) {
                return "User already exists.";
            }
            return null;
        }

    }
}


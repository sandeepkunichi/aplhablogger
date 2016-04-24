package controllers;

import com.google.api.services.drive.Drive;
import models.Blog;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.DriveService;
import views.html.dashboard;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sandeep.K on 4/5/2016.
 */
public class DashboardController extends Controller {
    @Inject
    private FormFactory formFactory;
    public Result index() throws IOException {
        String userName = session().getOrDefault("email", null);
        if(userName == null){
            return redirect("/login");
        }
        List<Blog> blogList = Blog.find.where().eq("author", User.find.where().eq("email", session().get("email")).findUnique()).findList();
        return ok(dashboard.render(formFactory.form(BlogForm.class), blogList));
    }

    public Result uploadPicture() throws IOException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart document = body.getFile("picture");
        Drive service = DriveService.getDriveService();
        java.io.File file = (java.io.File)document.getFile();
        DriveService.insertFile(service, file.getName(), "Testing API", null, "image/jpeg",file);
        return ok("Done");
    }

    public Result createBlog() throws IOException {
        Form<BlogForm> blogForm = formFactory.form(BlogForm.class).bindFromRequest();
        List<Blog> blogList = Blog.find.where().eq("author", User.find.where().eq("email", session().get("email")).findUnique()).findList();
        if (blogForm.hasErrors()) {
            return badRequest(dashboard.render(blogForm, blogList));
        } else {
            blogForm.get().author = User.find.where().eq("email", session().get("email")).findUnique();
            Blog.db().insert(Blog.getUserFromForm(blogForm.get()));
            return redirect("/dashboard");
        }
    }

    public Result deleteBlog(String blogId){
        Blog.find.byId(Long.valueOf(blogId)).delete();
        return redirect("/dashboard");
    }

    public static class BlogForm {

        public String title;
        public String content;
        public User author;

        public String validate() {
            return null;
        }

    }

}

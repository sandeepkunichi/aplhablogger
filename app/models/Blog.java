package models;

import com.avaje.ebean.Model;
import controllers.DashboardController;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Sandeep.K on 4/9/2016.
 */
@Entity
public class Blog extends Model {

    @Id
    @Constraints.Min(10)
    public Long id;

    @Constraints.Required(message = "Title cannot be empty")
    public String title;

    public static Finder<Long, Blog> find = new Finder<Long, Blog>(Blog.class);

    @ManyToOne
    public User author;

    @Constraints.Required(message = "Content cannot be empty")
    public String content;

    public static Blog getUserFromForm(DashboardController.BlogForm blogForm){
        Blog blog = new Blog();
        blog.title = blogForm.title;
        blog.author = blogForm.author;
        blog.content = blogForm.content;
        return blog;
    }
}

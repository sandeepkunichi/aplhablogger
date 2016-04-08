package controllers;

import com.google.api.services.drive.Drive;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.DriveService;

import java.io.IOException;

/**
 * Created by Sandeep.K on 4/5/2016.
 */
public class DashboardController extends Controller {
    public Result index() throws IOException {
        return ok(views.html.dashboard.render());
    }

    public Result uploadPicture() throws IOException {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart document = body.getFile("picture");
        Drive service = DriveService.getDriveService();
        java.io.File file = (java.io.File)document.getFile();
        DriveService.insertFile(service, file.getName(), "Testing API", "0B3m2RgPn-XewVVhHTldTeWJYMlk", "image/jpeg",file);
        return ok("Done");
    }

}

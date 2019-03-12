package controllers;

import business.gmail.GmailRepository;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class GmailController extends Controller {

    private GmailRepository gmailRepository;

    @Inject
    public GmailController(GmailRepository gmailRepository) {
        this.gmailRepository = gmailRepository;
    }

    public Result create() {

        return TODO;
    }

    public Result getAll() {

        return TODO;
    }

    public Result get() {

        return TODO;
    }

    public Result update() {

        return TODO;
    }

    public Result delete() {

        return TODO;
    }
}

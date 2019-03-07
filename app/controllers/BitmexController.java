package controllers;

import business.bitmex.BitmexHelper;
import play.mvc.Result;

import javax.inject.Inject;

import static play.mvc.Results.TODO;

public class BitmexController {

    private final BitmexHelper bitmexHelper;

    @Inject
    public BitmexController(BitmexHelper bitmexHelper) {
        this.bitmexHelper = bitmexHelper;
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

package org.voc5.backend;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import org.voc5.backend.data.RegisterBody;
import org.voc5.backend.data.Vocabulary;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.jade.JadeTemplateEngine;

import java.util.HashMap;

import static spark.Spark.*;

public class Main {

    private static Gson gson = new Gson();

    public static void main(String[] args) {
        JDBCWrapper.getInstance().connect();
        get("/login", Main::login);
        get("/", (rq, rs) -> new ModelAndView(new HashMap<>(), "docs"), new JadeTemplateEngine());
        post("/register", Main::register);

        before("/voc*", Main::checkForLogin);
        get("/voc", Main::getAllVocabulary, model -> gson.toJson(model));
        get("/voc/random", Main::getRandomVocabulary, model -> gson.toJson(model));
        patch("/voc/:id", Main::patchVocabularyWithId);
        post("/voc", Main::createVocabulary);
        delete("/voc/:id", Main::deleteVocabularyWithId);
    }

    private static Object createVocabulary(Request request, Response response) {
        int userId = checkForLogin(request);
        Vocabulary vocabulary = gson.fromJson(request.body(), Vocabulary.class);
        return JDBCWrapper.getInstance().createVoc(userId, vocabulary);
    }

    private static Object patchVocabularyWithId(Request request, Response response) {
        int userId = checkForLogin(request);
        Vocabulary vocabulary = gson.fromJson(request.body(), Vocabulary.class);
        JDBCWrapper.getInstance().patchVocabulary(userId, vocabulary);
        return "patched";
    }

    private static Object login(Request request, Response response) {
        if (checkForLogin(request) != -1) {
            return "Logged in";
        }
        return "Log in failed";
    }

    private static void checkForLogin(Request request, Response response) {
        checkForLogin(request);
    }

    private static Object register(Request request, Response response) {
        String body = request.body();
        RegisterBody registerBody = gson.fromJson(body, RegisterBody.class);
        String passwordHash = BCrypt.withDefaults().hashToString(12, registerBody.getPassword().toCharArray());
        JDBCWrapper.getInstance().register(registerBody.getEmail(), passwordHash);
        return "Registered";
    }

    private static int checkForLogin(Request request) {
        String email = request.headers("email");
        String password = request.headers("password");

        JDBCWrapper.IdAndPassword passwordHashFromUserAndId = JDBCWrapper.getInstance().getPasswordHashFromUserAndId(email);
        if (passwordHashFromUserAndId == null) {
            halt(401, "Login failed, check your email and password");
            return -1;
        }
        //Vulnerable via timing attack
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), passwordHashFromUserAndId.getPasswordHash());
        if (result.verified) {
            return passwordHashFromUserAndId.getId();
        } else {
            halt(401, "Login failed, check your email and password");
            return -1;
        }
    }

    private static Object deleteVocabularyWithId(Request req, Response res) {
        int userId = checkForLogin(req);
        int vocId;
        try {
            vocId = Integer.parseInt(req.params(":id"));
        } catch (Exception e) {
            e.printStackTrace();
            halt(400, "Bad parameter id, must be an int");
            return null;
        }
        return JDBCWrapper.getInstance().deleteVocabulary(userId, vocId);
    }

    private static Object getRandomVocabulary(Request req, Response res) {
        int userId = checkForLogin(req);
        return JDBCWrapper.getInstance().getRandomVoc(userId);
    }

    private static Object getAllVocabulary(Request req, Response res) {
        int userId = checkForLogin(req);
        return JDBCWrapper.getInstance().getAllVocabulary(userId);
    }
}

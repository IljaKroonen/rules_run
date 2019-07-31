package example;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin.create()
                .get("/", ctx -> {
                    ctx.result("I'm the main, my dependency is " + System.getenv("SERVICE_DEP_URI"));
                })
                .start(Integer.parseInt(System.getenv("PORT")));
    }
}

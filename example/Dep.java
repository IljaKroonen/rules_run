package example;

import io.javalin.Javalin;

public class Dep {
    public static void main(String[] args) {
        Javalin.create()
                .get("/", ctx -> {
                    ctx.result("I'm the dependency");
                })
                .start(Integer.parseInt(System.getenv("PORT")));
    }
}

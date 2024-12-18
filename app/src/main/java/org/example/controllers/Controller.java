package org.example.controllers;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.http.Context;

public class Controller {
  public static void helloWorld(Context ctx) {
    var dotenv = Dotenv.load();
    ctx.json(new Message("hello", dotenv.get("MONGO_URI")));
  }

  static class Message {
    String message;
    String content;

    public Message(String message, String content) {
      this.message = message;
      this.content = content;
    }

    public String getMessage() {
      return message;
    }

    public String getContent() {
      return content;
    }
  }
}

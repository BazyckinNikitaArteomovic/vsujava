package ru.vsu.cs.bazykin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), obj);
    }

    public static void sendError(HttpServletResponse resp, int code, String message) throws IOException {
        resp.setStatus(code);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), new ErrorResponse(message));
    }

    public static class ErrorResponse {
        public final String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}
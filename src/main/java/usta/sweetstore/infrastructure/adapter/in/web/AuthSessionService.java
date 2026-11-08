package usta.sweetstore.infrastructure.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class AuthSessionService {

    private final Map<String, String> sessions =
            new ConcurrentHashMap<>();

    public String createSession(String username) {

        String token = UUID.randomUUID().toString();

        sessions.put(token, username);

        return token;
    }

    public boolean isValid(String token) {

        return token != null
                && sessions.containsKey(token);
    }

    public void closeSession(String token) {

        if (token != null) {
            sessions.remove(token);
        }
    }
}
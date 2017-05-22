package edu.hm.shareit.services;

import edu.hm.shareit.models.Token;
import edu.hm.shareit.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private Map<String, User> users;
    private static Map<String, User> tokens;

    public UserServiceImpl() {
        users = new HashMap<>();
        users.put("testuser", new User("testuser", "Test123"));
        if (tokens == null) {
            tokens = new HashMap<>();
        }
    }

    public Map getUsers() {
        return users;
    }

    @Override
    public ServiceResult checkUser(User user) {
        if (users.containsKey(user.getUsername())) {
            if (user.getPassword().equals(users.get(user.getUsername()).getPassword())) {
                return ServiceResult.OK;
            }
        }
        return ServiceResult.UNAUTHORIZED;
    }

    @Override
    public User checkToken(String token) {
        return tokens.getOrDefault(token, null);
    }

    @Override
    public Token getNewToken(User user) {
        Token token = new Token();
        tokens.put(token.getToken(), users.get(user.getUsername()));
        return token;
    }

    void flushAllData() {
        tokens = new HashMap<>();
    }

}

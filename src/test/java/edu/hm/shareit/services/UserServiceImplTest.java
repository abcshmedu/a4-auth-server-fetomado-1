package edu.hm.shareit.services;

import edu.hm.shareit.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class UserServiceImplTest {

    private UserServiceImpl service;
    private Map<String, User> testUsers;

    @Before
    public void before(){
        service = new UserServiceImpl();
        testUsers = service.getUsers();
    }

    @Test
    public void testCheckValidUser(){
        ServiceResult sr = service.checkUser(testUsers.get("testuser"));
        assertEquals(ServiceResult.OK, sr);
    }

    @Test
    public void testCheckInvalidUser(){
        User invalidUser = new User("invalidUser", "qwertz");
        ServiceResult sr = service.checkUser(invalidUser);
        assertEquals(ServiceResult.UNAUTHORIZED, sr);
    }

}
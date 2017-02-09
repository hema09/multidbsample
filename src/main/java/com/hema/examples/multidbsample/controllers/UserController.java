package com.hema.examples.multidbsample.controllers;

import com.hema.examples.multidbsample.models.User;
import com.hema.examples.multidbsample.models.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by hemabhatia on 2/7/17.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDao userDao;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@RequestHeader("TenantID") String tenant, String email, String name) {

        TenantContext.setCurrentTenant(tenant);
        User user = null;

        try {
            user = new User(email, name);
            user = userDao.save(user);
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }

        return "User succesfully created! (id = " + user.getId() + ")";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(long id) {
        try {
            User user = new User(id);
            userDao.delete(user);
        }
        catch (Exception ex) {
            return "Error deleting the user: " + ex.toString();
        }
        return "User succesfully deleted!";
    }


    @RequestMapping(value = "/get-by-email", method = RequestMethod.GET)
    @ResponseBody
    public User getByEmail(@RequestHeader("TenantID") String tenant, String email) {
        TenantContext.setCurrentTenant(tenant);

        String userId;
        try {
            User user = userDao.findByEmail(email);
            return user;
        }
        catch (Exception ex) {
            return null;
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAll(@RequestHeader("TenantID") String tenant) {
        TenantContext.setCurrentTenant(tenant);

        try {
            List<User> users = userDao.findAll();
            return users;
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * /update  --> Update the email and the name for the user in the database
     * having the passed id.
     *
     * @param id The id for the user to update.
     * @param email The new email.
     * @param name The new name.
     * @return A string describing if the user is succesfully updated or not.
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public User updateUser(long id, String email, String name) {
        try {
            User user = userDao.findOne(id);
            user.setEmail(email);
            user.setName(name);
            user = userDao.save(user);
            return user;
        }
        catch (Exception ex) {
            return null;
        }
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void sampleRun() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);

        Set<Callable<Object>> tasks = new HashSet<>();

        for(int i = 0; i < 100; i++ ) {
            final int t = i;
            tasks.add(new Callable() {
                @Override
                public Object call() throws Exception {
                    int tenantNumber = (1 + (t % 2));
                    long threadId = Thread.currentThread().getId();
                    String tenant = "tenant" + tenantNumber;
                    System.out.println("threadId = " + threadId + " before set : " + TenantContext.getCurrentTenant());
                    TenantContext.setCurrentTenant(tenant);
                    System.out.println("threadId = " + threadId + " after set : " + TenantContext.getCurrentTenant());
                    String name = "user" + t + "tenant" + tenantNumber;
                    User user = new User(name + "@email.com", name);
                    return userDao.save(user);
                }
            });
        }

        service.invokeAll(tasks);

    }

} // class UserController

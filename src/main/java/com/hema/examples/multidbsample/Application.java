package com.hema.examples.multidbsample;

import com.hema.examples.multidbsample.controllers.TenantContext;
import com.hema.examples.multidbsample.models.User;
import com.hema.examples.multidbsample.models.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by hemabhatia on 2/7/17.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

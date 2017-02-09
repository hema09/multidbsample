package com.hema.examples.multidbsample.controllers;

/**
 * Created by hemabhatia on 2/8/17.
 */
public class TenantContext {

    private static ThreadLocal<Object> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(Object tenant) {
        currentTenant.set(tenant);
    }
    public static Object getCurrentTenant() {
        return currentTenant.get();
    }
}


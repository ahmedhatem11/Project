package com.sumerge.program.rest.config;

import com.sumerge.program.rest.AuditLogResource;
import com.sumerge.program.rest.GroupResource;
import com.sumerge.program.rest.UserResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class MyApplication extends javax.ws.rs.core.Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(UserResource.class);
        classes.add(GroupResource.class);
        classes.add(AuditLogResource.class);
        return classes;
    }
}
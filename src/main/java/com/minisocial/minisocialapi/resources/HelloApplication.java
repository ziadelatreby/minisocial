package com.minisocial.minisocialapi.resources;

import com.minisocial.minisocialapi.common.JwtAuthFilter;
import com.minisocial.minisocialapi.resources.*;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Register resources
        classes.add(UserResource.class);
        classes.add(ConnectionResource.class);
        classes.add(HelloResource.class);
        classes.add(GroupResource.class);
        classes.add(PostResource.class);
        classes.add(CommentResource.class);
        //classes.add(LikeResource.class);
        classes.add(NotificationResource.class);
        //classes.add(FriendRequestResource.class);

        // Register filters
        classes.add(JwtAuthFilter.class);
        
        return classes;
    }
}
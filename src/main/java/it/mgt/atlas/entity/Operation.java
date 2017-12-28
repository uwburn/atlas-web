package it.mgt.atlas.entity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Operation {

    public final static String CREATE_ROLE = "CREATE_ROLE";
    public final static String CHANGE_ROLES = "CHANGE_ROLES";
    public final static String DELETE_ROLE = "DELETE_ROLE";
    public final static String CHANGE_ROLE_OPERATIONS = "CHANGE_ROLE_OPERATIONS";
    
    public final static String READ_USER = "READ_USER";
    public final static String ADD_USER = "ADD_USER";
    public final static String REMOVE_USER = "REMOVE_USER";
    public final static String READ_PROFILE = "READ_PROFILE";
    public final static String MODIFY_PROFILE = "MODIFY_PROFILE";
    
    public final static String READ_EXAMPLE = "READ_EXAMPLE";
    public final static String ADD_EXAMPLE = "ADD_EXAMPLE";
    public final static String EDIT_EXAMPLE = "EDIT_EXAMPLE";
    public final static String REMOVE_EXAMPLE = "REMOVE_EXAMPLE";
    
    public static Set<String> values() {
        return Arrays.stream(Operation.class.getFields())
                .filter(f -> f.getType().equals(String.class))
                .map(f -> {
                    try {
                        return (String) f.get(null);
                    } catch(Exception ignored) {
                        return "";
                    }
                })
                .filter(f -> f.length() > 0)
                .collect(Collectors.toSet());
    }

}
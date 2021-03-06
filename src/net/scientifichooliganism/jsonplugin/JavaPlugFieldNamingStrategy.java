package net.scientifichooliganism.jsonplugin;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

public class JavaPlugFieldNamingStrategy implements FieldNamingStrategy {
    private FieldNamingPolicy defaultPolicy;

    JavaPlugFieldNamingStrategy(){
        this(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    JavaPlugFieldNamingStrategy(FieldNamingPolicy policy){
        defaultPolicy = policy;
    }

    @Override
    public String translateName(Field field) {
        if("klass".equalsIgnoreCase(field.getName())){
            return "class";
        } else if ("objectid".equalsIgnoreCase(field.getName())){
            return "object_id";
        }else {
            return defaultPolicy.translateName(field);
        }
    }
}

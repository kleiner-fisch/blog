package com.example.demo;

import java.util.function.Consumer;

public class Util {
    
    /**
     * updates a value using a setter, if the given value is not null.
     * 
     * @param setterMethod the function to use to update the value
     * @param value the value to replace the old value
     */
    public static <T> void updateValue(Consumer<T> setterMethod, T value) {
        if (value != null){
            setterMethod.accept(value);
        }
    }

}

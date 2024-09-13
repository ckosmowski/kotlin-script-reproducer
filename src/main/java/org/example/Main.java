package org.example;

import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ScriptHost scriptHost = new ScriptHost();
        scriptHost.executeScript();
    }
}
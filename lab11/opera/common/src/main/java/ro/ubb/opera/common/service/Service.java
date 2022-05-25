package ro.ubb.opera.common.service;

public interface Service {
    int PORT = 1234;

    String HOST = "localhost";

    String TOKENS_DELIMITER = System.getProperty("tokens.delimiter");

    String ENTITIES_DELIMITER = System.getProperty("entities.delimiter");
}
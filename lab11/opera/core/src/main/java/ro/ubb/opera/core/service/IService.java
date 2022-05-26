package ro.ubb.opera.core.service;

public interface IService {
    int PORT = 1234;

    String HOST = "localhost";

    String TOKENS_DELIMITER = System.getProperty("tokens.delimiter");

    String ENTITIES_DELIMITER = System.getProperty("entities.delimiter");
}
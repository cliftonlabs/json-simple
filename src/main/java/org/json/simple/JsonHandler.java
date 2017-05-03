package org.json.simple;

public interface JsonHandler {

    void startObject(Jsoner.States state);

    void key(String key);

    boolean endObject(int returnCount);

    void startArray(Jsoner.States state);

    boolean endArray(int returnCount);

    void datum(Object value, Jsoner.States state);

}

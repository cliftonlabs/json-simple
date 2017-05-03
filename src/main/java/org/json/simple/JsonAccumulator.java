package org.json.simple;

import java.util.Deque;
import java.util.LinkedList;

public class JsonAccumulator implements JsonHandler {

    private final Deque<Object> valueStack = new LinkedList<>();

    @Override
    public void startObject(Jsoner.States state) {
        JsonObject object = new JsonObject();
        meld(object, state);
        this.valueStack.addLast(object);
    }

    @Override
    public void key(String key) {
        this.valueStack.addLast(key);
    }

    @Override
    public boolean endObject(int returnCount) {
        boolean b = this.valueStack.size() > returnCount;
        if (b) {
            this.valueStack.removeLast();
        }
        return b;
    }

    @Override
    public void startArray(Jsoner.States state) {
        JsonArray array = new JsonArray();
        meld(array, state);
        this.valueStack.addLast(array);
    }

    @Override
    public boolean endArray(int returnCount) {
        boolean b = this.valueStack.size() > returnCount;
        if (b) {
            this.valueStack.removeLast();
        }
        return b;
    }

    @Override
    public void datum(Object value, Jsoner.States state) {
        if (state == Jsoner.States.INITIAL) {
            this.valueStack.addLast(value);
        } else {
            meld(value, state);
        }
    }

    private void meld(Object object, Jsoner.States state) {
        switch (state) {
            case INITIAL:
                break;
            case PARSING_ARRAY:
                JsonArray parentArray = (JsonArray) this.valueStack.getLast();
                parentArray.add(object);
                break;
            case PARSING_OBJECT:
                String key = (String) this.valueStack.removeLast();
                JsonObject parentObject = (JsonObject) this.valueStack.getLast();
                parentObject.put(key, object);
                break;
        }
    }

    JsonArray finish() {
        return new JsonArray(this.valueStack);
    }

}

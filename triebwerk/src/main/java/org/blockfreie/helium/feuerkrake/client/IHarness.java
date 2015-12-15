package org.blockfreie.helium.feuerkrake.client;

import java.util.List;

public interface IHarness {
    List<Object> getMessages();
    void query(String sql);
    void execute(String sql);
    <T> void registerMethods(Class<T> clazz);
    <T> void registerTables(Class<T> clazz);
}

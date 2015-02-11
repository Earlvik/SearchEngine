package ru.hhschool.searchengine;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import ru.hhschool.searchengine.engine.SearchEngine;
import ru.hhschool.searchengine.engine.SearchEngineImpl;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

/**
 * Created by Earlviktor on 11.02.2015.
 */
@ApplicationPath("rest")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig(){
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(SearchEngineImpl.class).to(SearchEngine.class).in(Singleton.class);
            }
        });
        packages(true, "ru.hhschool.searchengine");
        register(JacksonFeature.class);
    }
}

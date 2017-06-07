package fr.pacreau.teamu.module;

import javax.inject.Singleton;

import dagger.Component;
import fr.pacreau.teamu.MainActivity;

@Singleton
@Component(modules = {AppModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
}


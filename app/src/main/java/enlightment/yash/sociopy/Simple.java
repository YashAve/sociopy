package enlightment.yash.sociopy;


import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.components.SingletonComponent;

interface SimpleInterface {

    String doAThing();
}

@InstallIn(SingletonComponent.class)
@Module
class SimplerModule {

    @Singleton
    @MuchComplicated
    @Provides
    public SimpleInterface provideComplicated() {
        return new Complicated();
    }

    @Singleton
    @MuchComplex
    @Provides
    public SimpleInterface provideComplex() {
        return new Complex();
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }


    @Singleton
    @Provides
    public String provideString() {
        return "some string";
    }

    @Singleton
    @Provides
    public SimpleInterface provide(String string, Gson gson) {
        return new MySimple(string, gson);
    }
}

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface MuchComplex {
}

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface MuchComplicated {
}

class Complicated implements SimpleInterface {

    public final String NAME = getClass().getSimpleName().toUpperCase();

    public Complicated() {

    }

    @Override
    public String doAThing() {
        return getClass().getSimpleName();
    }
}

class Complex implements SimpleInterface {

    public final String NAME = getClass().getSimpleName().toUpperCase();

    public Complex() {

    }

    @Override
    public String doAThing() {
        return getClass().getSimpleName();
    }
}

class MySimple implements SimpleInterface {

    private String name;
    private Gson gson;

    @Inject
    public MySimple(String name, Gson gson) {
        this.name = name;
        this.gson = gson;
    }

    @Override
    public String doAThing() {
        return "simple interface";
    }
}

/*@InstallIn(SingletonComponent.class)
@Module
abstract class SimpleModule {

    @Singleton
    @Binds
    abstract SimpleInterface bindSomeDependency(Simple simple);

    @Singleton
    @Binds
    abstract Gson bindGson(Gson gson);
}*/

@ActivityScoped
public class Simple implements SimpleInterface {

    private Simpler simpler;
    private SimpleInterface simpleInterface;

    @Inject
    public Simple(Simpler simpler, SimpleInterface simpleInterface) {
        this.simpler = simpler;
        this.simpleInterface = simpleInterface;
    }

    public String doSomethingSimple() {
        return "something simple";
    }

    public String doSomethingSimpler() {
        return simpler.doSomeOtherThing();
    }

    @Override
    public String doAThing() {
        return "a simple interface thing";
    }
}

class Simpler {

    @Inject
    public Simpler() {

    }

    public String doSomeOtherThing() {
        return "look, i did something simpler";
    }
}

package enlightment.yash.sociopy;

import javax.inject.Inject;

public class MuchSimpler {

    @MuchComplex
    private SimpleInterface complex;
    @MuchComplicated
    private SimpleInterface complicated;

    @Inject
    public MuchSimpler(SimpleInterface complex, SimpleInterface complicated) {
        this.complex = complex;
        this.complicated = complicated;
    }

    public String getComplexName() {
        return new Complex().NAME;
    }

    public String getComplicatedName() {
        return new Complicated().NAME;
    }
}
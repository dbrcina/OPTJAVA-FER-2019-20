package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class ThreadLocalRNGProvider implements IRNGProvider {

    private final ThreadLocal<IRNG> threadLocal;

    public ThreadLocalRNGProvider() {
        this.threadLocal = ThreadLocal.withInitial(RNGRandomImpl::new);
    }

    @Override
    public IRNG getRNG() {
        return threadLocal.get();
    }

}

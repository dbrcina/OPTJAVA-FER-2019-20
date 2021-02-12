package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

public class ThreadBoundRNGProvider implements IRNGProvider {

    @Override
    public IRNG getRNG() {
        return ((EVOThread) Thread.currentThread()).getRNG();
    }

}

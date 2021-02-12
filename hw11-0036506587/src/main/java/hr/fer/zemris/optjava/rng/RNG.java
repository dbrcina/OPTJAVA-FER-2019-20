package hr.fer.zemris.optjava.rng;

import java.util.Properties;

public class RNG {

    private static IRNGProvider rngProvider;

    static {
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = RNG.class.getClassLoader();
            properties.load(classLoader.getResourceAsStream("rng_config.properties"));
            String rngProviderClass = properties.getProperty("rng-provider");
            rngProvider = (IRNGProvider) classLoader.loadClass(rngProviderClass)
                    .getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }

}

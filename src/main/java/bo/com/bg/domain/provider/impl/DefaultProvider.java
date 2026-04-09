package bo.com.bg.domain.provider.impl;

import bo.com.bg.domain.provider.Provider;
import org.springframework.stereotype.Component;

@Component
public class DefaultProvider implements Provider {

    @Override
    public boolean ready() {
        return true;
    }
}

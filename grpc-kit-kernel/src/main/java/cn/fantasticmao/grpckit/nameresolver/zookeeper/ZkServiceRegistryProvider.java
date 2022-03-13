package cn.fantasticmao.grpckit.nameresolver.zookeeper;

import cn.fantasticmao.grpckit.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * A provider for {@link ZkServiceRegistry}.
 *
 * @author fantasticmao
 * @version 1.39.0
 * @since 2022-03-13
 */
public class ZkServiceRegistryProvider extends ServiceRegistry.Provider implements ZkServiceBased {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceRegistryProvider.class);

    public ZkServiceRegistryProvider() {
    }

    @Override
    public ServiceRegistry newServiceRegistry(URI serviceUri) {
        if (!SCHEME.equalsIgnoreCase(serviceUri.getScheme())) {
            return null;
        }
        return new ZkServiceRegistry(serviceUri);
    }

    public String getDefaultScheme() {
        return SCHEME;
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.apache.curator.framework.CuratorFramework");
            return true;
        } catch (ClassNotFoundException e) {
            LOGGER.error("Unable to load ZooKeeper NameResolver, can't found dependency: curator-framework", e);
            return false;
        }
    }

    @Override
    public int priority() {
        return DEFAULT_PRIORITY - 1;
    }
}

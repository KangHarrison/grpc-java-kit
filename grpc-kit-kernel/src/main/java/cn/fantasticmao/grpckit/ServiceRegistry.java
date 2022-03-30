package cn.fantasticmao.grpckit;

import java.io.Closeable;

/**
 * Register service instance, the implementation is independent of gRPC, and will
 * be called by {@link ServiceBuddy} after {@link io.grpc.Server} started.
 *
 * @author fantasticmao
 * @version 1.39.0
 * @since 2022-03-13
 */
public abstract class ServiceRegistry implements Closeable {

    /**
     * Register service instances.
     *
     * @param metadata service metadata
     * @return if succeed
     */
    public abstract boolean doRegister(ServiceMetadata metadata);

    public abstract void shutdown();

    @Override
    public void close() {
        this.shutdown();
    }

}

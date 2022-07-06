package cn.fantasticmao.grpckit;

import io.grpc.LoadBalancer;
import io.grpc.Status;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Service load balancer, implemented by using gRPC {@link io.grpc.LoadBalancer LoadBalancer}
 * and {@link io.grpc.LoadBalancerProvider LoadBalancerProvider} plugins.
 *
 * @author fantasticmao
 * @version 1.39.0
 * @see io.grpc.internal.AutoConfiguredLoadBalancerFactory.AutoConfiguredLoadBalancer
 * @since 2022-03-20
 */
public abstract class ServiceLoadBalancer extends LoadBalancer {

    /**
     * Handles newly resolved server groups and metadata attributes from name resolution system.
     * {@code servers} contained in {@link io.grpc.EquivalentAddressGroup} should be considered equivalent
     * but may be flattened into a single list if needed.
     *
     * <p>Implementations should not modify the given {@code servers}.
     *
     * @param resolvedAddresses the resolved server addresses, attributes, and config.
     */
    @Override
    public abstract void handleResolvedAddresses(ResolvedAddresses resolvedAddresses);

    /**
     * Handles an error from the name resolution system.
     *
     * @param error a non-OK status
     */
    @Override
    public abstract void handleNameResolutionError(Status error);

    /**
     * The channel asks the load-balancer to shutdown.  No more methods on this class will be called
     * after this method.  The implementation should shutdown all Subchannels and OOB channels, and do
     * any other cleanup as necessary.
     */
    @Override
    public abstract void shutdown();

    public enum Policy {
        /**
         * The pick-first balancing policy.
         *
         * @see io.grpc.internal.PickFirstLoadBalancerProvider
         */
        PICK_FIRST("pick_first"),

        /**
         * The round-robin balancing policy, see
         * {@code io.grpc.util.SecretRoundRobinLoadBalancerProvider$Provider}.
         */
        ROUND_ROBIN("round_robin"),

        /**
         * The weighted random balancing policy (the default policy).
         *
         * @see cn.fantasticmao.grpckit.loadbalancer.RandomLoadBalancerProvider
         */
        WEIGHTED_RANDOM("weighted_random"),

        /**
         * The weighted least-number-of-active-connections balancing policy.
         */
        WEIGHTED_LEAST_CONN("weighted_least_conn"),

        /**
         * The weighted least-average-response-time balancing policy.
         */
        WEIGHTED_LEAST_TIME("weighted_least_time");

        public final String name;

        Policy(String name) {
            this.name = name;
        }

        public static Policy of(String name) {
            return Arrays.stream(Policy.values())
                .filter(e -> Objects.equals(e.name, name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such policy: " + name));
        }
    }
}

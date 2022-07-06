package cn.fantasticmao.grpckit.nameresolver.dns;

import cn.fantasticmao.grpckit.ServiceURI;
import cn.fantasticmao.grpckit.ServiceURILoader;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A DNS based {@link ServiceURI}.
 *
 * <p>The capability of grouping applications will be not work.
 *
 * @author fantasticmao
 * @version 1.39.0
 * @since 2022-07-07
 */
public class DnsServiceURI extends ServiceURI {

    public DnsServiceURI(URI registryUri, String appName, String appGroup) {
        super(registryUri, appName, appGroup);
    }

    @Override
    public URI toTargetUri() {
        try {
            return new URI(super.registryUri.getScheme(), super.registryUri.getUserInfo(),
                super.registryUri.getHost(), super.registryUri.getPort(), appName,
                super.registryUri.getQuery(), super.registryUri.getFragment());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static class Loader implements ServiceURILoader {
        private static final String SCHEME = "dns";

        @Nullable
        @Override
        public ServiceURI with(URI registryUri, String appName, String appGroup) {
            if (!SCHEME.equalsIgnoreCase(registryUri.getScheme())) {
                return null;
            }
            return new DnsServiceURI(registryUri, appName, appGroup);
        }

        @Nullable
        @Override
        public ServiceURI from(URI targetUri) {
            if (!SCHEME.equalsIgnoreCase(targetUri.getScheme())) {
                return null;
            }

            URI registryUri;
            try {
                registryUri = new URI(targetUri.getScheme(), targetUri.getUserInfo(),
                    targetUri.getHost(), targetUri.getPort(), "",
                    targetUri.getQuery(), targetUri.getFragment());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }

            String appName = targetUri.getPath();
            return new DnsServiceURI(registryUri, appName, "default");
        }
    }
}

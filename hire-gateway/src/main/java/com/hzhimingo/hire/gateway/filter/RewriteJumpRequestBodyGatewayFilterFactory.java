package com.hzhimingo.hire.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RewriteJumpRequestBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<RewriteJumpRequestBodyGatewayFilterFactory.Config>
        implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(RewriteJumpRequestBodyGatewayFilterFactory.class);

    private ObjectMapper objectMapper;

    public RewriteJumpRequestBodyGatewayFilterFactory() {
        super(Config.class);
        log.info("Loaded GatewayFilterFactory [RewriteJumpRequestBody]");
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("enabled");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new RewriteJumpRequestBodyFilter(config, objectMapper);
    }

    public static class Config {

        private boolean enabled;

        public Config() {}

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    private static class RewriteJumpRequestBodyFilter implements GatewayFilter, Ordered {

        private static final Logger log = LoggerFactory.getLogger(RewriteJumpRequestBodyFilter.class);

        private final Config config;

        private final ObjectMapper objectMapper;

        public RewriteJumpRequestBodyFilter(Config config, ObjectMapper objectMapper) {
            this.config = config;
            this.objectMapper = objectMapper;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            log.info("enter rewrite jump request body filter ....");
            MediaType contentType = exchange.getRequest().getHeaders().getContentType();
            if (contentType == null) {
                return chain.filter(exchange);
            }
            if (!MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
                return chain.filter(exchange);
            }
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {

            });


            ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {

                private boolean bodyRewrite;

                @Override
                public Flux<DataBuffer> getBody() {
                    log.info("rewrite.....");
                    Flux<DataBuffer> originBody = super.getBody();
                    StringBuilder stringBuilder = new StringBuilder();
                    originBody.subscribe(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        String s = new String(bytes, StandardCharsets.UTF_8);
                        stringBuilder.append(s);
                    });
                    log.info("request body: {}", stringBuilder);
                    return super.getBody();
                }

                @Override
                public HttpHeaders getHeaders() {
                    return super.getHeaders();
                }
            };
            return chain.filter(exchange.mutate().request(requestDecorator).build());
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.objectMapper = new ObjectMapper();
    }
}

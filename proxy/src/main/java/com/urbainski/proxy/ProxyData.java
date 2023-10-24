package com.urbainski.proxy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProxyData {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyData.class);

    private List<ProxyDataDTO> listaResponse;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProxyData(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        final var resourceLoader = new DefaultResourceLoader();
        final var resource = resourceLoader.getResource("dados/proxy-data.json");

        try {
            final var json = resource.getContentAsString(Charset.defaultCharset());
            this.listaResponse = objectMapper.readValue(json, new TypeReference<List<ProxyDataDTO>>() {
            });
        } catch (Exception ex) {
            LOGGER.error("Erro ao conversar dados do proxy.", ex);
        }
    }


    public Optional<ProxyDataDTO> buscar(String method, String path) {
        return buscar(HttpMethod.valueOf(method), path);
    }

    public Optional<ProxyDataDTO> buscar(HttpMethod method, String path) {
        return listaResponse.stream()
                .filter(dto -> dto.method() == method)
                .filter(dto -> dto.path().equals(path))
                .findFirst();
    }

    public record ProxyDataDTO(
            HttpMethod method,
            String path,
            HttpStatus status,
            String mediaType,
            String responseBody) {

    }
}

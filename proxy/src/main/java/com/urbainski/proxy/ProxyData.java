package com.urbainski.proxy;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProxyData {

    private List<ProxyDataDTO> listaResponse;

    @PostConstruct
    public void init() {
        this.listaResponse = new ArrayList<>();
        this.listaResponse.add(new ProxyDataDTO(
                HttpMethod.GET,
                "/pessoa/04052939921",
                HttpStatus.OK,
                MediaType.APPLICATION_JSON,
                """
                {
                   "oid": 25636,
                   "nome": "Joeli Vivian Urbainski",
                   "documento": "04052939921",
                   "nascimento": "1992-12-25",
                   "tipoPessoa": "FISICA",
                   "agro": false
                }
                 """));
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
            MediaType mediaType,
            String responseBody) {

    }
}

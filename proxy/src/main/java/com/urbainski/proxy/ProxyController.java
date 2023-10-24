package com.urbainski.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
public class ProxyController {

    private final ProxyData proxyData;

    @Autowired
    public ProxyController(final ProxyData proxyData) {
        this.proxyData = proxyData;
    }


    @GetMapping("/**")
    public ResponseEntity<String> get(HttpServletRequest request) {
        final var response = proxyData.buscar(request.getMethod(), request.getServletPath());
        return response.map(this::buildResponseEntiy).orElseGet(() -> callReal(request));
    }

    private ResponseEntity<String> callReal(HttpServletRequest request) {
        final var restTemplate = new RestTemplate();
        try {
            final var response = restTemplate.getForEntity(request.getRequestURL().toString(), String.class);
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(Objects.requireNonNull(response.getHeaders().getContentType()))
                    .header("X-Mocked-Value", "false")
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (RestClientResponseException ex) {
            return ResponseEntity.internalServerError().body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<String> buildResponseEntiy(ProxyData.ProxyDataDTO dto) {
        return ResponseEntity.status(dto.status())
                .contentType(dto.mediaType())
                .header("X-Mocked-Value", "true")
                .body(dto.responseBody());
    }

}

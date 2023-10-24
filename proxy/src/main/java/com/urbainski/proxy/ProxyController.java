package com.urbainski.proxy;

import com.urbainski.proxy.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
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

    @PostMapping("/**")
    public ResponseEntity<String> post(HttpServletRequest request) {
        final var response = proxyData.buscar(request.getMethod(), request.getServletPath());
        return response.map(this::buildResponseEntiy).orElseGet(() -> callReal(request));
    }

    @PutMapping("/**")
    public ResponseEntity<String> put(HttpServletRequest request) {
        final var response = proxyData.buscar(request.getMethod(), request.getServletPath());
        return response.map(this::buildResponseEntiy).orElseGet(() -> callReal(request));
    }

    @DeleteMapping("/**")
    public ResponseEntity<String> delete(HttpServletRequest request) {
        final var response = proxyData.buscar(request.getMethod(), request.getServletPath());
        return response.map(this::buildResponseEntiy).orElseGet(() -> callReal(request));
    }

    private ResponseEntity<String> callReal(HttpServletRequest request) {
        final var restTemplate = new RestTemplate();
        final var method = HttpMethod.valueOf(request.getMethod());
        if (HttpMethod.GET.equals(method)) {
            return callRealGet(request, restTemplate);
        } else if (HttpMethod.POST.equals(method)) {
            return callRealPost(request, restTemplate);
        } else if (HttpMethod.DELETE.equals(method)) {
            return callRealDelete(request, restTemplate);
        }
        return ResponseEntity.internalServerError().body("Não implementado!");
    }

    private ResponseEntity<String> callRealPost(HttpServletRequest request, RestTemplate restTemplate) {
        final var headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(key -> headers.put(key, Collections.list(request.getHeaders(key))));
        final var newRequest = RequestEntity.post(request.getRequestURL().toString())
                .headers(headers)
                .body(RequestUtil.readRquestBody(request));
        final var response = restTemplate.exchange(newRequest, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .contentType(Objects.requireNonNull(response.getHeaders().getContentType()))
                .header("X-Mocked-Value", "false")
                .body(response.getBody());
    }

    private ResponseEntity<String> callRealGet(HttpServletRequest request, RestTemplate restTemplate) {
        final var newRequest = RequestEntity.get(request.getRequestURL().toString()).build();
        final var response = restTemplate.exchange(newRequest, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .contentType(Objects.requireNonNull(response.getHeaders().getContentType()))
                .header("X-Mocked-Value", "false")
                .body(response.getBody());
    }

    private ResponseEntity<String> callRealDelete(HttpServletRequest request, RestTemplate restTemplate) {
        final var newRequest = RequestEntity.delete(request.getRequestURL().toString()).build();
        final var response = restTemplate.exchange(newRequest, String.class);
        return ResponseEntity.status(response.getStatusCode())
                .contentType(Objects.requireNonNull(response.getHeaders().getContentType()))
                .header("X-Mocked-Value", "false")
                .body(response.getBody());
    }

    private ResponseEntity<String> buildResponseEntiy(ProxyData.ProxyDataDTO dto) {
        return ResponseEntity.status(dto.status())
                .contentType(MediaType.parseMediaType(dto.mediaType()))
                .header("X-Mocked-Value", "true")
                .body(dto.responseBody());
    }

}

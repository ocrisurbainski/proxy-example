package com.urbainski.proxy.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class RequestUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static String readRquestBody(HttpServletRequest request) {
        final var body = new StringBuffer();
        try {
            String line;
            final var reader = request.getReader();
            while ((line = reader.readLine()) != null)
                body.append(line);
        } catch (Exception e) {
            LOGGER.error("Erro ao ler dados do request.", e);
        }
        return body.toString();
    }

}

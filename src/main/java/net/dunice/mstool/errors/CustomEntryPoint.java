package net.dunice.mstool.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.dunice.mstool.DTO.response.common.BaseSuccessResponse;
import net.dunice.mstool.constants.ConstantFields;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        BaseSuccessResponse errorResponse = new BaseSuccessResponse();
        errorResponse.setStatusCode(7);
        errorResponse.setSuccess(true);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(ConstantFields.APPLICATION_JSON);
        response.setCharacterEncoding(ConstantFields.UTF_8);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}

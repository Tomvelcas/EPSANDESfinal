package uniandes.edu.co.epsandes.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class IPSControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // Configuración inicial si es necesaria
    }

    @Test
    public void testRegistrarIPS() throws Exception {
        String jsonRequest = "{\"nombre\":\"IPS Test\",\"direccion\":\"Calle 123\",\"telefono\":\"123456789\"}";

        mockMvc.perform(post("/api/ips")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("IPS registrada con éxito"));
    }
}
package com.codigo.unit_testing.controller;

import com.codigo.unit_testing.aggregates.constants.Constants;
import com.codigo.unit_testing.aggregates.request.EmpresaRequest;
import com.codigo.unit_testing.aggregates.response.BaseResponse;
import com.codigo.unit_testing.entity.Empresa;
import com.codigo.unit_testing.service.EmpresaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresaService empresaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deberia registrar una empresa")
    void testRegistrarEmpresa() throws Exception {
        //Arrange
        EmpresaRequest empresaRequest = new EmpresaRequest();
        empresaRequest.setNumeroDocumento("123456789");

        Empresa empresa = new Empresa();
        empresa.setNumeroDocumento("123456789");

        BaseResponse<Empresa> response = new BaseResponse<>();
        response.setCode(Constants.CODE_OK);
        response.setMessage(Constants.MSJ_OK);
        response.setObjeto(Optional.of(empresa));

        when(empresaService.crear(any())).thenReturn(ResponseEntity.ok(response));
        //ACT
        mockMvc.perform(MockMvcRequestBuilders.post("/empresa/v1/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empresaRequest)))

                //ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2001))
                .andExpect(jsonPath("$.message").value("Transacción exitosa"))
                .andExpect(jsonPath("$.objeto.numeroDocumento").value("123456789"));

    }

    //LDF
    @Test
    @DisplayName("Deberia actualizar una empresa")
    void testUpdateEmpresa() throws Exception{
        EmpresaRequest empresaRequest = new EmpresaRequest();
        empresaRequest.setNumeroDocumento("123456789");

        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNumeroDocumento("123456789");

        BaseResponse<Empresa> response = new BaseResponse<>();
        response.setCode(Constants.CODE_OK);
        response.setMessage(Constants.MSJ_OK);
        response.setObjeto(Optional.of(empresa));
        when(empresaService.actualizar(eq(1L),any())).thenReturn(ResponseEntity.ok(response));
        mockMvc.perform(MockMvcRequestBuilders.put("/empresa/v1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2001))
                .andExpect(jsonPath("$.message").value("Transacción exitosa"))
                .andExpect(jsonPath("$.objeto.id").value(1))
                .andExpect(jsonPath("$.objeto.numeroDocumento").value("123456789"));
    }
}
package com.codigo.unit_testing.service.impl;

import com.codigo.unit_testing.aggregates.constants.Constants;
import com.codigo.unit_testing.aggregates.request.EmpresaRequest;
import com.codigo.unit_testing.aggregates.response.BaseResponse;
import com.codigo.unit_testing.dao.EmpresaRepository;
import com.codigo.unit_testing.entity.Empresa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EmpresaServiceImplTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaServiceImpl empresaService;
    private Empresa empresa;
    private EmpresaRequest empresaRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        empresa = new Empresa();
        empresaRequest = new EmpresaRequest();
        empresaRequest.setNumeroDocumento("123456789");
    }

    @Test
    @DisplayName("Deberia retornar conflicto si la empresa ya existe")
    void testDeberiaRetornarEmpresaExiste(){
        //ARRANGE
        when(empresaRepository.existsByNumeroDocumento(anyString())).thenReturn(true);

        //ACT
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaService.crear(empresaRequest);

        //ASSERT
        assertNotNull(resultado);
        assertEquals(Constants.CODE_EXIST, resultado.getBody().getCode());
        assertFalse(resultado.getBody().getObjeto().isPresent());

    }

    @Test
    @DisplayName("Deberia crear empresa nueva correctamente")
    void testDeberiaCrearEmpresaNueva(){
        //ARRANGE
        when(empresaRepository.existsByNumeroDocumento(anyString())).thenReturn(false);
        when(empresaRepository.save(any())).thenReturn(empresa);

        //ACT
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaService.crear(empresaRequest);

        //ASSERT
        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertTrue(resultado.getBody().getObjeto().isPresent());
        assertSame(empresa, resultado.getBody().getObjeto().get());
    }

    @Test
    @DisplayName("Deberia obtener empresa por ID si existe")
    void testDeberiaObtenerEmpresaPorId(){
        //ARRANGE
        when(empresaRepository.findById(any())).thenReturn(Optional.of(empresa));
        //ACT
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaService.obtenerEmpresa(1L);
        //ASSERT
        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertTrue(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    @DisplayName("Deberia retornar mensaje si empresa no existe")
    void testDeberiaRetornarEmpresaNoExistente(){
        //ARRANGE
        when(empresaRepository.findById(any())).thenReturn(Optional.empty());
        //ACT
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaService.obtenerEmpresa(1L);
        //ASSERT
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, resultado.getBody().getCode());
        assertFalse(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    @DisplayName("Deberia listar empresas si existen")
    void testDeberiaListarEmpresas(){
        //arrange
        when(empresaRepository.findAll()).thenReturn(List.of(empresa));
        //act
        ResponseEntity<BaseResponse<List<Empresa>>> resultado = empresaService.obtenerTodos();
        //assert
        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertEquals(1, resultado.getBody().getObjeto().get().size());
    }

    @Test
    @DisplayName("Deberia retornar vacio si no existen empresas")
    void testDeberiaRetornarListaVacia(){
        //Arrange
        when(empresaRepository.findAll()).thenReturn(List.of());
        //Act
        ResponseEntity<BaseResponse<List<Empresa>>> resultado = empresaService.obtenerTodos();
        //Assert
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, resultado.getBody().getCode());
        assertFalse(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    @DisplayName("Deberia retornar mensaje si empresa no existe al actualizar")
    void testDeberiaRetornarNoExisteEnActualizacion(){
        when(empresaRepository.existsById(any())).thenReturn(false);
        ResponseEntity<BaseResponse<Empresa>> resultado =
                empresaService.actualizar(1L,empresaRequest);

        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, resultado.getBody().getCode());
        assertFalse(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    @DisplayName("Deberia Lanzar una excepcion si empresa no se encuenta al actualizar")
    void testDeberiaLanzarExcepcionAlObtenerEmpresaParaActualizar(){
        when(empresaRepository.existsById(any())).thenReturn(true);
        when(empresaRepository.findById(any())).thenReturn(Optional.empty());

        //Act && Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                empresaService.actualizar(1L, empresaRequest));
        assertEquals("Empresa no encontrada", exception.getMessage());

    }
    @Test
    @DisplayName("Deberia retornar un null")
    void testDeberiaRetornarNulo(){
        assertNull(empresaService.obtenerEmpresaXNumDoc("123456789"));
    }
    @Test
    @DisplayName("Deberia actualizar una empresa existente")
    void testDeberiaActualizarEmpresa(){
        when(empresaRepository.existsById(any())).thenReturn(true);
        when(empresaRepository.findById(any())).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any())).thenReturn(empresa);

        ResponseEntity<BaseResponse<Empresa>> resultado = empresaService.actualizar(1L,empresaRequest);

        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertTrue(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    @DisplayName("Deberia eliminar logicamente a una empresa existente")
    void testDeberiaEliminarLogicaEmpresaExistente(){
        when(empresaRepository.existsById(any())).thenReturn(true);
        when(empresaRepository.findById(any())).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any())).thenReturn(empresa);

        ResponseEntity<BaseResponse<Empresa>> resultado = empresaService.delete(1L);

        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertTrue(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    @DisplayName("Deberia retornar mensaje si empresa no existe")
    void testDeberiaRetornarMensajeNoExisteEmpresaEliminar(){
        when(empresaRepository.existsById(any())).thenReturn(false);
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaService.delete(1L);
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, resultado.getBody().getCode());
        assertFalse(resultado.getBody().getObjeto().isPresent());

    }



}
package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.dto.request.CrearClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import com.hotel.Hotel.mapper.ClienteMapper;
import com.hotel.Hotel.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void debeCrearCliente() {
        // Preparar: simular el guardado y la respuesta del mapper.
        CrearClienteRequest request = new CrearClienteRequest("Ana", "ana@correo.com");
        Cliente guardado = new Cliente("Ana", "ana@correo.com");
        ClienteResponse esperado = new ClienteResponse(guardado.getId(), "Ana", "ana@correo.com", true, 0);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(guardado);
        when(clienteMapper.toResponse(guardado)).thenReturn(esperado);

        // Actuar: llamar al servicio real.
        ClienteResponse resultado = clienteService.crear(request);

        // Comprobar: verificar la respuesta y los datos enviados a guardar.
        assertEquals(esperado, resultado);
        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(captor.capture());
        assertEquals("Ana", captor.getValue().getNombre());
        assertEquals("ana@correo.com", captor.getValue().getEmail());
    }

    @Test
    void debeListarClientes() {
        // Preparar.
        Cliente cliente = new Cliente("Ana", "ana@correo.com");
        List<Cliente> clientes = List.of(cliente);
        ClienteResponse respuesta = new ClienteResponse(cliente.getId(), "Ana", "ana@correo.com", true, 0);
        List<ClienteResponse> esperado = List.of(respuesta);
        when(clienteRepository.findAll()).thenReturn(clientes);
        when(clienteMapper.toResponseList(clientes)).thenReturn(esperado);

        // Actuar.
        List<ClienteResponse> resultado = clienteService.listarTodos();

        // Comprobar.
        assertEquals(esperado, resultado);
        verify(clienteRepository).findAll();
    }

    @Test
    void debeDevolverListaVaciaCuandoNoHayClientes() {
        // Preparar.
        when(clienteRepository.findAll()).thenReturn(List.of());
        when(clienteMapper.toResponseList(List.of())).thenReturn(List.of());

        // Actuar.
        List<ClienteResponse> resultado = clienteService.listarTodos();

        // Comprobar.
        assertTrue(resultado.isEmpty());
        verify(clienteRepository).findAll();
    }

    @Test
    void debeObtenerClientePorId() {
        // Preparar.
        Cliente cliente = new Cliente("Ana", "ana@correo.com");
        UUID id = cliente.getId();
        ClienteResponse esperado = new ClienteResponse(id, "Ana", "ana@correo.com", true, 0);
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponse(cliente)).thenReturn(esperado);

        // Actuar.
        ClienteResponse resultado = clienteService.obtenerPorId(id);

        // Comprobar.
        assertEquals(esperado, resultado);
        verify(clienteRepository).findById(id);
    }

    @Test
    void debeLanzarExcepcionCuandoClienteNoExiste() {
        // Preparar.
        UUID id = UUID.randomUUID();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Actuar y comprobar: la búsqueda debe lanzar una excepción.
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> clienteService.obtenerPorId(id));

        assertEquals("Cliente no encontrado con ID: " + id, error.getMessage());
        verify(clienteRepository).findById(id);
        verifyNoInteractions(clienteMapper);
    }
}

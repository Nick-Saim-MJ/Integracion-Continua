package pe.edu.upeu.sysalmacen.repositorio;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.sysalmacen.modelo.Cliente;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Rollback(false)
@ActiveProfiles("test") // Para base de datos real de pruebas
public class IClienteRepositoryTest {

    @Autowired
    private IClienteRepository clienteRepository;

    private static String dniruc;

    @BeforeEach
    public void setUp() {
        Cliente cliente = new Cliente();
        cliente.setDniruc("123456789012");
        cliente.setNombres("Nick Jara");
        cliente.setTipoDocumento("DNI");
        cliente.setDireccion("123 Calle Ficticia");

        Cliente guardada = clienteRepository.save(cliente);
        dniruc = guardada.getDniruc();
    }

    @Test
    @Order(1)
    public void testGuardarCliente() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDniruc("987654321098"); // Asignamos un valor manual
        nuevoCliente.setNombres("Nick");
        nuevoCliente.setTipoDocumento("DNI");
        nuevoCliente.setDireccion("456 Otra Calle");
        Cliente guardada = clienteRepository.save(nuevoCliente);
        assertNotNull(guardada.getDniruc());
        assertEquals("Nick", guardada.getNombres());
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Cliente> cliente = clienteRepository.findById(dniruc);
        assertTrue(cliente.isPresent());
        assertEquals("Nick Jara", cliente.get().getNombres());
    }

    @Test
    @Order(3)
    public void testActualizarCliente() {
        Cliente cliente = clienteRepository.findById(dniruc).orElseThrow();
        cliente.setNombres("Nick Saim");
        Cliente actualizada = clienteRepository.save(cliente);
        assertEquals("Nick Saim", actualizada.getNombres());
    }

    @Test
    @Order(4)
    public void testListarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        assertFalse(clientes.isEmpty());
        System.out.println("Total Clientes Registrados: " + clientes.size());
        for (Cliente m : clientes) {
            System.out.println(m.getNombres() + "\t" + m.getDniruc());
        }
    }

    @Test
    @Order(5)
    public void testEliminarCliente() {
        clienteRepository.deleteById(dniruc);
        Optional<Cliente> eliminada = clienteRepository.findById(dniruc);
        assertFalse(eliminada.isPresent(), "El Cliente debería haber sido eliminada");
    }
}

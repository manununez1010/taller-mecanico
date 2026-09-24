package persistencia;

import java.util.List;

public interface IRepositorio<T> { // IRepositorio o IDAO
    void guardar(T objeto);
    T obtenerPorId(int id);
    List<T> listarTodos();
    void actualizar(T objeto);
    void eliminar(int id);
}
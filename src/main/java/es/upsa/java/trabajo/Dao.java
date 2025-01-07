package es.upsa.java.trabajo;

import java.sql.SQLException;
import java.util.List;

public interface Dao extends AutoCloseable
{
    String seleccionarPalabraAleatoria(String categoria) throws SQLException;
    List<String> obtenerCategorias() throws SQLException;
}




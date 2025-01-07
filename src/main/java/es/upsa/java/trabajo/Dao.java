package es.upsa.java.trabajo;

import java.sql.SQLException;

public interface Dao extends AutoCloseable
{
    String seleccionarPalabraAleatoria(String categoria) throws SQLException;
}




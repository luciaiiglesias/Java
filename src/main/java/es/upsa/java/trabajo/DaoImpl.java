package es.upsa.java.trabajo;

import org.postgresql.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoImpl implements Dao
{
    Connection connection;
    public DaoImpl(String url, String user, String password) throws SQLException
    {
        DriverManager.registerDriver(new Driver());
        this.connection = DriverManager.getConnection(url, user, password);
    }

    @Override
    public void close() throws Exception
    {
        if(connection!=null) {
            this.connection.close();
            this.connection = null;
        }
    }

    @Override
    public String seleccionarPalabraAleatoria(String categoria) throws SQLException
    {
        final String SQL_CATEGORIA =    """
                                        SELECT id
                                        FROM categorias
                                        WHERE nombre = ?
                                        """;

        PreparedStatement psCategoria = connection.prepareStatement(SQL_CATEGORIA);
        psCategoria.setString(1, categoria);

        ResultSet rsCategoria = psCategoria.executeQuery();

        if(rsCategoria.next())
        {
            int idCategoria = rsCategoria.getInt(1);


        final String SQL_PALABRA =  """
                            SELECT palabra
                            FROM palabras
                            WHERE id_categoria = ?
                            ORDER BY RANDOM() LIMIT 1
                            """;
        PreparedStatement psPalabra = connection.prepareStatement(SQL_PALABRA);

        psPalabra.setInt(1, idCategoria);

        ResultSet rsPalabra = psPalabra.executeQuery();

        if(rsPalabra.next())
        {
            String palabra = rsPalabra.getString("palabra");
            System.out.println("Palabra seleccionada" + palabra);
            return palabra;
        } else
        {
            throw new SQLException("No se encontraron palabras para la categoría " + categoria);
        }

        } else
        {
            System.out.println("La categoria no existe");
            return null;
        }
    }

    @Override
    public List<String> obtenerCategorias() throws SQLException
    {
        List<String> categorias = new ArrayList<>();

        final String SQL =  """
                            SELECT nombre
                            FROM categorias
                            """;

        try (PreparedStatement psCategoria = connection.prepareStatement(SQL);
        ResultSet rsCategoria = psCategoria.executeQuery();)
        {
            while (rsCategoria.next())
            {
                categorias.add(rsCategoria.getString("nombre"));
            }
        }
        return categorias;
    }
}




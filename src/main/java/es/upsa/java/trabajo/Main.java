package es.upsa.java.trabajo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main extends Application
{
    private static final Map<String, Jugador> jugadores = new HashMap<>();
    private Jugador jugadorActual;
    private Juego juego;
    private Label progresoLabel;
    private Label intentosLabel;
    private Label letrasFalladasLabel;
    private Label resultadoLabel;
    private TextField letraInput;

    private Dao dao;

    public static void main(String[] args)
    {
            launch(args);   //inicio javaFX
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        dao = new DaoImpl("jdbc:postgresql://localhost:5432/upsa", "system", "manager");

        mostrarMenuPrincipal(stage);
    }

    private void mostrarMenuPrincipal(Stage stage)
    {
        VBox menuRoot = new VBox(15);
        menuRoot.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titulo = new Label("Juego de Ahorcado");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        //botones del menú principal
        Button empezarJuegoButton = new Button("Empezar juego");
        Button modoMultijugadorButton = new Button("Modo multijugador");
        Button estadisticasButton = new Button("Estadísticas");
        Button salirButton = new Button("Salir");

        //acciones de cada botón
        empezarJuegoButton.setOnAction(event -> solicitarNombreJugador(stage));
        modoMultijugadorButton.setOnAction(event -> solicitarNombresMultijugador(stage));
        estadisticasButton.setOnAction(event -> mostrarEstadisticas(stage));
        salirButton.setOnAction(event -> stage.close());

        //añadir elementos al VBox
        menuRoot.getChildren().addAll(titulo, empezarJuegoButton, modoMultijugadorButton, estadisticasButton, salirButton);

        //mostrar
        Scene menuScene = new Scene(menuRoot, 400, 300);
        stage.setTitle("Menú Principal");
        stage.setScene(menuScene);
        stage.show();

    }

    private void solicitarNombreJugador(Stage stage)
    {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titulo = new Label("Identifícate para empezar el juego!");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        //pido nombre al jugador
        TextField nombreInput = new TextField();
        nombreInput.setPromptText("Nombre");
        nombreInput.setMaxWidth(200);

        Button volverAtrasButton = new Button("Volver al menú");
        volverAtrasButton.setOnAction(event -> mostrarMenuPrincipal(stage));

        Button confirmarNombreButton = new Button("Confirmar");
        confirmarNombreButton.setOnAction(event -> {
            String nombre = nombreInput.getText().trim();
            if (!nombre.isEmpty()) {
                jugadorActual = jugadores.computeIfAbsent(nombre, Jugador::new);
                mostrarJuegoSolitario(stage);
            } else {
                mostrarAlerta("Error", "El nombre no puede estar vacío.");
            }
        });

        root.getChildren().addAll(titulo, nombreInput, volverAtrasButton, confirmarNombreButton);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void solicitarNombresMultijugador(Stage stage)
    {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titulo = new Label("Modo Multijugador");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Inputs para los nombres de los jugadores
        TextField jugador1Input = new TextField();
        jugador1Input.setPromptText("Nombre del Jugador 1");
        jugador1Input.setMaxWidth(200);

        TextField jugador2Input = new TextField();
        jugador2Input.setPromptText("Nombre del Jugador 2");
        jugador2Input.setMaxWidth(200);

        Button confirmarButton = new Button("Confirmar");
        confirmarButton.setOnAction(event -> {
            String nombreJugador1 = jugador1Input.getText().trim();
            String nombreJugador2 = jugador2Input.getText().trim();

            if (!nombreJugador1.isEmpty() && !nombreJugador2.isEmpty()) {
                Jugador jugador1 = jugadores.computeIfAbsent(nombreJugador1, Jugador::new);
                Jugador jugador2 = jugadores.computeIfAbsent(nombreJugador2, Jugador::new);

                mostrarIngresoPalabra(stage, jugador1, jugador2);
            } else {
                mostrarAlerta("Error", "Ambos nombres deben estar completos.");
            }
        });

        Button volverAtrasButton = new Button("Volver al menú");
        volverAtrasButton.setOnAction(event -> mostrarMenuPrincipal(stage));

        root.getChildren().addAll(titulo, jugador1Input, jugador2Input, confirmarButton, volverAtrasButton);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }


    private void mostrarJuegoSolitario(Stage stage) {
        //Palabra palabra = new Palabra("Navidad"); //de momento sin bdd
        //String categoria = "Animales";
        //String palabraAleatoria;

        VBox categoriaRoot = new VBox(10);
        categoriaRoot.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label seleccionCategoriaLabel = new Label("Selecciona una categoría:");
        categoriaRoot.getChildren().add(seleccionCategoriaLabel);

        try {
            List<String> categorias = dao.obtenerCategorias();
            for (String categoria : categorias) {
                Button categoriaButton = new Button(categoria);
                categoriaButton.setOnAction(event -> iniciarJuegoConCategoria(stage, categoria));
                categoriaRoot.getChildren().add(categoriaButton);
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar las categorías" + e.getMessage());
            return; //detiene si hay error
        }

        Scene categoriaScene = new Scene(categoriaRoot, 400, 300);
        stage.setScene(categoriaScene);
    }

    private void iniciarJuegoConCategoria(Stage stage, String categoria)
    {
        String palabraAleatoria;

        try {
            palabraAleatoria = dao.seleccionarPalabraAleatoria(categoria);
        } catch (SQLException e)
        {
            mostrarAlerta("Error", "No se pudo obtener la palabra de la base de datos: " + e.getMessage());
            return; //detiene si hay error
        }

        Palabra palabra = new Palabra(palabraAleatoria);
        juego = new Juego(palabra, jugadorActual, 6);

        // Crear el lienzo para el dibujo del ahorcado
        DibujoAhorcado dibujoAhorcado = new DibujoAhorcado(300,400);

        //VBox para el modo de un jugador
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        progresoLabel = new Label("Palabra: " + juego.getProgreso());
        intentosLabel = new Label("Intentos restantes: " + juego.getIntentosRestantes());
        letrasFalladasLabel = new Label("Letras falladas: ");
        resultadoLabel = new Label("");

        letraInput = new TextField();
        letraInput.setPromptText("Ingresa una letra");

        Button jugarTurnoButton = new Button("Adivinar Letra");
        jugarTurnoButton.setOnAction(event -> {
            String letra = letraInput.getText().toUpperCase();
            if (letra.length() == 1) {
                jugarTurno(letra.charAt(0));
                letraInput.clear();
                // Dibujar el ahorcado después de cada intento
                dibujoAhorcado.dibujarParte(juego.getIntentosRestantes());
            } else {
                resultadoLabel.setText("Por favor, ingresa solo una letra.");
            }
        });

        Button volverMenuButton = new Button("Volver al menú principal");
        volverMenuButton.setOnAction(event -> volverAlMenuPrincipal(stage)); // Vuelve al menú principal.

        root.getChildren().addAll(progresoLabel, intentosLabel, letrasFalladasLabel, letraInput, jugarTurnoButton, resultadoLabel, dibujoAhorcado, volverMenuButton);

        Scene juegoScene = new Scene(root, 400, 300);
        stage.setScene(juegoScene);
    }

    private void mostrarJuegoMultijugador(Stage stage, Palabra palabra, Jugador jugadorAdivina) {

        juego = new Juego(palabra, jugadorAdivina, 6); // Usa la palabra proporcionada

        DibujoAhorcado dibujoAhorcado = new DibujoAhorcado(300,400);

        //VBox para el modo multi
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        progresoLabel = new Label("Palabra: " + juego.getProgreso());
        intentosLabel = new Label("Intentos restantes: " + juego.getIntentosRestantes());
        letrasFalladasLabel = new Label("Letras falladas: ");
        resultadoLabel = new Label("");

        letraInput = new TextField();
        letraInput.setPromptText("Ingresa una letra");

        Button jugarTurnoButton = new Button("Adivinar Letra");
        jugarTurnoButton.setOnAction(event -> {
            String letra = letraInput.getText().toUpperCase();
            if (letra.length() == 1) {
                jugarTurno(letra.charAt(0));
                letraInput.clear();
                // Dibujar el ahorcado después de cada intento
                dibujoAhorcado.dibujarParte(juego.getIntentosRestantes());
            } else {
                resultadoLabel.setText("Por favor, ingresa solo una letra.");
            }
        });

        Button volverMenuButton = new Button("Volver al menú principal");
        volverMenuButton.setOnAction(event -> volverAlMenuPrincipal(stage));

        root.getChildren().addAll(progresoLabel, intentosLabel, letrasFalladasLabel, letraInput, jugarTurnoButton, resultadoLabel, dibujoAhorcado, volverMenuButton);

        Scene juegoScene = new Scene(root, 400, 300);
        stage.setScene(juegoScene);
    }


    private void mostrarIngresoPalabra(Stage stage, Jugador jugador1, Jugador jugador2) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label instruccionLabel = new Label(jugador1.getNombre() + ", introduce la palabra secreta para " + jugador2.getNombre() + ":");
        TextField palabraSecretaInput = new TextField();
        palabraSecretaInput.setPromptText("Palabra secreta");
        palabraSecretaInput.setMaxWidth(200);

        Button confirmarPalabraButton = new Button("Confirmar palabra");
        confirmarPalabraButton.setOnAction(event -> {
            String palabraSecreta = palabraSecretaInput.getText().toUpperCase();

            if (!palabraSecreta.isEmpty() && palabraSecreta.matches("[A-Z]+")) {
                Palabra nuevaPalabra = new Palabra(palabraSecreta);
                mostrarJuegoMultijugador(stage, nuevaPalabra, jugador2);
            } else {
                mostrarAlerta("Error", "La palabra debe contener solo letras.");
            }
        });


        Button volverAtrasButton = new Button("Volver al menú");
        volverAtrasButton.setOnAction(event -> mostrarMenuPrincipal(stage));

        root.getChildren().addAll(instruccionLabel, palabraSecretaInput, confirmarPalabraButton, volverAtrasButton);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }



    private void mostrarEstadisticas(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titulo = new Label("Estadísticas de los jugadores");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        jugadores.forEach((nombre, jugador) -> {
            Label jugadorLabel = new Label(nombre + ": " + jugador.getEstadisticas());
            root.getChildren().add(jugadorLabel);
        });

        Button volverMenuButton = new Button("Volver al menú");
        volverMenuButton.setOnAction(event -> volverAlMenuPrincipal(stage));

        root.getChildren().addAll(titulo, volverMenuButton);

        Scene estadisticasScene = new Scene(root, 400, 300);
        stage.setScene(estadisticasScene);
    }

    private void jugarTurno(char letra) {
        try {
            if (juego.jugarTurno(letra)) {
                progresoLabel.setText("Progreso: " + juego.getProgreso());
            } else {
                intentosLabel.setText("Intentos restantes: " + juego.getIntentosRestantes());
                letrasFalladasLabel.setText("Letras falladas: " + juego.getLetrasFalladas());
            }

            if (juego.esJuegoGanado()) {
                resultadoLabel.setText(juego.getResultado());
            } else if (juego.esJuegoPerdido()) {
                resultadoLabel.setText(juego.getResultado());
            }

        } catch (AppException e) {
            resultadoLabel.setText(e.getMessage());
        }
    }

    private void volverAlMenuPrincipal(Stage stage) {
        mostrarMenuPrincipal(stage);
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


}


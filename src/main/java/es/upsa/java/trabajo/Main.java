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


import java.util.Scanner;

public class Main extends Application
{
    private Jugador jugador;
    private Juego juego;
    private Label progresoLabel;
    private Label intentosLabel;
    private Label letrasFalladasLabel;
    private Label resultadoLabel;
    private TextField letraInput;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        //inicializo la instancia global del jugador solo una vez para mantener estadist
        if (jugador == null) {
            jugador = new Jugador("Jugador1");
        }

        //VBox para organizar el menú principal
        VBox menuRoot = new VBox(15);
        menuRoot.setStyle("-fx-padding: 20; -fx-alignment: center;");

        //título del menú
        Label titulo = new Label("Juego de Ahorcado");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        //botones del menú principal
        Button empezarJuegoButton = new Button("Empezar juego");
        Button modoMultijugadorButton = new Button("Modo multijugador");
        Button estadisticasButton = new Button("Estadísticas");
        Button salirButton = new Button("Salir");

        //acciones de cada botón
        empezarJuegoButton.setOnAction(event -> mostrarJuegoSolitario(stage));
        modoMultijugadorButton.setOnAction(event -> mostrarModoMultijugador(stage));
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

    private void mostrarJuegoSolitario(Stage stage) {
        Palabra palabra = new Palabra("Navidad"); //de momento sin bdd
        juego = new Juego(palabra, jugador, 6);

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
            } else {
                resultadoLabel.setText("Por favor, ingresa solo una letra.");
            }
        });

        Button volverMenuButton = new Button("Volver al menú principal");
        volverMenuButton.setOnAction(event -> start(stage)); // Vuelve al menú principal.

        root.getChildren().addAll(progresoLabel, intentosLabel, letrasFalladasLabel, letraInput, jugarTurnoButton, resultadoLabel, volverMenuButton);

        Scene juegoScene = new Scene(root, 400, 300);
        stage.setScene(juegoScene);
    }

    private void mostrarJuegoMultijugador(Stage stage, Palabra palabra) {
        jugador = new Jugador("Jugador2"); // En multijugador, este es el jugador 2
        juego = new Juego(palabra, jugador, 6); // Usa la palabra proporcionada

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
            } else {
                resultadoLabel.setText("Por favor, ingresa solo una letra.");
            }
        });

        Button volverMenuButton = new Button("Volver al menú principal");
        volverMenuButton.setOnAction(event -> start(stage));

        root.getChildren().addAll(progresoLabel, intentosLabel, letrasFalladasLabel, letraInput, jugarTurnoButton, resultadoLabel, volverMenuButton);

        Scene juegoScene = new Scene(root, 400, 300);
        stage.setScene(juegoScene);
    }


    private void mostrarModoMultijugador(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label instruccionLabel = new Label("Jugador 1: Ingresa la palabra secreta para el Jugador 2:");
        TextField palabraSecretaInput = new TextField();
        palabraSecretaInput.setPromptText("Palabra secreta");
        palabraSecretaInput.setMaxWidth(200);

        Button confirmarPalabraButton = new Button("Confirmar palabra");
        confirmarPalabraButton.setOnAction(event -> {
            String palabraSecreta = palabraSecretaInput.getText().toUpperCase();

            if (!palabraSecreta.isEmpty() && palabraSecreta.matches("[A-Z]+")) {
                //nueva instancia de Palabra y Juego con la palabra ingresada
                Palabra nuevaPalabra = new Palabra(palabraSecreta);
                juego = new Juego(nuevaPalabra, jugador, 6); // Nueva instancia del juego

                //siguiente turno para que el jugador 2 adivine
                mostrarJuegoMultijugador(stage, nuevaPalabra);
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("Palabra inválida");
                alerta.setContentText("La palabra debe contener solo letras.");
                alerta.showAndWait();
            }
        });

        root.getChildren().addAll(instruccionLabel, palabraSecretaInput, confirmarPalabraButton);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarEstadisticas(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titulo = new Label("Estadísticas del jugador");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label partidasJugadasLabel = new Label("Partidas jugadas: " + (jugador != null ? jugador.getPartidasJugadas() : 0));
        Label partidasGanadasLabel = new Label("Partidas ganadas: " + (jugador != null ? jugador.getPartidasGanadas() : 0));
        Label porcentajeVictoriasLabel = new Label(String.format("Porcentaje de victorias: %.2f%%",
                (jugador != null ? jugador.getPorcentajeVictorias() : 0.0)));

        Button volverMenuButton = new Button("Volver al menú principal");
        volverMenuButton.setOnAction(event -> start(stage));

        root.getChildren().addAll(titulo, partidasJugadasLabel, partidasGanadasLabel, porcentajeVictoriasLabel, volverMenuButton);

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

}


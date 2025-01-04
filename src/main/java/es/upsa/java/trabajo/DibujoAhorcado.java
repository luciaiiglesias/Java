package es.upsa.java.trabajo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class DibujoAhorcado extends Canvas
{
    private final GraphicsContext gc;

    public DibujoAhorcado(double width, double height)
    {
        super(width, height);
        gc = getGraphicsContext2D();
        configurarEstilo();
        dibujarBase();
    }

    private void configurarEstilo()
    {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
    }

    private void dibujarBase()
    {
        gc.strokeLine(50, 250, 150, 250);  // Línea horizontal de la base
        gc.strokeLine(100, 50, 100, 250);  // Poste vertical
        gc.strokeLine(100, 50, 200, 50);   // Brazo horizontal
        gc.strokeLine(200, 50, 200, 70);   // Cuerda
    }

    //dibujar para cada intento
    public void dibujarParte(int intentosRestantes) {
        switch (intentosRestantes) {
            case 5 -> gc.strokeOval(180, 70, 40, 40);  // Cabeza
            case 4 -> gc.strokeLine(200, 110, 200, 180); // Cuerpo
            case 3 -> gc.strokeLine(200, 130, 170, 160); // Brazo izquierdo
            case 2 -> gc.strokeLine(200, 130, 230, 160); // Brazo derecho
            case 1 -> gc.strokeLine(200, 180, 170, 220); // Pierna izquierda
            case 0 -> gc.strokeLine(200, 180, 230, 220); // Pierna derecha
        }
    }
}

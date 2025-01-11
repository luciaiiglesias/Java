package es.upsa.java.trabajo;

public class JuegoMultijugador extends Juego
{
    private Jugador jugador1;
    private Jugador jugador2;

    public JuegoMultijugador(Palabra palabra, Jugador jugador1, Jugador jugador2, int maxIntentos) {
        super(palabra, maxIntentos);
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public String getResultado() {
        EstadoJuego estado = obtenerEstadoJuego();
        if (estado == EstadoJuego.GANADO) {
            jugador2.incrementarGanadas();
            return "¡Felicidades, has ganado!";
        } else if (estado == EstadoJuego.PERDIDO) {
            jugador2.incrementarPerdidas();
            return "Game Over. La palabra era: " + palabra.getPalabraSecreta();
        }
        return ""; //si el juego sigue no hay mensaje
    }
}

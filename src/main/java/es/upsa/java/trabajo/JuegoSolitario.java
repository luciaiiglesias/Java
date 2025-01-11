package es.upsa.java.trabajo;

public class JuegoSolitario extends Juego
{
    private Jugador jugador;

    public JuegoSolitario(Palabra palabra, Jugador jugador, int maxIntentos) {
        super(palabra, maxIntentos);
        this.jugador = jugador;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public String getResultado() {
        EstadoJuego estado = obtenerEstadoJuego();
        if (estado == EstadoJuego.GANADO) {
            jugador.incrementarGanadas();
            return "¡Felicidades, has ganado!";
        } else if (estado == EstadoJuego.PERDIDO) {
            jugador.incrementarPerdidas();
            return "Game Over. La palabra era: " + palabra.getPalabraSecreta();
        }
        return ""; //si el juego sigue no hay mensaje
    }


}

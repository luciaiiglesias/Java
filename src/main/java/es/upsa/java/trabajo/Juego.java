package es.upsa.java.trabajo;

public class Juego
{
    private Palabra palabra;
    private Jugador jugador;
    private int intentosRestantes;

    public Juego(Palabra palabra, Jugador jugador, int maxIntentos)
    {
        this.palabra = palabra;
        this.jugador = jugador;
        this.intentosRestantes = maxIntentos;
    }

    public int getIntentosRestantes()
    {
        return intentosRestantes;
    }

    //jugar 1 turno
    public boolean jugarTurno(char letra) throws AppException {
        if(esJuegoTerminado())
        {
            throw new AppException("El juego ya ha terminado");
        }
        if(palabra.adivinarLetra(letra))
        {
            return true;
        } else
        {
            intentosRestantes--;
            return false;
        }
    }

    public String getProgreso()
    {
        return palabra.getProgreso();
    }

    public String getLetrasFalladas() {
        return palabra.getLetrasFalladas().toString();
    }

    //métodos comprobar estado del juego
    public EstadoJuego obtenerEstadoJuego()
    {
        if (palabra.estaCompleta()) {
            return EstadoJuego.GANADO;
        } else if (intentosRestantes <= 0) {
            return EstadoJuego.PERDIDO;
        } else {
            return EstadoJuego.EN_PROGRESO;
        }
    }

    public boolean esJuegoGanado()
    {
        return obtenerEstadoJuego() == EstadoJuego.GANADO;
    }

    public boolean esJuegoPerdido()
    {
        return obtenerEstadoJuego() == EstadoJuego.PERDIDO;
    }

    public boolean esJuegoTerminado()
    {
        return obtenerEstadoJuego() != EstadoJuego.EN_PROGRESO;
    }

    public String getResultado()
    {
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

    public void reiniciarJuego(Palabra nuevaPalabra) {
        this.palabra = nuevaPalabra;
        this.intentosRestantes = 6;
    }


}



package es.upsa.java.trabajo;

public class Jugador
{
    private String nombre;
    private int partidasGanadas;
    private int partidasPerdidas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
    }

    public void incrementarGanadas()
    {
        partidasGanadas++;
    }

    public void incrementarPerdidas()
    {
        partidasPerdidas++;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    public int getPartidasPerdidas() {
        return partidasPerdidas;
    }

    public int getPartidasJugadas() {
        return partidasGanadas + partidasPerdidas;
    }

    //métodos actualizar estadísticas
    //calcular % victorias
    public double getPorcentajeVictorias()
    {
        int partidasJugadas = partidasGanadas + partidasPerdidas;
        if (partidasJugadas == 0) {
            return 0.0;
        }
        return (double) partidasGanadas / partidasJugadas * 100;
    }

    public String getEstadisticas()
    {
        return "Ganadas: " + partidasGanadas + ", Perdidas: " + partidasPerdidas;
    }
}



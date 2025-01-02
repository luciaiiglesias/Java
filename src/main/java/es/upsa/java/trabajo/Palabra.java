package es.upsa.java.trabajo;

import java.util.HashSet;
import java.util.Set;

public class Palabra
{
    private String palabraSecreta;
    private Set<Character> letrasAdivinadas;
    private Set<Character> letrasFalladas;

    public Palabra(String palabraSecreta)
    {
        this.palabraSecreta = palabraSecreta.toLowerCase();
        this.letrasAdivinadas = new HashSet<>();
        this.letrasFalladas = new HashSet<>();
    }

    //comprobar si una letra está en la palabra secreta
    public boolean adivinarLetra(char letra)
    {
        letra=Character.toLowerCase(letra);
        if (palabraSecreta.indexOf(letra)>=0)
        {
            letrasAdivinadas.add(letra);
            return true;
        } else
        {
            letrasFalladas.add(letra);
            return false;
        }
    }

    //representación actual de la palabra
    public String getProgreso()
    {
        StringBuilder progreso = new StringBuilder();
        for(char c: palabraSecreta.toCharArray())
        {
            if(letrasAdivinadas.contains(c))
            {
                progreso.append(c).append(" ");
            } else
            {
                progreso.append("_ ");
            }
        }
        return progreso.toString().trim();
    }

    //verificar si la palabra está completa
    public boolean estaCompleta()
    {
        for(char c: palabraSecreta.toCharArray())
        {
            if(!letrasAdivinadas.contains(c))
            {
                return false;
            }
        }
        return true;
    }

    public String getPalabraSecreta() {
        return palabraSecreta;
    }

    public Set<Character> getLetrasFalladas() {
        return letrasFalladas;
    }
}



package Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Test_svolto{

    private int id_test;
    private String nome_test;
    private Studente studente;
    private ArrayList<Quiz_svolto> lista_quiz_svolti;
    private Timestamp dataConsegna;
    private int stato_correzione;
    private float punteggio_finale;


    public Test_svolto(String nome_test, Studente studente, ArrayList<Quiz_svolto> lista_quiz, Timestamp timestampConsegna){
        this.nome_test = nome_test;
        this.studente = studente;
        lista_quiz_svolti = lista_quiz;
        dataConsegna = timestampConsegna;
        punteggio_finale = 0;
    }
    public Test_svolto(){

    }


    public Timestamp getDataConsegna() {
        return dataConsegna;
    }
    public void setDataConsegna(Timestamp tmpConsegna){
        dataConsegna = tmpConsegna;
    }

    public ArrayList<Quiz_svolto> getLista_quiz_svolti() {
        return lista_quiz_svolti;
    }

    public void setLista_quiz_svolti(ArrayList<Quiz_svolto> lista_quiz_svolti) {
        this.lista_quiz_svolti = lista_quiz_svolti;
    }

    public String getNome_test() {
        return nome_test;
    }

    public void setNome_test(String nome_test) {
        this.nome_test = nome_test;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente user_studente) {
        this.studente = user_studente;
    }

    public int getId_test() {
        return id_test;
    }

    public void setId_test(int id_test) {
        this.id_test = id_test;
    }

    public int getStato_correzione() {
        return stato_correzione;
    }

    public void setStato_correzione(int stato_correzione) {
        this.stato_correzione = stato_correzione;
    }

    public float getPunteggio_finale() {
        return punteggio_finale;
    }

    public void setPunteggio_finale(float punteggio_finale) {
        this.punteggio_finale = punteggio_finale;
    }
}

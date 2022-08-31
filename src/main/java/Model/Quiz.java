package Model;

import java.util.ArrayList;

public class Quiz {

    public static class Risposta{
        private String  descrizione;
        public String getDescrizione() {
            return descrizione;
        }
        public void setDescrizione(String descrizione) {
            this.descrizione = descrizione;
        }
    }




    private int id_risp_corretta;
    private int tipo;
    private String domanda;
    private ArrayList<Risposta> risposte_arr;
    private float voto_minimo;
    private float voto_max;
    private int risp_max_char; //<-- per quiz a risposta aperta
    private int id_quiz; // <-- utilizzato solamente per fare le query sul db


    public Quiz(){
        domanda = "";
        risposte_arr = new ArrayList<Risposta>();
    }

    public void add_risposta(String descrizione){


        if (descrizione != null){
            Risposta risp_obj = new Risposta();
            risp_obj.setDescrizione(descrizione);
            risposte_arr.add(risp_obj);
        }
    }

    public String getDomanda() {
        return domanda;
    }

    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }

    public void inserisci_risposta(Risposta r) {
        this.risposte_arr.add(r);
    }

    public Risposta get_risposta(int i){
        return this.risposte_arr.get(i);
    }

    public int getId_risp_corretta() {
        return id_risp_corretta;
    }

    public void setId_risp_corretta(int id_risp_corretta) {
        this.id_risp_corretta = id_risp_corretta;
    }

    public ArrayList<Risposta> getRisposte_arr() {
        return risposte_arr;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public float getVoto_minimo() {
        return voto_minimo;
    }

    public void setVoto_minimo(float voto_minimo) {
        this.voto_minimo = voto_minimo;
    }

    public float getVoto_max() {
        return voto_max;
    }

    public void setVoto_max(float voto_max) {
        this.voto_max = voto_max;
    }

    public int getMaxChar() {
        return risp_max_char;
    }

    public void setMaxChar(int maxChar) {
        this.risp_max_char = maxChar;
    }

    public int getId_quiz() {
        return id_quiz;
    }

    public void setId_quiz(int id_quiz) {
        this.id_quiz = id_quiz;
    }
}

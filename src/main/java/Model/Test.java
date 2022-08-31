package Model;

import java.util.ArrayList;

public class Test {

private String nome;
private Docente docente;
private ArrayList<Quiz> lista_quiz;

    public Test(String nome_test, Docente docente, ArrayList<Quiz> lista_quiz) {
        nome = nome_test;
        this.docente = docente;
        this.lista_quiz = lista_quiz;
    }

    public Test() {
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Quiz> getLista_quiz() {
        return lista_quiz;
    }

    public void setLista_quiz(ArrayList<Quiz> lista_quiz) {
        this.lista_quiz = lista_quiz;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Utente docente) {
        this.docente = (Docente) docente;
    }
}

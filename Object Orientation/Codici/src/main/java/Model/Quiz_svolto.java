package Model;

public class Quiz_svolto {
    private int id_quiz_svolto;
    private int id_quiz;
    private int tipo_quiz;
    private Studente studente;
    private Quiz.Risposta risposta_inserita;
    private float punteggio_assegnato;
    private int id_test;

    public int getId_quiz() {
        return id_quiz;
    }

    public void setId_quiz(int id_quiz) {
        this.id_quiz = id_quiz;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    public Quiz.Risposta getRisposta_inserita() {
        return risposta_inserita;
    }

    public void setRisposta_inserita(Quiz.Risposta risposta_inserita) {
        this.risposta_inserita = risposta_inserita;
    }

    public int getTipo_quiz() {
        return tipo_quiz;
    }

    public void setTipo_quiz(int tipo_quiz) {
        this.tipo_quiz = tipo_quiz;
    }

    public int getId_test() {
        return id_test;
    }

    public void setId_test(int id_test) {
        this.id_test = id_test;
    }

    public float getPunteggio_assegnato() {
        return punteggio_assegnato;
    }

    public void setPunteggio_assegnato(float punteggio_assegnato) {
        this.punteggio_assegnato = punteggio_assegnato;
    }

    public int getId_quiz_svolto() {
        return id_quiz_svolto;
    }

    public void setId_quiz_svolto(int id_quiz_svolto) {
        this.id_quiz_svolto = id_quiz_svolto;
    }
}

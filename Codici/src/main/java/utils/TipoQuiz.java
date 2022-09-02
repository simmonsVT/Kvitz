package utils;

public enum TipoQuiz {

    RISPOSTA_MULTIPLA(0), RISPOSTA_APERTA(1);

    int value;
    TipoQuiz(int i) {
        this.value = i;
    }

    public int get_tipo(){
        return value;
    }
}

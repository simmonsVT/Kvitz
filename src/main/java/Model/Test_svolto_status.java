package Model;

public enum Test_svolto_status {
    IN_ATTESA_CORREZIONE(0),
    CORRETTO(1);

    private int value;

    Test_svolto_status(int value) {
        this.value = value;
    }

    public int getStatusId() {
        return value;
    }
}

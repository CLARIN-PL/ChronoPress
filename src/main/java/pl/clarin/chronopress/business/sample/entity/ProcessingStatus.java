package pl.clarin.chronopress.business.sample.entity;

public enum ProcessingStatus {

    PROCESSED("Procesowanie zakończone"), TO_PROCESS("Do ponownego procesowania");

    private String name;

    ProcessingStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

package pl.clarin.chronopress.presentation.page.admin.datamanagment;

public class ImportFonemsEvent {

    private String path;

    public ImportFonemsEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

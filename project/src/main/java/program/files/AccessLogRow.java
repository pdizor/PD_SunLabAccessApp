package program.files;

public class AccessLogRow {

    private String id;
    private String time;

    public AccessLogRow(String id, String time) {

        this.id = id;
        this.time = time;

    }

    public String getId() {
        return this.id;
    }

    public String getTime() {
        return time;
    }

}

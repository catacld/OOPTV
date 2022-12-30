import platformlogic.StreamingService;

public final class Main {

    private Main() {
    }

    /**
     * main
     */
    public static void main(final String[] args) {
        StreamingService service = new StreamingService(args[0]);
        service.start();


    }
}

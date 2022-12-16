import platformlogic.StreamingService;

public class Main {
    public static void main(String[] args) {
        StreamingService service = new StreamingService(args[0]);
        service.start();


    }
}

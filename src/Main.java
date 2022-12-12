import classes.StreamingService;

public class Main {
    public static void main(String[] args) {
        StreamingService service = new StreamingService(args[0]);
        service.start();
//        Double d = 0.00;
//        System.out.println(String.format("%.2f",d));

    }
}

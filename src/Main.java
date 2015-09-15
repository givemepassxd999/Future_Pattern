public class Main {
    public static void main(String[] args){
        Client client = new Client();
        Data data = client.request();
        System.out.println("result = " + data.getResult());
    }
}

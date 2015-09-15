public class RealData implements Data{

    protected String result;

    public RealData() {}

    public void doTask(){
        for(int i = 1; i <= 10; i++){
            System.out.println(i * 10 + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        result = "completed";
    }
    @Override
    public String getResult() {
        return result;
    }
}

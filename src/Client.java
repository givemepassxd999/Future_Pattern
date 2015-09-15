public class Client {
    private FutureData mFutureData;

    public Client(){
        mFutureData = new FutureData();
    }

    public Data request(){
        new Thread(){
            @Override
            public void run() {
                System.out.println("start!");
                RealData realData = new RealData();
                realData.doTask();
                mFutureData.setRealData(realData);
            }
        }.start();
        return mFutureData;
    }
}

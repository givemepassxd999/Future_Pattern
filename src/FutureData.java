public class FutureData implements Data{

    protected RealData mRealData;

    public synchronized void setRealData(RealData realData){
        mRealData = realData;
        notifyAll();
    }

    public synchronized String getResult() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mRealData.result;
    }
}

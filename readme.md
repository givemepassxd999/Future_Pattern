�p�G���@�ӥ��Ȼݭn��@�U�ɶ��Ӱ���, �i�O�ϥΪ̫o���i�H���䵥�ݥ��ȧ���,
�o�ɭԴN�i�H�ϥ�Future Pattern,
���I���w��iPhone������, �ڥ�����@�ӹw�ʳ�, ����iPhone�ӤF�A���w�ʳ洫����C

<img src="https://dl.dropboxusercontent.com/u/24682760/Android_AS/FuturePattern/future_pattern.png">
�p�W��, �ڥ����A�@��Future Data����, 
���O�ڷ|�h����Real Data����, ���ڰ��槹��, 
�h�|�N���G�^�ǵ��A��Future Data����C

```
�D��: ������server�n�@�Ӫ���
�Ѫk: �ϥ�Future Pattern, server�����A�@��Future Data, 
      ����Real Data�n�F, �A�N���G�^�ǵ�Future Data�C
```


���ŧi�@��Data����C

```java
public interface Data {
    public String getResult();
}
```
�ѩ�u�O�Ҥl, �u�ŧi�@�Ӧ^�ǵ��G����k�C

```java
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
```
�������ڭ̪�Future Data�h��@Data, �ŧi��Ӥ�k,
�Ĥ@�Ӥ�k�O�NReadData�����������, �N��ڭ̪�Real Data�n�F,
�]���N�i�H�h��Future Data���ݪ������s��, �򥦻��i�H�^�ǵ��G�F�C

�ĤG�Ӥ�k�N�O�b�o�䵥��, ��Real Data�ӥs����, �N�i�H�^��Real Data�����G�C

�`�N�o��Ӥ�k���n�ϥ�synchronized����r, �קK�W�꦳�H�}���i�ӡC

���Ӭ�Real Data����

```java
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
```
��Real Data�Q�إߥX�Ӫ��ɭ�, doTask�C���j���1��ΨӼ���server���檺�i��,
�åB�M�X�ʤ���, ���q�{���X�����H��, �h��ܧ����C

���U�Ӭݧڭ�Client������I�s�C

```java
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
```
�@�}�l�|�h�إ߰�����, ���찲����H��N�i�H�ϥ�request, 
������Thread�brequest�Ӫ��ɭ�, �N�h�إ�Real Data,
�åB�^��Future Data

�ѤU�D�{��

```java
public class Main {
    public static void main(String[] args){
        Client client = new Client();
        Data data = client.request();
        System.out.println("result = " + data.getResult());
    }
}
```

�ʧ@�D�`²��, �u�n�I�sClient����request, �N�i�H�ݨ�ʤ���i�׶}�l�]���쵲��,
�^�ǵ������r��C

```java
start!
10%
20%
30%
40%
50%
60%
70%
80%
90%
100%
result = completed
```


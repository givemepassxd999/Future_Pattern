如果有一個任務需要花一下時間來執行, 可是使用者卻不可以那邊等待任務完成,
這時候就可以使用Future Pattern,
有點像預購iPhone的概念, 我先拿到一個預購單, 等到iPhone來了再拿預購單換實機。

<img src="https://dl.dropboxusercontent.com/u/24682760/Android_AS/FuturePattern/future_pattern.png">
如上圖, 我先給你一個Future Data物件, 
但是我會去執行Real Data物件, 等我執行完畢, 
則會將結果回傳給你的Future Data物件。

```
題目: 模擬跟server要一個物件
解法: 使用Future Pattern, server先給你一個Future Data, 
      等待Real Data好了, 再將結果回傳給Future Data。
```


先宣告一個Data物件。

```java
public interface Data {
    public String getResult();
}
```
由於只是例子, 只宣告一個回傳結果的方法。

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
接著讓我們的Future Data去實作Data, 宣告兩個方法,
第一個方法是將ReadData物件指派給它, 代表我們的Real Data好了,
因此就可以去把Future Data等待的部分叫醒, 跟它說可以回傳結果了。

第二個方法就是在這邊等待, 讓Real Data來叫醒我, 就可以回傳Real Data的結果。

注意這兩個方法都要使用synchronized關鍵字, 避免上鎖有人開門進來。

那來看Real Data部分

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
當Real Data被建立出來的時候, doTask每次迴圈睡1秒用來模擬server執行的進度,
並且映出百分比, 當整段程式碼完成以後, 則顯示完成。

接下來看我們Client物件怎麼呼叫。

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
一開始會去建立假物件, 拿到假物件以後就可以使用request, 
中間的Thread在request來的時候, 就去建立Real Data,
並且回傳Future Data

剩下主程式

```java
public class Main {
    public static void main(String[] args){
        Client client = new Client();
        Data data = client.request();
        System.out.println("result = " + data.getResult());
    }
}
```

動作非常簡單, 只要呼叫Client物件的request, 就可以看到百分比進度開始跑直到結束,
回傳結束的字串。

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


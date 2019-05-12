import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WaitNotifyTest {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread waitThread1 = new Thread(new Wait(),"WaitThread1");
        waitThread1.start();
        Thread waitThread = new Thread(new Wait(),"WaitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(),"NotifyThread");
        notifyThread.start();
    }
    static class Wait implements Runnable{

        @Override
        public void run() {
            //加锁
            synchronized (lock){
                //当条件不满足时，继续wait,同时释放了lock的锁
                while (flag){
                    try {
                        System.out.println(Thread.currentThread()+"flag is true.wait@ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
                //条件满足时，完成工作
                System.out.println(Thread.currentThread()+"flag is false.running@ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                try {
                    TimeUnit.SECONDS.sleep(1);
                }catch (Exception e){

                }
            }
        }
    }
    static class Notify implements Runnable{

        @Override
        public void run() {
            //notify和notifyAll,wait必须在同步代码中执行
            synchronized (lock){
                //获取lock的锁，然后进行通知，通知时不会释放lock的锁
                //直到当前线程释放了lock后，WaitThread才能从wait方法中返回
                    System.out.println(Thread.currentThread()+"hold lock.notify@ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    lock.notifyAll();  //唤醒调用了wait的线程，使它们从waiting状态进入blocked状态 然后等到当前线程释放锁时，位于阻塞队列的其他线程通过竞争获取锁
                    flag = false;
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    }catch (Exception e){

                    }
            }
        }
    }
}

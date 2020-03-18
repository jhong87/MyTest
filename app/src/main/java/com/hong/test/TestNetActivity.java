package com.hong.test;

import android.app.Activity;
import android.os.Bundle;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestNetActivity extends Activity {

    private final static int Thread_Count = 1000;
    private final static int Random_Delay_Time = 3 * 60 * 1000;
    private final static int Add_Delay_Time = 23;
    private final static int Random_Action_Count = 7 * 60;
    private final static int Add_Action_Count = 50 * 60;

    private ExecutorService executor;
    private Random random;
    private boolean running;

    private DeviceTask[] tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random(System.currentTimeMillis());
        tasks = new DeviceTask[Thread_Count];
        for (int i = 0; i < Thread_Count; i++) {
            tasks[i] = new DeviceTask(i + 1);
        }

        running = true;
        executor = Executors.newFixedThreadPool(Thread_Count + 1);
        executor.execute(mainTask);
    }

    @Override
    protected void onDestroy() {
        running = false;
        executor.shutdown();
        for (DeviceTask task : tasks) {
            task.cancel();
        }

        super.onDestroy();
    }

    private int getOnlineCount() {
        int count = 0;
        for (DeviceTask task : tasks) {
            if (task.inputStream != null && task.outputStream != null) {
                count++;
            }
        }
        return count;
    }

    private Runnable mainTask = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; running && i < tasks.length; i++) {
                try {
                    executor.execute(tasks[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private class DeviceTask implements Runnable {

        private int index;
        private String id;
        private byte[] data;

        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        DeviceTask(int n) {
            index = n;
            id = String.format(Locale.CHINA, "%010d", n);

            socket = null;
            inputStream = null;
            outputStream = null;

            if (index % 100 == 1) {
                int size = random.nextInt(200) + 400;
                data = new byte[size];
                random.nextBytes(data);
            } else {
                data = new byte[1024];
            }
        }

        @Override
        public void run() {
            while (running) {
                try {
                    long delay = random.nextInt(Random_Delay_Time) + Add_Delay_Time;
                    while (running && delay > 0) {
                        if (delay > 100) Thread.sleep(100);
                        else Thread.sleep(delay);
                        delay -= 100;
                    }
                    if (!running) break;

//                    socket = new Socket("192.168.3.89", 5000);
                    socket = new Socket("192.168.0.104", 5000);
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    outputStream.write(id.getBytes());
                    outputStream.flush();
                    System.out.println("hong ++++++++++++++++++ start: " + id + ", online: " + getOnlineCount());

                    int count = random.nextInt(Random_Action_Count) + Add_Action_Count;
                    if (index % 100 == 1) {
                        for (int i = 0; running && i < count * 10; i++) {
                            Thread.sleep(1000);
                            outputStream.write(data);
                            outputStream.flush();
                        }
                    } else {
                        for (int i = 0; running && i < count; i++) {
                            int len = inputStream.read(data);
                            if (len < 0) break;
                            else if (len % 30 == 0) {
                                outputStream.write(data, 0, 6);
                                outputStream.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("hong ++++++++++++++++++ error:" + id + ", " + e.getMessage());
                }
                cancel();
                System.out.println("hong ++++++++++++++++++ finish:" + id + ", online: " + getOnlineCount());
            }
        }

        private void cancel() {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

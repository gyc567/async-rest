package com.github.ericguo.servlet.nio;

/**
 * Created by eric567 [email:gyc567@126.com]
 * on 5/13/2017.
 */
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncReadListener implements ReadListener {

    private final AsyncContext ac;
    private final ServletInputStream is;
    private final ServletOutputStream os;

    final Queue<String> queue = new LinkedBlockingQueue<String>();

    public AsyncReadListener(AsyncContext ac, ServletInputStream is, ServletOutputStream os) {
        this.ac = ac;
        this.is = is;
        this.os = os;
    }


    @Override
    public void onDataAvailable() throws IOException {
        System.out.println("AsyncReadListener: data available ");

        int len = -1;
        byte[] b = new byte[1024];
        StringBuilder sb = new StringBuilder();

        // The ReadListener will be invoked again when
        // the input#isReady is changed from false to true
        while (is.isReady() && (len = is.read(b)) != -1) {
            String data = new String(b, 0, len);
            sb.append(data);
        }
        queue.add(sb.toString());
    }

    @Override
    public void onAllDataRead() throws IOException {
        System.out.println("AsyncReadListener: All data read.. ");

        // now all data are read, set up a WriteListener to write
        os.setWriteListener(new AsyncWriteListener(ac, queue, os));
    }



    @Override
    public void onError(Throwable t) {
        System.out.println("AsyncReadListener onError() " + t);
        t.printStackTrace();
        ac.complete();
    }
}

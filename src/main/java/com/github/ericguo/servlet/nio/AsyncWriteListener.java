package com.github.ericguo.servlet.nio;

/**
 * Created by eric567 [email:gyc567@126.com]
 * on 5/10/2017.
 */

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.util.Queue;

public class AsyncWriteListener implements WriteListener {

    private final AsyncContext ac;
    private final Queue<String> queue;
    private final ServletOutputStream os;
    private static final String header = "<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\"><body>";
    private static final String footer = "</body></html>";
    private boolean writeHeader;

    public AsyncWriteListener(AsyncContext ac, Queue<String> queue, ServletOutputStream os) {
        this.ac = ac;
        this.queue = queue;
        this.os = os;
        writeHeader = true;
    }

    @Override
    public void onWritePossible() throws IOException {
        System.out.println("AsyncWriteListener: onWritePossible.. ");
        if ( writeHeader ) {
            os.println(header);
            writeHeader = false;
        }

        while (queue.peek() != null && os.isReady()) {
            String data = queue.poll();
            os.print(data);
        }

        // complete the async process when there is no more data to write
        if (queue.peek() == null) {
            os.println(footer);
            ac.complete();
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("AsyncWriteListener onError() " + t);
        t.printStackTrace();
        ac.complete();
    }
}
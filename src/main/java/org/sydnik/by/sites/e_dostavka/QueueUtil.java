package org.sydnik.by.sites.e_dostavka;

import org.sydnik.by.framework.utils.Logger;
import org.sydnik.by.sites.e_dostavka.data.Product;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueUtil {
    private static QueueUtil queueUtil;
    private ArrayBlockingQueue<Product> queue;

    private QueueUtil() {
        queue = new ArrayBlockingQueue(1000000);
    }

    public static QueueUtil getInstance() {
        if (queueUtil == null) {
            queueUtil = new QueueUtil();
        }
        return queueUtil;
    }

    public synchronized static int size() {
        return getInstance().queue.size();
    }

    public synchronized static Product take() {
        try {
            return getInstance().queue.take();
        } catch (InterruptedException e) {
            Logger.error(QueueUtil.class, e.getMessage());
        }
        return null;
    }

    public ArrayBlockingQueue<Product> getQueue() {
        return queue;
    }

    public synchronized static void add(Product product) {
        getInstance().queue.add(product);
    }

    public synchronized static void addList(List<Product> list) {
        getInstance().queue.addAll(list);
    }

    public synchronized static boolean isEmpty() {
        return getInstance().queue.isEmpty();
    }
}

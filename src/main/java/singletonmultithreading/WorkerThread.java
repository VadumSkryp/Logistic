package singletonmultithreading;

public class WorkerThread extends Thread {
    private String message;

    public WorkerThread(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void run() {
        MySingleton singleton = MySingleton.getInstance();
        singleton.printMessage(message);
    }
}


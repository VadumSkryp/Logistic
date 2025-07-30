package singletonmultithreading;

public class MySingleton {
    private static MySingleton instance;


    private MySingleton() {
        System.out.println("Singleton instance created!");
    }

    public static synchronized MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }


    public void printMessage(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message + " | Singleton hash: " + this.hashCode());
    }
}


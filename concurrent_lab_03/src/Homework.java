import java.util.concurrent.Semaphore;

/**
 * Created by yellowflash on 9/26/16.
 */
public class Homework {

    public static void main(String[] args) {

        Semaphore mutex_1 = new Semaphore(1);       //binary semaphore
        Semaphore mutex_2 = new Semaphore(1);       //binary semaphore
        Semaphore mutex_3 = new Semaphore(0);       //semaphore with zero initial value
        int initialPizzaCount = 5;                  //initial pizza count
        int numberOfStudents = 3;                   //number of students available

        Pizza.pizzaCount = initialPizzaCount;
        Thread t = null;

        for (int i = 0; i < numberOfStudents; i++) {
            //create students
            t = new Thread(new Student(mutex_1, mutex_2, mutex_3, initialPizzaCount), "Student_" + i);
            t.start();
        }

    }
}

/**
 * kamal class which used to distribute pizza
 */

class Kamal implements Runnable {

    Semaphore mutex_3;
    int pizzaCount = 0;

    public Kamal(Semaphore mutex_3, int pizzaCount) {
        this.mutex_3 = mutex_3;
        this.pizzaCount = pizzaCount;
    }

    @Override
    public void run() {
        Pizza.pizzaCount = pizzaCount;      //refill pizza
        System.out.println("Pizza refilled and wake up Students_" + Thread.currentThread().getName());
        mutex_3.release();                  //wake up students who slept without pizza

    }
}

/**
 * Student class: students are studying while eating pizza
 */

class Student implements Runnable {

    Semaphore mutex_1, mutex_2, mutex_3;
    int pizzaCount = 0;
    Thread t = null;

    public Student(Semaphore mutex_1, Semaphore mutex_2, Semaphore mutex_3, int pizzaCount) {
        this.mutex_1 = mutex_1;
        this.mutex_2 = mutex_2;
        this.mutex_3 = mutex_3;
        this.pizzaCount = pizzaCount;
    }

    @Override
    public void run() {
//loop will indicate the continuous pizza eating and studying
        while (true) {

            try {
                mutex_1.acquire();          //lock critical section which takes pizza piece
            } catch (InterruptedException e) {
                mutex_1.release();
            }
            if (Pizza.pizzaCount > 0) {     //if pizza count >0 eat
                System.out.println("eating " + Thread.currentThread().getName() + "Piece number " + Pizza.pizzaCount);
            }
            Pizza.pizzaCount--;             // reduce pizza piece

            mutex_1.release();              // release the critical section of taking pizza


            if (Pizza.pizzaCount <= 0) {    //if run out of pizza
                try {
                    mutex_2.acquire();      //applying this mutex will block all the students who does not have pizza
                } catch (InterruptedException e) {
                    mutex_2.release();
                }

                if (Pizza.pizzaCount < 0) {     //this if condition will prevent placing multiple orders
                    try {
                        System.out.println("Pizza Ordered_" + Thread.currentThread().getName()); //place new order
                        t = new Thread(new Kamal(mutex_3, pizzaCount), "Kamal");
                        t.start();
                        mutex_3.acquire();             //block the person who place order until pizza getting refilled

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mutex_2.release();       //release the mutex which applied for block students who does not have pizza
            }


        }

    }
}

/**
 * pizza class which keep the record of number of pizza pieces
 */
class Pizza {
    public static int pizzaCount = 0;
}
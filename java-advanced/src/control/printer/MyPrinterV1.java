package control.printer;

import util.MyLogger;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class MyPrinterV1 {

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        final Scanner userInput = new Scanner(System.in);
        while (true){
            log("enter string [exit (q)] :");
            final String input = userInput.nextLine();

            // printer 쓰레드의 run 메서드가 바로 종료되지 않는다! 최대 3초정도의 지연이 있다!
            if(input.equals("q")){
                printer.work = false;
                break;
            }

            printer.addJob(input);
        }
    }

    static class Printer implements Runnable {
        volatile boolean work = true;

        Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            while (work){
                if(jobQueue.isEmpty()){
                    continue;
                }

                final String job = jobQueue.poll();
                log("print start : " + job + ", waiting document : " + jobQueue);
                sleep(3000);
                log("print complete");
            }
        }

        public void addJob(String input){
            jobQueue.offer(input);
        }
    }
}

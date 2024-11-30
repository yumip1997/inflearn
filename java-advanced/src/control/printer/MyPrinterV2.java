package control.printer;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.MyLogger.log;

public class MyPrinterV2 {

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        final Scanner userInput = new Scanner(System.in);
        while (true){
            log("enter string [exit (q)] :");
            final String input = userInput.nextLine();

            // printerThread run 메서드가 바로 종료되지 않는다! 최대 3초정도의 지연이 있다!
            if(input.equals("q")){
                printer.work = false;
                printerThread.interrupt();
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

                try{
                    String job = jobQueue.poll();
                    log("print start : " + job + ", waiting document : " + jobQueue);
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    log("interrupted!");
                    break;
                }
            }
            log("print complete");
        }

        public void addJob(String input){
            jobQueue.offer(input);
        }
    }

}

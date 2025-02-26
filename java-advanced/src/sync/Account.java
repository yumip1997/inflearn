package sync;

public class Account {

    private int balance;

    public Account(int balance) {
        this.balance = balance;
    }

    public void withdraw(int amount) {
        System.out.println("["+Thread.currentThread().getName()+"] 출금을 시작합니다. 현재 잔액: " + balance);
        if(balance < amount) {
            System.out.println("["+Thread.currentThread().getName()+"] 출금에 실패했습니다. (잔액이 부족)");
            return;
        }

        System.out.println("["+Thread.currentThread().getName()+"] 출금을 시도합니다.");
        balance -= amount;
        System.out.println("["+Thread.currentThread().getName()+"] 출금에 성공했습니다. 현재 잔액: " + balance);
    }

    public int getBalance() {
        return balance;
    }

    public static void main(String[] args) throws InterruptedException {
        Account bankAccount = new Account(1000);

        Thread t1 = new Thread(() -> bankAccount.withdraw(800), "Thread-1");
        Thread t2 = new Thread(() -> bankAccount.withdraw(800), "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("최종 잔액 : " + bankAccount.getBalance());
    }
}

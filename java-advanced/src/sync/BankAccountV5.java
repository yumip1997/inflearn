package sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV5 implements BankAccount {

    private final Lock lock = new ReentrantLock();
    private int balance;

    public BankAccountV5(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작 : " + getClass().getSimpleName());

        if(!lock.tryLock()){    // 락을 획득할 수 있다면 withdraw 작업 포기
            log("[진입 실패] 이미 처리 중인 작업이 있습니다.");
            return false;
        }

        lock.lock(); // ReentrantLock을 사용하여 lock
        try{
            log("[검증 시작] 출금액: " + amount + ", 잔액: " + balance);
            // 잔고가 출금액 보다 적으면 false
            if(balance < amount) {
                log("[검증 실패]");
                return false;
            }

            // 잔고가 출금액보다 많으면 진행
            log("[검증 완료] 출금액: " + amount + ", 잔액: " + balance);
            sleep(1000);

            balance -= amount;

            log("[출금 완료] 출금액: " + amount + ", 잔액: " + balance);
        }finally {
            lock.unlock();  // ReentrantLock 해제
        }

        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        return balance;
    }
}

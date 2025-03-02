package sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV2 implements BankAccount {

    private int balance;

    public BankAccountV2(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public synchronized boolean withdraw(int amount) {
        log("거래 시작 : " + getClass().getSimpleName());

        // == 임계 영역 시작 ==
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
        // == 임계 영역 종료 ==

        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        return balance;
    }
}

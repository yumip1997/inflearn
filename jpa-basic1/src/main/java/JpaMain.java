import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // 어플리케이션 로딩 시점 한 개만 만든다

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Member member = new Member();
            member.setName("member1");
            member.setAddress(new Address("city1", "street", "1000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "1000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "1000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("===== START ======");
            Member findMember = em.find(Member.class, member.getId());
            findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "1000"));

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            System.out.println("exception = " + e);
        }finally {
            em.close();
        }
        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("username = " + member.getName());

        System.out.println("team = " + member.getTeam().getName());
    }
}

package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/*
    20210711
    양방향 매핑시 연관관계 주인과 피주인의 모두 등록해야 한다.
    1. jpa를 쓰지 않고 테스트 작성 시 문제가 될 수 있다.
    2. 피주인을 조회했을 때, 영속성 컨텍스트 안에 있는 값을 가져와 예기치 못한 에러가 발생할 수 있음
    따라서, 연관관계 편의 메소드를 생성하자.

    연관 관계 편의 메소드는 한 쪽만 정의
    양방향 매핑 시 무한루프 조심
    - toString, lombok, json 생성 라이브러리
 */


public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Order order = new Order();
            order.addOrderItem(new OrderItem());
            // order를 통해서 item을 조회하는 것은 비지니스적으로 굉장히 빈번히 발생
            // 따라서, order에서 item에 접근할 수 있도록 양방향 연관관계를 맺는다.
            // 개발 상의 편의를 위해 양방향 연관관계 구현

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);

            em.persist(orderItem);

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();

        }
        emf.close();

    }
}


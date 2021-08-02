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
            Team team = new Team();
            team.setName("teamA");
//          team.getMembers().add(member);
//          연관관계의 주인이 아닌 team은 값을 추가/수정할 수 없다.
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);
//            member.setTeamId(team.getId());

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();
            for(Member m : members){
                System.out.println("m = " + m.getUsername());
            }

//            Team findTeam = findMember.getTeam();
//            System.out.println("findTeam = " + findTeam.getName());
//            Long findTeamId = findMember.getTeamId();



            Team newTeam = em.find(Team.class, 100L);
            findMember.setTeam(newTeam);
            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();

        }
        emf.close();

    }
}


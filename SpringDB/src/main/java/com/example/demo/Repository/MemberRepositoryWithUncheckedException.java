package com.example.demo.Repository;

import com.example.demo.exception.MyDBException;
import com.example.demo.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryWithUncheckedException implements MemberRepository{

    private final DataSource dataSource;

    public MemberRepositoryWithUncheckedException(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member){
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            //PrepareStatement - sql 파라미터를 바운딩 해줌 & DB에 넘겨 쿼리 실행을 요청
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw new MyDBException(e);
        }finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public Member findById(String memberId){
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("Member not found memberId = " + memberId);
            }

        }catch (SQLException e){
            log.error("db error", e);
            throw new MyDBException(e);
        }finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void update(String memberId, int money){
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int i = pstmt.executeUpdate();
            log.info("resultSetSize = {}", i);
        } catch (SQLException e) {
            log.error("db error", e);
            throw new MyDBException(e);
        }finally {
            close(con, pstmt, null);
        }
    }


    @Override
    public void delete(String memberId){
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        con = getConnection();
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw new MyDBException(e);
        }
    }

    /*
    리소스 정리
     */
    private void close(Connection con, Statement stat, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stat);
        //트랜잭션 동기화 매니저가 보관하고 있는 connection 닫기
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection(){
        //트랜잭션 동기화 매니저가 보관하고 있는 connection 호출
        //쓰레드 별 같은 Connection을 쓸 수 있음?
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection = {}, class = {}", con, con.getClass());
        return con;
    }

}

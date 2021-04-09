package com.example.transactiontest

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.persistence.*

@Repository
class TestJdbcRepository(val jdbcTemplate: JdbcTemplate) {
    fun truncate() {
        jdbcTemplate.update("truncate test")
    }

    fun findById(id: Int): Test? {
        return jdbcTemplate.queryForObject("""
            select id, content from test where id = ?
        """, { rs: ResultSet, _: Int ->
            Test(rs.getInt("id"), rs.getString("content"))
             }, id)
    }

    fun update(test: Test) {
        jdbcTemplate.update("""
            update test set content = ? where id = ?
        """, test.content, test.id)

    }

    fun deleteById(id: Int) {
        jdbcTemplate.update("""
            delete from test where id = ?
        """, id)
    }

    fun insert() {
        jdbcTemplate.update("""
            insert into test(content) values('inserted from JdbcTemplate') 
            """
        )
    }
}

@Repository
interface TestJpaRepository : JpaRepository<Test, Int>

@Entity
@Table(name = "test")
data class Test(
        @Id
        val id: Int?,
        @Column(nullable = false)
        val content: String
)

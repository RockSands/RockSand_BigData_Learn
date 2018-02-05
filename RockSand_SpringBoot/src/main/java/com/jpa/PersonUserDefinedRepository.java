package com.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自定义
 * 
 * @author Administrator
 *
 */
public class PersonUserDefinedRepository implements Repository<Person, String> {
    
    /**
     * 查询所有
     * @return
     */
    @Query("SELECT * FROM PERSON")
    public List<Person> queryAll() {
	return null;
    }
    
    /**
     * 查询ID,参数?1 表示第一个参数,第二个为?2
     * @return
     */
    @Query("SELECT * FROM PERSON WHERE UUID = ?1")
    public Person queryByUUID(String uuid) {
	return null;
    }
    
    /**
     * 查询Name,参数:name 对应参数为name的参数
     * @return
     */
    @Query("SELECT * FROM PERSON WHERE NAME = :name")
    public List<Person> queryByName(@Param("name")String name) {
	return null;
    }
    
    /**
     * 更新
     * @Modifying 标记为更新操作
     * @Transactional 表明事物
     * @Query 书写SQL
     * @return
     */
    @Modifying
    @Transactional
    @Query("UPDATE PERSON AGE = :age WHERE UUID = :uuid")
    public List<Person> updatePersonAge(@Param("age")int age,@Param("uuid")String uuid) {
	return null;
    }
}

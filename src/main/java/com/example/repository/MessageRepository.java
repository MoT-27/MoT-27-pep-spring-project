package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

    @Query("SELECT m FROM Message m WHERE m.postedBy = :accountId")
    List<Message> findMessagesByAccountId (@Param("accountId") int accountId);

    @Modifying
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.messageId = :messageId")
    int updateMessageById(@Param("messageText") String messageText, @Param("messageId") Integer messageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.messageId = :messageId")
    int deleteMessageById(@Param("messageId") Integer messageId);

}

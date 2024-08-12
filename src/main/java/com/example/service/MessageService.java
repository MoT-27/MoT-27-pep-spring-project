package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    
    @Autowired
    public MessageService (MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }


    // get all messages
    public List<Message> getMessageList() {
        return (List<Message>) messageRepository.findAll();
    }


    // get message by ID
    public Message getMessage (Integer messageId) throws ResourceNotFoundException{
        return messageRepository.findById(messageId).orElse(null);
    }

    
    // delete message by ID
    public int deleteMessageById (Integer messageId){
        return messageRepository.deleteMessageById(messageId);
    }


    // patch/update a message by ID
    public int updateMessageById (Integer messageId, Message message) throws ResourceNotFoundException{
        Message message1 = messageRepository.findById(messageId).orElseThrow(() -> new ResourceNotFoundException(messageId + " not found.  Please try again."));
        message.setMessageText(message.getMessageText());
        messageRepository.save(message1);
        return 1;
    }

    
    // check existing message by ID
    public boolean existingMessage(Integer messageId){
        return messageRepository.existsById(messageId);
    }


    // create a new message
    public void addMessage(Message message){
        messageRepository.save(message);
    }


    // get all messages by a user
    public List<Message> getMessageByUser(Integer accountId) {
        List<Message> userMessages = messageRepository.findMessagesByAccountId(accountId);
        return userMessages;
    }


}

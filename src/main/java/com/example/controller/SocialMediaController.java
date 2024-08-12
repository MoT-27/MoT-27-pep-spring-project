package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@Controller
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;


    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountservice){
        this.messageService = messageService;
        this.accountService = accountservice;
    }


    // get list of messages
    @RequestMapping("messages")
    public @ResponseBody List<Message> getMessageList(){
        return messageService.getMessageList();
    }


    // get message by Id
    @GetMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> findMessageById(@PathVariable Integer messageId){
        Message message = messageService.getMessage(messageId);
        if (message == null || message.getMessageText() == null || message.getMessageText().isEmpty()){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(message);
    }


    // get messages by user
    @GetMapping("accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> findMessagesByUser(@PathVariable Integer accountId){
        List<Message> userMessageList = messageService.getMessageByUser(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(userMessageList);
    }


    // delete message by Id
    @DeleteMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId){
        Message message = messageService.getMessage(messageId);
        if(message != null){
            int deletedRows = messageService.deleteMessageById(messageId);
            return ResponseEntity.ok().body(deletedRows);
        } else {
            return ResponseEntity.ok().build();
        }
    }


    // patch/update message by Id
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody Message message){
        
        if (message.getMessageText() == null || message.getMessageText().isEmpty() || message.getMessageText().length() > 255){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!messageService.existingMessage(messageId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        int updatedRows = messageService.updateMessageById(messageId, message);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRows);
    }


    // post new message
    @PostMapping("messages")
    public @ResponseBody ResponseEntity<Message> addNewMessage(@RequestBody Message newMessage){
        boolean existingUser = accountService.existingAccount(newMessage.getPostedBy());
        if(newMessage.getMessageText().isBlank() || 
            newMessage.getMessageText().length() > 255 || 
            !existingUser){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newMessage);
        }
        messageService.addMessage(newMessage);
        return ResponseEntity.status(HttpStatus.OK).body(newMessage);
    }


    // login account
    @PostMapping("login")
    public @ResponseBody ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        try {
            Account loggedAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(loggedAccount);
        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    // register account
    @PostMapping("register")
    public ResponseEntity<String> registerAccount(@RequestBody Account account){
        if (account.getUsername().isBlank() || account.getPassword().length() < 4){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        } else if (accountService.existingAccountByUsername(account.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
        accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(account + " was created.");
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody String handleUnauthorized(AuthenticationException e){
        return e.getMessage();
    }

}

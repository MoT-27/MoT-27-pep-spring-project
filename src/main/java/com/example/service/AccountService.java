package com.example.service;

import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private MessageService messageService;


    @Autowired
    public AccountService (MessageService messageService, AccountRepository accountRepository){
        this.accountRepository = accountRepository;
        this.messageService = messageService;
    }


    // register new account
    public void addAccount (Account newAccount) {
        accountRepository.save(newAccount);
    }


    // login to account
    public Account loginAccount (String username, String password) throws AuthenticationException{
        Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
        if (account.isPresent()){
            return account.get();
        } else {
            throw new AuthenticationException("Invalid username or password.");
        }
    }


    // checking existing account by Id
    public boolean existingAccount (Integer accountId){
        return accountRepository.findById(accountId).isPresent();
    }


    // checking existing account by username
    public boolean existingAccountByUsername (String username){
        return accountRepository.findByUsername(username).isPresent();
    }


}

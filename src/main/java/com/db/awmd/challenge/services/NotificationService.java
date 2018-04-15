package com.db.awmd.challenge.services;

import com.db.awmd.challenge.domain.Account;

public interface NotificationService {

  void notifyAboutTransfer(Account account, String transferDescription);
}

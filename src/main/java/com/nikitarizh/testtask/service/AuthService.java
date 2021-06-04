package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.entity.User;

public interface AuthService {
    User getAuthenticatedUser();
}

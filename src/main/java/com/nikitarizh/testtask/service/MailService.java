package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;

import java.util.Collection;

public interface MailService {

    void sendBuyNotification(User to);

    void sendProductUpdateNotification(User to, Product oldProduct, ProductUpdateDTO productUpdateDTO);
}

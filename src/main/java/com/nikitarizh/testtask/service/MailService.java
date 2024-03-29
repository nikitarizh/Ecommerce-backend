package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.User;

public interface MailService {

    void sendBuyNotification(User to);

    void sendProductUpdateNotification(User to, ProductFullDTO oldProduct, ProductUpdateDTO productUpdateDTO);

    void sendProductDeleteNotification(User to, ProductFullDTO product);
}

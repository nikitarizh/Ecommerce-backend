package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendBuyNotification(User to) {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("You have bought:").append('\n');
        for (Product product : to.getOrderedProducts()) {
            textBuilder.append(product).append('\n');
        }

        sendMail(to, "Successful purchase", textBuilder.toString());
    }

    @Override
    public void sendProductUpdateNotification(User to, Product oldProduct, ProductUpdateDTO productUpdateDTO) {
        StringBuilder textBuilder = new StringBuilder();

        textBuilder.append("Product with id ").append(oldProduct.getId()).append(" was changed").append('\n');
        textBuilder.append("-----------").append('\n');
        textBuilder.append("Old description: ").append(oldProduct.getDescription()).append('\n');
        textBuilder.append("Old tags: ").append(oldProduct.getTags()).append('\n');
        textBuilder.append("-----------").append('\n');
        textBuilder.append("New description: ").append(productUpdateDTO.getDescription()).append('\n');
        textBuilder.append("New tags: ").append(productUpdateDTO.getTags()).append('\n');

        sendMail(to, "Product that is in your cart was changed", textBuilder.toString());
    }

    void sendMail(User to, String subj, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("noreply@baeldung.com");
        message.setTo(to.getEmail());
        message.setSubject(subj);
        message.setText(text);

        javaMailSender.send(message);
    }
}

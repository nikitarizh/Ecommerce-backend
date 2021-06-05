package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.mapper.ProductMapper;
import com.nikitarizh.testtask.service.MailService;
import com.nikitarizh.testtask.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final TagService tagService;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendBuyNotification(User to) {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("You have bought:").append('\n');
        for (Product product : to.getOrderedProducts()) {
            textBuilder.append(ProductMapper.PRODUCT_MAPPER.mapToFullDTO(product)).append('\n');
        }

        sendMail(to, "Successful purchase", textBuilder.toString());
    }

    @Override
    public void sendProductUpdateNotification(User to, ProductFullDTO oldProduct, ProductUpdateDTO productUpdateDTO) {
        StringBuilder textBuilder = new StringBuilder();

        textBuilder.append("Product with id ").append(oldProduct.getId()).append(" was changed").append('\n');
        textBuilder.append("-----------").append('\n');
        textBuilder.append("Old description: ").append(oldProduct.getDescription()).append('\n');
        textBuilder.append("Old tags: ").append(oldProduct.getTags()).append('\n');
        textBuilder.append("-----------").append('\n');
        textBuilder.append("New description: ").append(productUpdateDTO.getDescription()).append('\n');
        textBuilder.append("New tags: ").append(tagService.findAllByIds(productUpdateDTO.getTagIds())).append('\n');

        sendMail(to, "Product that is in your cart was changed", textBuilder.toString());
    }

    @Override
    public void sendProductDeleteNotification(User to, ProductFullDTO product) {
        sendMail(to, "Product was deleted from your cart", "Product " + product + " was deleted from your cart because it was deleted from the catalogue");
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

package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.ProductIsInCartException;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.repository.ProductRepository;
import com.nikitarizh.testtask.service.MailService;
import com.nikitarizh.testtask.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;

@Service
public class ProductServiceImpl implements ProductService {

    private final MailService mailService;

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(MailService mailService, ProductRepository productRepository) {
        this.mailService = mailService;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductFullDTO findById(Integer id) throws ProductNotFoundException {
        Optional<Product> result = productRepository.findById(id);
        if (result.isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        return PRODUCT_MAPPER.mapToFullDTO(result.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<ProductFullDTO> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).map(PRODUCT_MAPPER::mapToFullDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductFullDTO create(ProductCreateDTO productCreateDTO) {
        Product newProduct = productRepository.save(PRODUCT_MAPPER.mapToEntity(productCreateDTO));
        return PRODUCT_MAPPER.mapToFullDTO(newProduct);
    }

    @Override
    @Transactional
    public ProductFullDTO update(ProductUpdateDTO productUpdateDTO, boolean force) {
        Product productToUpdate = productRepository.findById(productUpdateDTO.getId())
                .orElseThrow(() -> new ProductNotFoundException(productUpdateDTO.getId()));

        if (!force && productToUpdate.getOrderedBy().size() > 0) {
            throw new ProductIsInCartException(productToUpdate);
        } else {
            for (User buyer : productToUpdate.getOrderedBy()) {
                mailService.sendProductUpdateNotification(buyer, productToUpdate, productUpdateDTO);
            }
        }

        productToUpdate.setDescription(productUpdateDTO.getDescription());
        productToUpdate.setTags(productUpdateDTO.getTags());

        return PRODUCT_MAPPER.mapToFullDTO(productToUpdate);
    }

    @Override
    @Transactional
    public void delete(Integer productId) {
        productRepository.deleteById(productId);
    }
}

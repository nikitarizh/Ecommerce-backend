package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductDeleteDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.ProductAlreadyInCartException;
import com.nikitarizh.testtask.exception.ProductIsInCartException;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.mapper.ProductMapper;
import com.nikitarizh.testtask.repository.ProductRepository;
import com.nikitarizh.testtask.service.MailService;
import com.nikitarizh.testtask.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final MailService mailService;

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(MailService mailService, ProductRepository productRepository) {
        this.mailService = mailService;
        this.productRepository = productRepository;
    }

    @Override
    public ProductFullDTO findById(Integer id) throws ProductNotFoundException {
        Optional<Product> result = productRepository.findById(id);
        if (result.isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        return PRODUCT_MAPPER.mapToFullDTO(result.get());
    }

    @Override
    public Iterable<ProductFullDTO> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).map(PRODUCT_MAPPER::mapToFullDTO).collect(Collectors.toList());
    }

    @Override
    public ProductFullDTO create(ProductCreateDTO productCreateDTO) {
        Product newProduct = productRepository.save(PRODUCT_MAPPER.mapToEntity(productCreateDTO));
        return PRODUCT_MAPPER.mapToFullDTO(newProduct);
    }

    @Override
    public ProductFullDTO update(ProductUpdateDTO productUpdateDTO, boolean force) {
        Product productToUpdate = productRepository.findById(productUpdateDTO.getId())
                .orElseThrow(() -> new ProductNotFoundException(productUpdateDTO.getId()));

        if (!force && productToUpdate.getOrderedBy().size() > 0) {
            throw new ProductIsInCartException(productToUpdate);
        }
        else {
            for (User buyer : productToUpdate.getOrderedBy()) {
                mailService.sendProductUpdateNotification(buyer, productToUpdate, productUpdateDTO);
            }
        }

        productToUpdate.setDescription(productUpdateDTO.getDescription());
        productToUpdate.setTags(productUpdateDTO.getTags());

        return PRODUCT_MAPPER.mapToFullDTO(productToUpdate);
    }

    @Override
    public void delete(ProductDeleteDTO productDeleteDTO) {
        productRepository.deleteById(productDeleteDTO.getId());
    }
}

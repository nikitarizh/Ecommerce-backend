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
import com.nikitarizh.testtask.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final MailService mailService;
    private final TagService tagService;

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductFullDTO findById(Integer id) throws ProductNotFoundException {
        Product result = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return PRODUCT_MAPPER.mapToFullDTO(result);
    }

    @Override
    @Transactional
    public ProductFullDTO create(ProductCreateDTO productCreateDTO) {
        Product newProduct = productRepository.save(PRODUCT_MAPPER.mapToEntity(productCreateDTO));
        newProduct.setTags(tagService.findAllByIds(productCreateDTO.getTagIds()));
        return PRODUCT_MAPPER.mapToFullDTO(newProduct);
    }

    @Override
    @Transactional
    public ProductFullDTO update(ProductUpdateDTO productUpdateDTO, boolean force) {
        Product productToUpdate = productRepository.findById(productUpdateDTO.getId())
                .orElseThrow(() -> new ProductNotFoundException(productUpdateDTO.getId()));

        if (!force && productToUpdate.getOrderedBy().size() > 0) {
            throw new ProductIsInCartException(productToUpdate);
        }

        for (User buyer : productToUpdate.getOrderedBy()) {
            mailService.sendProductUpdateNotification(buyer, PRODUCT_MAPPER.mapToFullDTO(productToUpdate), productUpdateDTO);
        }

        productToUpdate.setDescription(productUpdateDTO.getDescription());
        productToUpdate.setTags(tagService.findAllByIds(productUpdateDTO.getTagIds()));

        return PRODUCT_MAPPER.mapToFullDTO(productToUpdate);
    }

    @Override
    public List<ProductFullDTO> search(String description, List<Integer> tagIds) {
        List<ProductFullDTO> output = productRepository.search(description, tagIds)
                .stream()
                .map(PRODUCT_MAPPER::mapToFullDTO)
                .collect(Collectors.toList());

        if ((description != null || tagIds != null) && output.size() == 0) {
            throw new ProductNotFoundException(description, tagIds);
        }

        return output;
    }

    @Override
    @Transactional
    public void delete(Integer productId, boolean force) {
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!force && productToDelete.getOrderedBy().size() > 0) {
            throw new ProductIsInCartException(productToDelete);
        }

        for (User buyer : productToDelete.getOrderedBy()) {
            mailService.sendProductDeleteNotification(buyer, PRODUCT_MAPPER.mapToFullDTO(productToDelete));
            buyer.getOrderedProducts().remove(productToDelete);
        }

        productToDelete.getTags().clear();

        productRepository.deleteById(productId);
    }
}

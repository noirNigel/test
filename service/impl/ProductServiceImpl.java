package org.example.demomanagementsystemcproject.service.impl;

import org.example.demomanagementsystemcproject.dto.*;
import org.example.demomanagementsystemcproject.entity.CategoryEntity;
import org.example.demomanagementsystemcproject.entity.ProductEntity;
import org.example.demomanagementsystemcproject.repo.CategoryRepository;
import org.example.demomanagementsystemcproject.repo.ProductRepository;
import org.example.demomanagementsystemcproject.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final int AUTO_OFF_SHELF_THRESHOLD = 5;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<ProductDTO> getProducts(ProductQueryDTO query) {
        Pageable pageable = PageRequest.of(query.getPage() - 1, query.getSize());

        Specification<ProductEntity> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getName() != null && !query.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + query.getName() + "%"));
            }

            if (query.getSku() != null && !query.getSku().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("sku"), "%" + query.getSku() + "%"));
            }

            if (query.getCategoryId() != null) {
                List<Long> categoryIds = resolveCategoryIds(query.getCategoryId());
                if (!categoryIds.isEmpty()) {
                    predicates.add(root.get("categoryId").in(categoryIds));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("categoryId"), query.getCategoryId()));
                }
            }

            if (query.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), query.getStatus()));
            }

            if (query.getLowStock() != null && query.getLowStock() == 1) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stock"), root.get("warningThreshold")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ProductEntity> page = productRepository.findAll(spec, pageable);
        return mapWithCategoryNames(page);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        return convertToDTO(entity, null);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        // 检查SKU是否重复
        ProductEntity existingProduct = productRepository.findBySku(productDTO.getSku());
        if (existingProduct != null) {
            throw new RuntimeException("SKU已存在");
        }

        ProductEntity entity = new ProductEntity();
        applyDtoToEntity(productDTO, entity);
        ProductEntity saved = productRepository.save(entity);
        return convertToDTO(saved, null);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 检查SKU是否重复（排除自己）
        ProductEntity skuProduct = productRepository.findBySku(productDTO.getSku());
        if (skuProduct != null && !skuProduct.getId().equals(id)) {
            throw new RuntimeException("SKU已存在");
        }

        applyDtoToEntity(productDTO, entity);
        ProductEntity saved = productRepository.save(entity);
        return convertToDTO(saved, null);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("商品不存在");
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        entity.setStatus(status);
        enforceAutoOffShelf(entity);
        productRepository.save(entity);
    }

    @Override
    @Transactional
    public void batchUpdateStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            updateStatus(id, status);
        }
    }

    @Override
    @Transactional
    public void updateStock(Long id, Integer stock) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        entity.setStock(stock);
        enforceAutoOffShelf(entity);
        productRepository.save(entity);
    }

    @Override
    @Transactional
    public void batchUpdateStock(Map<Long, Integer> stockMap) {
        for (Map.Entry<Long, Integer> entry : stockMap.entrySet()) {
            updateStock(entry.getKey(), entry.getValue());
        }
    }

    @Override
    @Transactional
    public void setWarningThreshold(Long id, Integer threshold) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        entity.setWarningThreshold(threshold);
        productRepository.save(entity);
    }

    @Override
    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(entity -> convertToDTO(entity, null))
                .collect(Collectors.toList());
    }

    private void applyDtoToEntity(ProductDTO dto, ProductEntity entity) {
        entity.setName(dto.getName());
        entity.setSku(dto.getSku());
        entity.setPrice(dto.getPrice());
        entity.setCost(dto.getCost());
        entity.setStock(dto.getStock());
        entity.setWarningThreshold(dto.getWarningThreshold());
        entity.setStatus(dto.getStatus());
        entity.setCategoryId(dto.getCategoryId());
        entity.setCategoryPath(dto.getCategoryPath());
        entity.setRecipe(dto.getRecipe());
        entity.setImage(compressImageIfNeeded(dto.getImage()));
        entity.setDescription(dto.getDescription());
        entity.setImages(dto.getImages());
        enforceAutoOffShelf(entity);
    }

    private ProductDTO convertToDTO(ProductEntity entity, Map<Long, String> categoryNameMap) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(entity, dto);
        Long categoryId = entity.getCategoryId();
        if (categoryId != null) {
            String categoryName = categoryNameMap != null
                    ? categoryNameMap.get(categoryId)
                    : null;
            if (categoryName == null) {
                categoryName = categoryRepository.findById(categoryId)
                        .map(CategoryEntity::getName)
                        .orElse(null);
            }
            dto.setCategoryName(categoryName);
        }
        return dto;
    }

    private Page<ProductDTO> mapWithCategoryNames(Page<ProductEntity> page) {
        List<Long> categoryIds = page.getContent().stream()
                .map(ProductEntity::getCategoryId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> categoryNameMap = categoryIds.isEmpty()
                ? Map.of()
                : categoryRepository.findAllById(categoryIds).stream()
                    .collect(Collectors.toMap(CategoryEntity::getId, CategoryEntity::getName));
        return page.map(entity -> convertToDTO(entity, categoryNameMap));
    }

    private List<Long> resolveCategoryIds(Long categoryId) {
        List<CategoryEntity> children = categoryRepository.findByParentId(categoryId);
        if (children == null || children.isEmpty()) {
            return List.of(categoryId);
        }
        return children.stream()
                .map(CategoryEntity::getId)
                .collect(Collectors.toList());
    }

    private void enforceAutoOffShelf(ProductEntity entity) {
        Integer stock = entity.getStock();
        if (stock != null && stock < AUTO_OFF_SHELF_THRESHOLD) {
            entity.setStatus(0);
        }
    }

    /**
     * Compress base64-encoded images to a reasonable size while keeping non-image strings untouched.
     * This avoids rejecting large uploads while preventing oversized payloads from being persisted as-is.
     */
    private String compressImageIfNeeded(String imageData) {
        if (imageData == null || imageData.isBlank()) {
            return imageData;
        }

        String prefix = null;
        String format = "jpeg";
        String payload = imageData;

        // data URI handling (e.g., data:image/png;base64,xxxx)
        if (imageData.startsWith("data:image")) {
            int commaIndex = imageData.indexOf(',');
            int slashIndex = imageData.indexOf('/');
            int semicolonIndex = imageData.indexOf(';');
            if (commaIndex > 0) {
                prefix = imageData.substring(0, commaIndex + 1);
                payload = imageData.substring(commaIndex + 1);
            }
            if (slashIndex > 0 && semicolonIndex > slashIndex) {
                format = imageData.substring(slashIndex + 1, semicolonIndex);
            }
        }

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(payload);
        } catch (IllegalArgumentException e) {
            // Not base64 – return original string untouched.
            return imageData;
        }

        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(decoded));
            if (original == null) {
                return imageData;
            }

            BufferedImage processed = resizeIfNeeded(original, 1024);

            String encoded = encodeImage(processed, format, 0.8f, prefix);

            // If still large for TEXT/LONGTEXT safe defaults, perform a second-pass aggressive shrink.
            final int maxLength = 60000; // characters
            if (encoded.length() > maxLength) {
                BufferedImage tighter = resizeIfNeeded(processed, 720);
                encoded = encodeImage(tighter, "jpeg", 0.7f, "data:image/jpeg;base64,");
            }

            return encoded;
        } catch (Exception e) {
            // If anything goes wrong during compression, fall back to the original string.
            return imageData;
        }
    }

    private BufferedImage resizeIfNeeded(BufferedImage original, int targetMaxEdge) {
        int width = original.getWidth();
        int height = original.getHeight();
        int maxEdge = Math.max(width, height);

        double scale = maxEdge > targetMaxEdge ? (double) targetMaxEdge / maxEdge : 1.0d;
        int targetWidth = (int) Math.round(width * scale);
        int targetHeight = (int) Math.round(height * scale);

        if (scale >= 0.999d) {
            return original;
        }

        BufferedImage processed = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = processed.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return processed;
    }

    private String encodeImage(BufferedImage image, String sourceFormat, float quality, String prefix) throws Exception {
        String format = sourceFormat;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (!"png".equalsIgnoreCase(format)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(Math.max(0.45f, Math.min(1.0f, quality)));
            }
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }
            format = "jpeg";
        } else {
            ImageIO.write(image, "png", baos);
        }

        String compressedBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        String dataPrefix = prefix;
        if (dataPrefix == null || !dataPrefix.startsWith("data:image")) {
            dataPrefix = "data:image/" + format + ";base64,";
        }
        return dataPrefix + compressedBase64;
    }
}

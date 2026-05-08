package com.shopflow.service;

import com.shopflow.dto.request.CartItemRequest;
import com.shopflow.dto.response.CartItemResponse;
import com.shopflow.dto.response.CartResponse;
import com.shopflow.dto.response.CouponResponse;
import com.shopflow.entity.*;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final CouponRepository couponRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository, ProductVariantRepository variantRepository,
                       CouponRepository couponRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.couponRepository = couponRepository;
    }

    private static final BigDecimal FRAIS_LIVRAISON = BigDecimal.valueOf(5.99);

    @Transactional
    public CartResponse getOrCreateCart(User customer) {
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().customer(customer).build();
                    return cartRepository.save(newCart);
                });
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse addItem(User customer, CartItemRequest request) {
        Cart cart = getOrCreateCartEntity(customer);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> ShopFlowException.notFound("Produit non trouvé"));

        if (!product.isActif()) {
            throw ShopFlowException.badRequest("Ce produit n'est plus disponible");
        }

        ProductVariant variant = null;
        if (request.getVariantId() != null) {
            variant = variantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> ShopFlowException.notFound("Variante non trouvée"));
        }

        // Vérifier le stock
        int stockDisponible = getStockDisponible(product, variant);
        if (stockDisponible < request.getQuantite()) {
            throw ShopFlowException.badRequest("Stock insuffisant. Disponible : " + stockDisponible);
        }

        // Chercher si l'article existe déjà dans le panier
        CartItem existingItem = findExistingCartItem(cart, product, variant);

        if (existingItem != null) {
            int newQty = existingItem.getQuantite() + request.getQuantite();
            if (stockDisponible < newQty) {
                throw ShopFlowException.badRequest("Stock insuffisant. Disponible : " + stockDisponible);
            }
            existingItem.setQuantite(newQty);
            cartItemRepository.save(existingItem);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .variant(variant)
                    .quantite(request.getQuantite())
                    .build();
            cartItemRepository.save(item);
            cart.getLignes().add(item);
        }

        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse updateItem(User customer, Long itemId, Integer quantite) {
        Cart cart = getOrCreateCartEntity(customer);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> ShopFlowException.notFound("Article non trouvé"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw ShopFlowException.forbidden("Cet article n'appartient pas à votre panier");
        }

        if (quantite <= 0) {
            cartItemRepository.delete(item);
            cart.getLignes().remove(item);
        } else {
            int stock = getStockDisponible(item.getProduct(), item.getVariant());
            if (stock < quantite) {
                throw ShopFlowException.badRequest("Stock insuffisant. Disponible : " + stock);
            }
            item.setQuantite(quantite);
            cartItemRepository.save(item);
        }

        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(User customer, Long itemId) {
        Cart cart = getOrCreateCartEntity(customer);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> ShopFlowException.notFound("Article non trouvé"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw ShopFlowException.forbidden("Cet article n'appartient pas à votre panier");
        }

        cartItemRepository.delete(item);
        cart.getLignes().remove(item);
        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse applyCoupon(User customer, String code) {
        Cart cart = getOrCreateCartEntity(customer);
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> ShopFlowException.notFound("Code promo invalide"));

        validateCoupon(coupon);
        cart.setCoupon(coupon);
        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse removeCoupon(User customer) {
        Cart cart = getOrCreateCartEntity(customer);
        cart.setCoupon(null);
        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Transactional
    public void clearCart(Cart cart) {
        cart.getLignes().clear();
        cart.setCoupon(null);
        cartRepository.save(cart);
    }

    // ---- Helpers ----

    private Cart getOrCreateCartEntity(User customer) {
        return cartRepository.findByCustomer(customer)
                .orElseGet(() -> cartRepository.save(Cart.builder().customer(customer).build()));
    }

    private CartItem findExistingCartItem(Cart cart, Product product, ProductVariant variant) {
        if (variant != null) {
            return cartItemRepository.findByCartIdAndProductIdAndVariantId(
                    cart.getId(), product.getId(), variant.getId()).orElse(null);
        }
        return cartItemRepository.findByCartIdAndProductIdAndVariantIsNull(
                cart.getId(), product.getId()).orElse(null);
    }

    private int getStockDisponible(Product product, ProductVariant variant) {
        if (variant != null) {
            return product.getStock() + variant.getStockSupplementaire();
        }
        return product.getStock();
    }

    public void validateCoupon(Coupon coupon) {
        if (!coupon.isActif()) {
            throw ShopFlowException.badRequest("Ce code promo n'est plus actif");
        }
        if (coupon.getDateExpiration() != null && coupon.getDateExpiration().isBefore(java.time.LocalDateTime.now())) {
            throw ShopFlowException.badRequest("Ce code promo a expiré");
        }
        if (coupon.getUsagesActuels() >= coupon.getUsagesMax()) {
            throw ShopFlowException.badRequest("Ce code promo a atteint son nombre maximum d'utilisations");
        }
    }

    public CartResponse toCartResponse(Cart cart) {
        // Reload cart items
        List<CartItem> items = cart.getLignes() != null ? cart.getLignes() : new ArrayList<>();

        List<CartItemResponse> itemResponses = items.stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());

        BigDecimal sousTotal = itemResponses.stream()
                .map(CartItemResponse::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remise = BigDecimal.ZERO;
        if (cart.getCoupon() != null) {
            remise = calculateRemise(sousTotal, cart.getCoupon());
        }

        BigDecimal fraisLivraison = items.isEmpty() ? BigDecimal.ZERO : FRAIS_LIVRAISON;
        BigDecimal totalTTC = sousTotal.subtract(remise).add(fraisLivraison);
        if (totalTTC.compareTo(BigDecimal.ZERO) < 0) totalTTC = BigDecimal.ZERO;

        CouponResponse couponResponse = cart.getCoupon() != null ? toCouponResponse(cart.getCoupon()) : null;

        return CartResponse.builder()
                .id(cart.getId())
                .lignes(itemResponses)
                .coupon(couponResponse)
                .sousTotal(sousTotal.setScale(2, RoundingMode.HALF_UP))
                .remiseCoupon(remise.setScale(2, RoundingMode.HALF_UP))
                .fraisLivraison(fraisLivraison)
                .totalTTC(totalTTC.setScale(2, RoundingMode.HALF_UP))
                .dateModification(cart.getDateModification())
                .build();
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        Product product = item.getProduct();
        BigDecimal prix = product.isEnPromotion() ? product.getPrixPromo() : product.getPrix();
        if (item.getVariant() != null && item.getVariant().getPrixDelta() != null) {
            prix = prix.add(item.getVariant().getPrixDelta());
        }

        String variantInfo = null;
        if (item.getVariant() != null) {
            variantInfo = item.getVariant().getAttribut() + ": " + item.getVariant().getValeur();
        }

        String image = product.getImages() != null && !product.getImages().isEmpty()
                ? product.getImages().get(0) : null;

        return CartItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .nomProduit(product.getNom())
                .imageProduit(image)
                .prixUnitaire(prix)
                .variantId(item.getVariant() != null ? item.getVariant().getId() : null)
                .variantInfo(variantInfo)
                .quantite(item.getQuantite())
                .sousTotal(prix.multiply(BigDecimal.valueOf(item.getQuantite())).setScale(2, RoundingMode.HALF_UP))
                .stockDisponible(getStockDisponible(product, item.getVariant()))
                .build();
    }

    private BigDecimal calculateRemise(BigDecimal sousTotal, Coupon coupon) {
        if (coupon.getType() == CouponType.PERCENT) {
            return sousTotal.multiply(coupon.getValeur()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            return coupon.getValeur().min(sousTotal);
        }
    }

    public BigDecimal calculateRemiseForOrder(BigDecimal sousTotal, Coupon coupon) {
        return calculateRemise(sousTotal, coupon);
    }

    private CouponResponse toCouponResponse(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .type(coupon.getType())
                .valeur(coupon.getValeur())
                .dateExpiration(coupon.getDateExpiration())
                .usagesMax(coupon.getUsagesMax())
                .usagesActuels(coupon.getUsagesActuels())
                .actif(coupon.isActif())
                .build();
    }
}

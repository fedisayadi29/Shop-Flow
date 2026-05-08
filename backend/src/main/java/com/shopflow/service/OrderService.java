package com.shopflow.service;

import com.shopflow.dto.request.OrderRequest;
import com.shopflow.dto.response.*;
import com.shopflow.entity.*;
import com.shopflow.exception.ShopFlowException;
import com.shopflow.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                        AddressRepository addressRepository, ProductRepository productRepository,
                        CouponRepository couponRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.cartService = cartService;
    }

    private static final BigDecimal FRAIS_LIVRAISON = BigDecimal.valueOf(5.99);

    @Transactional
    public OrderResponse createOrder(User customer, OrderRequest request) {
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> ShopFlowException.badRequest("Panier vide"));

        if (cart.getLignes() == null || cart.getLignes().isEmpty()) {
            throw ShopFlowException.badRequest("Votre panier est vide");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> ShopFlowException.notFound("Adresse non trouvée"));

        if (!address.getUser().getId().equals(customer.getId())) {
            throw ShopFlowException.forbidden("Cette adresse ne vous appartient pas");
        }

        // Vérification finale du stock et calcul des totaux
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal sousTotal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getLignes()) {
            Product product = cartItem.getProduct();
            ProductVariant variant = cartItem.getVariant();

            // Vérifier le stock
            int stockDisponible = variant != null
                    ? product.getStock() + variant.getStockSupplementaire()
                    : product.getStock();

            if (stockDisponible < cartItem.getQuantite()) {
                throw ShopFlowException.badRequest(
                        "Stock insuffisant pour : " + product.getNom() + ". Disponible : " + stockDisponible);
            }

            BigDecimal prixUnitaire = product.isEnPromotion() ? product.getPrixPromo() : product.getPrix();
            if (variant != null && variant.getPrixDelta() != null) {
                prixUnitaire = prixUnitaire.add(variant.getPrixDelta());
            }

            String image = product.getImages() != null && !product.getImages().isEmpty()
                    ? product.getImages().get(0) : null;

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .variant(variant)
                    .quantite(cartItem.getQuantite())
                    .prixUnitaire(prixUnitaire)
                    .nomProduit(product.getNom())
                    .build();
            orderItems.add(orderItem);

            sousTotal = sousTotal.add(prixUnitaire.multiply(BigDecimal.valueOf(cartItem.getQuantite())));

            // Décrémenter le stock
            if (variant != null) {
                int newVariantStock = variant.getStockSupplementaire() - cartItem.getQuantite();
                if (newVariantStock < 0) {
                    product.setStock(product.getStock() + newVariantStock);
                    variant.setStockSupplementaire(0);
                } else {
                    variant.setStockSupplementaire(newVariantStock);
                }
            } else {
                product.setStock(product.getStock() - cartItem.getQuantite());
            }
            product.setNombreVentes(product.getNombreVentes() + cartItem.getQuantite());
            productRepository.save(product);
        }

        // Calculer la remise coupon
        BigDecimal remiseCoupon = BigDecimal.ZERO;
        Coupon coupon = cart.getCoupon();
        if (coupon != null) {
            cartService.validateCoupon(coupon);
            remiseCoupon = cartService.calculateRemiseForOrder(sousTotal, coupon);
            coupon.setUsagesActuels(coupon.getUsagesActuels() + 1);
            couponRepository.save(coupon);
        }

        BigDecimal totalTTC = sousTotal.subtract(remiseCoupon).add(FRAIS_LIVRAISON);
        if (totalTTC.compareTo(BigDecimal.ZERO) < 0) totalTTC = BigDecimal.ZERO;

        // Créer la commande
        Order order = Order.builder()
                .customer(customer)
                .statut(OrderStatus.PENDING)
                .numeroCommande(generateNumeroCommande())
                .adresseLivraisonRue(address.getRue())
                .adresseLivraisonVille(address.getVille())
                .adresseLivraisonCodePostal(address.getCodePostal())
                .adresseLivraisonPays(address.getPays())
                .sousTotal(sousTotal.setScale(2, RoundingMode.HALF_UP))
                .fraisLivraison(FRAIS_LIVRAISON)
                .remiseCoupon(remiseCoupon.setScale(2, RoundingMode.HALF_UP))
                .totalTTC(totalTTC.setScale(2, RoundingMode.HALF_UP))
                .coupon(coupon)
                .isNew(true)
                .build();

        order = orderRepository.save(order);

        // Associer les items à la commande
        Order finalOrder = order;
        orderItems.forEach(item -> item.setOrder(finalOrder));
        order.setLignes(orderItems);
        order = orderRepository.save(order);

        // Vider le panier
        cartService.clearCart(cart);

        return toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id, User currentUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Commande non trouvée : " + id));

        // Vérifier les droits d'accès
        if (currentUser.getRole() == Role.CUSTOMER && !order.getCustomer().getId().equals(currentUser.getId())) {
            throw ShopFlowException.forbidden("Accès refusé à cette commande");
        }

        return toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getMyOrders(User customer, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCommande"));
        Page<Order> orders = orderRepository.findByCustomer(customer, pageable);
        return toPageResponse(orders.map(this::toOrderResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getAllOrders(int page, int size, Long sellerId, OrderStatus statut) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCommande"));
        Page<Order> orders;
        
        if (sellerId != null || statut != null) {
            Specification<Order> spec = (root, query, cb) -> {
                var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();
                
                if (sellerId != null) {
                    var lignes = root.join("lignes");
                    var product = lignes.join("product");
                    var seller = product.join("seller");
                    predicates.add(cb.equal(seller.get("id"), sellerId));
                }
                
                if (statut != null) {
                    predicates.add(cb.equal(root.get("statut"), statut));
                }
                
                return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
            };
            orders = orderRepository.findAll(spec, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }
        
        return toPageResponse(orders.map(this::toOrderResponse));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus newStatus, User currentUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Commande non trouvée : " + id));

        validateStatusTransition(order.getStatut(), newStatus, currentUser);

        order.setStatut(newStatus);
        order.setNew(false);
        order = orderRepository.save(order);
        return toOrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long id, User customer) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ShopFlowException.notFound("Commande non trouvée : " + id));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw ShopFlowException.forbidden("Vous ne pouvez pas annuler cette commande");
        }

        if (order.getStatut() != OrderStatus.PENDING && order.getStatut() != OrderStatus.PAID) {
            throw ShopFlowException.badRequest(
                    "Impossible d'annuler une commande avec le statut : " + order.getStatut());
        }

        // Remettre le stock
        for (OrderItem item : order.getLignes()) {
            Product product = item.getProduct();
            if (item.getVariant() != null) {
                item.getVariant().setStockSupplementaire(
                        item.getVariant().getStockSupplementaire() + item.getQuantite());
            } else {
                product.setStock(product.getStock() + item.getQuantite());
            }
            product.setNombreVentes(Math.max(0, product.getNombreVentes() - item.getQuantite()));
            productRepository.save(product);
        }

        OrderStatus newStatus = order.getStatut() == OrderStatus.PAID
                ? OrderStatus.REFUNDED : OrderStatus.CANCELLED;
        order.setStatut(newStatus);
        order = orderRepository.save(order);
        return toOrderResponse(order);
    }

    // ---- Helpers ----

    private String generateNumeroCommande() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String random = String.format("%05d", new Random().nextInt(100000));
        return "ORD-" + year + "-" + random;
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next, User user) {
        // Logique de transition de statut
        boolean valid = switch (current) {
            case PENDING -> next == OrderStatus.PAID || next == OrderStatus.CANCELLED;
            case PAID -> next == OrderStatus.PROCESSING || next == OrderStatus.REFUNDED;
            case PROCESSING -> next == OrderStatus.SHIPPED;
            case SHIPPED -> next == OrderStatus.DELIVERED;
            default -> false;
        };

        if (!valid) {
            throw ShopFlowException.badRequest(
                    "Transition de statut invalide : " + current + " -> " + next);
        }
    }

    public OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getLignes() != null
                ? order.getLignes().stream().map(this::toOrderItemResponse).collect(Collectors.toList())
                : new ArrayList<>();

        CouponResponse couponResponse = null;
        if (order.getCoupon() != null) {
            Coupon c = order.getCoupon();
            couponResponse = CouponResponse.builder()
                    .id(c.getId()).code(c.getCode()).type(c.getType()).valeur(c.getValeur()).build();
        }

        return OrderResponse.builder()
                .id(order.getId())
                .numeroCommande(order.getNumeroCommande())
                .statut(order.getStatut())
                .adresseLivraisonRue(order.getAdresseLivraisonRue())
                .adresseLivraisonVille(order.getAdresseLivraisonVille())
                .adresseLivraisonCodePostal(order.getAdresseLivraisonCodePostal())
                .adresseLivraisonPays(order.getAdresseLivraisonPays())
                .sousTotal(order.getSousTotal())
                .fraisLivraison(order.getFraisLivraison())
                .remiseCoupon(order.getRemiseCoupon())
                .totalTTC(order.getTotalTTC())
                .dateCommande(order.getDateCommande())
                .isNew(order.isNew())
                .lignes(items)
                .coupon(couponResponse)
                .build();
    }

    private OrderItemResponse toOrderItemResponse(OrderItem item) {
        String image = item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty()
                ? item.getProduct().getImages().get(0) : null;
        String variantInfo = item.getVariant() != null
                ? item.getVariant().getAttribut() + ": " + item.getVariant().getValeur() : null;

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .nomProduit(item.getNomProduit())
                .imageProduit(image)
                .variantId(item.getVariant() != null ? item.getVariant().getId() : null)
                .variantInfo(variantInfo)
                .quantite(item.getQuantite())
                .prixUnitaire(item.getPrixUnitaire())
                .sousTotal(item.getPrixUnitaire().multiply(BigDecimal.valueOf(item.getQuantite()))
                        .setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    private <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }
}

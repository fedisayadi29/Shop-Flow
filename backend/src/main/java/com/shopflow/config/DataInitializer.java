package com.shopflow.config;

import com.shopflow.entity.*;
import com.shopflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            if (userRepository.count() > 0) return;

            log.info("Initialisation des données de démonstration...");

            if (userRepository.count() > 0) {
                log.info("Mise à jour des images des produits existants...");
                Map<String, List<String>> imageMap = Map.ofEntries(
                        Map.entry("Samsung Galaxy S23", List.of("https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800&q=80")),
                        Map.entry("iPhone 14 Pro", List.of("https://images.unsplash.com/photo-1512499617640-c2f9992c0aa0?w=800&q=80")),
                        Map.entry("Dell XPS 15", List.of("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80")),
                        Map.entry("iPad Air 2024", List.of("https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800&q=80")),
                        Map.entry("AirPods Pro 2", List.of("https://images.unsplash.com/photo-1574180045827-681f8a1a9622?w=800&q=80")),
                        Map.entry("Maillot Équipe de Tunisie 2024", List.of("https://images.unsplash.com/photo-1517649763962-0c623066013b?w=800&q=80")),
                        Map.entry("Robe Traditionnelle Tunisienne", List.of("https://images.unsplash.com/photo-1503341455253-b2e723bb3dbb?w=800&q=80")),
                        Map.entry("Jebba Homme Blanche", List.of("https://images.unsplash.com/photo-1521334884684-d80222895322?w=800&q=80")),
                        Map.entry("Baskets Nike Air Max", List.of("https://images.unsplash.com/photo-1519741493375-cdfb1d6fe09f?w=800&q=80")),
                        Map.entry("Motoculteur Professionnel", List.of("https://images.unsplash.com/photo-1500534623283-312aade485b7?w=800&q=80")),
                        Map.entry("Semences Tomates Tunisiennes", List.of("https://images.unsplash.com/photo-1572441710518-97f7053f22ee?w=800&q=80")),
                        Map.entry("Engrais Organique Bio 25kg", List.of("https://images.unsplash.com/photo-1517430816045-df4b7de1ca71?w=800&q=80")),
                        Map.entry("Système d'Irrigation Goutte à Goutte", List.of("https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=800&q=80")),
                        Map.entry("Canne à Pêche Télescopique 3.6m", List.of("https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?w=800&q=80")),
                        Map.entry("Kit Leurres de Pêche 50 pièces", List.of("https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&q=80")),
                        Map.entry("Kayak de Pêche 2 Places", List.of("https://images.unsplash.com/photo-1464820453369-31d2c0b651af?w=800&q=80")),
                        Map.entry("Huile d'Olive Extra Vierge 5L", List.of("https://images.unsplash.com/photo-1476224203421-9ac39bcb3322?w=800&q=80")),
                        Map.entry("Dattes Deglet Nour 1kg", List.of("https://images.unsplash.com/photo-1570976919727-69f0e1ff5866?w=800&q=80")),
                        Map.entry("Harissa Artisanale 500g", List.of("https://images.unsplash.com/photo-1494809610411-52cac6067b9e?w=800&q=80")),
                        Map.entry("Tapis Berbère Fait Main", List.of("https://images.unsplash.com/photo-1560185127-6d5de1e61449?w=800&q=80")),
                        Map.entry("Service à Thé Tunisien", List.of("https://images.unsplash.com/photo-1518118573786-cf5a2ac0c6fa?w=800&q=80"))
                );
                List<Product> products = productRepository.findAll();
                products.forEach(product -> {
                    if (imageMap.containsKey(product.getNom())) {
                        product.setImages(imageMap.get(product.getNom()));
                    }
                });
                productRepository.saveAll(products);
                return;
            }

            // Créer l'admin
            User admin = userRepository.save(User.builder()
                    .email("admin@shopflow.com")
                    .motDePasse(passwordEncoder.encode("Admin123!"))
                    .prenom("Admin")
                    .nom("ShopFlow")
                    .role(Role.ADMIN)
                    .actif(true)
                    .build());

            // Créer un vendeur
            User seller = userRepository.save(User.builder()
                    .email("vendeur@shopflow.com")
                    .motDePasse(passwordEncoder.encode("Seller123!"))
                    .prenom("Fedi")
                    .nom("Sayadi")
                    .role(Role.SELLER)
                    .actif(true)
                    .build());

            sellerProfileRepository.save(SellerProfile.builder()
                    .user(seller)
                    .nomBoutique("Boutique Fedi Sayadi")
                    .description("Boutique spécialisée en électronique et mode")
                    .logo("https://via.placeholder.com/150")
                    .build());

            // Créer un client
            User customer = userRepository.save(User.builder()
                    .email("client@shopflow.com")
                    .motDePasse(passwordEncoder.encode("Client123!"))
                    .prenom("Fedi")
                    .nom("Sayadi")
                    .role(Role.CUSTOMER)
                    .actif(true)
                    .build());

            // Créer les catégories principales
            Category electronique = categoryRepository.save(Category.builder()
                    .nom("Électronique").description("Appareils et accessoires électroniques").build());
            Category mode = categoryRepository.save(Category.builder()
                    .nom("Mode & Vêtements").description("Vêtements, chaussures et accessoires").build());
            Category agriculture = categoryRepository.save(Category.builder()
                    .nom("Agriculture").description("Équipements et produits agricoles").build());
            Category peche = categoryRepository.save(Category.builder()
                    .nom("Pêche & Marine").description("Matériel de pêche et équipements marins").build());
            Category maison = categoryRepository.save(Category.builder()
                    .nom("Maison & Décoration").description("Meubles et articles de décoration").build());
            Category alimentation = categoryRepository.save(Category.builder()
                    .nom("Alimentation").description("Produits alimentaires et boissons").build());
            Category sport = categoryRepository.save(Category.builder()
                    .nom("Sport & Loisirs").description("Équipements sportifs et loisirs").build());
            Category beaute = categoryRepository.save(Category.builder()
                    .nom("Beauté & Santé").description("Produits de beauté et santé").build());

            // Sous-catégories Électronique
            Category smartphones = categoryRepository.save(Category.builder()
                    .nom("Smartphones").description("Téléphones mobiles").parent(electronique).build());
            Category ordinateurs = categoryRepository.save(Category.builder()
                    .nom("Ordinateurs").description("PC portables et de bureau").parent(electronique).build());
            Category tablettes = categoryRepository.save(Category.builder()
                    .nom("Tablettes").description("Tablettes tactiles").parent(electronique).build());
            Category accessoires = categoryRepository.save(Category.builder()
                    .nom("Accessoires").description("Accessoires électroniques").parent(electronique).build());

            // Sous-catégories Mode
            Category hommes = categoryRepository.save(Category.builder()
                    .nom("Homme").description("Mode masculine").parent(mode).build());
            Category femmes = categoryRepository.save(Category.builder()
                    .nom("Femme").description("Mode féminine").parent(mode).build());
            Category enfants = categoryRepository.save(Category.builder()
                    .nom("Enfants").description("Mode enfantine").parent(mode).build());
            Category chaussures = categoryRepository.save(Category.builder()
                    .nom("Chaussures").description("Chaussures pour tous").parent(mode).build());

            // Sous-catégories Agriculture
            Category outils = categoryRepository.save(Category.builder()
                    .nom("Outils Agricoles").description("Outils et machines agricoles").parent(agriculture).build());
            Category semences = categoryRepository.save(Category.builder()
                    .nom("Semences").description("Graines et plants").parent(agriculture).build());
            Category engrais = categoryRepository.save(Category.builder()
                    .nom("Engrais & Fertilisants").description("Produits pour la culture").parent(agriculture).build());

            // Sous-catégories Pêche
            Category cannes = categoryRepository.save(Category.builder()
                    .nom("Cannes à Pêche").description("Cannes et moulinets").parent(peche).build());
            Category appats = categoryRepository.save(Category.builder()
                    .nom("Appâts & Leurres").description("Appâts naturels et artificiels").parent(peche).build());
            Category bateaux = categoryRepository.save(Category.builder()
                    .nom("Bateaux & Kayaks").description("Embarcations de pêche").parent(peche).build());

            // ========== PRODUITS ÉLECTRONIQUE ==========
            
            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Samsung Galaxy S23")
                    .description("Smartphone 5G, écran AMOLED 6.1\", 128GB, caméra 50MP")
                    .prix(BigDecimal.valueOf(2499.000))
                    .prixPromo(BigDecimal.valueOf(2199.000))
                    .stock(45)
                    .nombreVentes(156)
                    .noteMoyenne(4.7)
                    .nombreAvis(89)
                    .images(List.of("https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=800&q=80"))
                    .categories(List.of(electronique, smartphones))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("iPhone 14 Pro")
                    .description("Apple iPhone 14 Pro, 256GB, Dynamic Island, caméra 48MP")
                    .prix(BigDecimal.valueOf(4299.000))
                    .stock(30)
                    .nombreVentes(98)
                    .noteMoyenne(4.9)
                    .nombreAvis(67)
                    .images(List.of("https://images.unsplash.com/photo-1678652197831-2d180705cd2c?w=800&q=80"))
                    .categories(List.of(electronique, smartphones))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Dell XPS 15")
                    .description("Laptop professionnel, Intel i7, 16GB RAM, SSD 512GB, écran 4K")
                    .prix(BigDecimal.valueOf(4599.000))
                    .prixPromo(BigDecimal.valueOf(3999.000))
                    .stock(20)
                    .nombreVentes(45)
                    .noteMoyenne(4.8)
                    .nombreAvis(34)
                    .images(List.of("https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=800&q=80"))
                    .categories(List.of(electronique, ordinateurs))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("iPad Air 2024")
                    .description("Tablette Apple, écran 10.9\", M1 chip, 64GB")
                    .prix(BigDecimal.valueOf(2199.000))
                    .stock(35)
                    .nombreVentes(78)
                    .noteMoyenne(4.6)
                    .nombreAvis(45)
                    .images(List.of("https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800&q=80"))
                    .categories(List.of(electronique, tablettes))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("AirPods Pro 2")
                    .description("Écouteurs sans fil, réduction de bruit active, autonomie 30h")
                    .prix(BigDecimal.valueOf(899.000))
                    .stock(100)
                    .nombreVentes(234)
                    .noteMoyenne(4.7)
                    .nombreAvis(156)
                    .images(List.of("https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=800&q=80"))
                    .categories(List.of(electronique, accessoires))
                    .build());

            // ========== PRODUITS MODE ==========

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Maillot Équipe de Tunisie 2024")
                    .description("Maillot officiel de l'équipe nationale, 100% polyester respirant")
                    .prix(BigDecimal.valueOf(149.000))
                    .stock(150)
                    .nombreVentes(456)
                    .noteMoyenne(4.8)
                    .nombreAvis(234)
                    .images(List.of("https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=800&q=80"))
                    .categories(List.of(mode, hommes, sport))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Robe Traditionnelle Tunisienne")
                    .description("Robe élégante avec broderies artisanales, 100% coton")
                    .prix(BigDecimal.valueOf(299.000))
                    .prixPromo(BigDecimal.valueOf(249.000))
                    .stock(45)
                    .nombreVentes(89)
                    .noteMoyenne(4.9)
                    .nombreAvis(67)
                    .images(List.of("https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800&q=80"))
                    .categories(List.of(mode, femmes))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Jebba Homme Blanche")
                    .description("Jebba traditionnelle tunisienne, coton premium, coupe moderne")
                    .prix(BigDecimal.valueOf(189.000))
                    .stock(80)
                    .nombreVentes(123)
                    .noteMoyenne(4.6)
                    .nombreAvis(78)
                    .images(List.of("https://images.unsplash.com/photo-1617127365659-c47fa864d8bc?w=800&q=80"))
                    .categories(List.of(mode, hommes))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Baskets Nike Air Max")
                    .description("Chaussures de sport confortables, semelle Air, design moderne")
                    .prix(BigDecimal.valueOf(399.000))
                    .stock(60)
                    .nombreVentes(167)
                    .noteMoyenne(4.7)
                    .nombreAvis(98)
                    .images(List.of("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&q=80"))
                    .categories(List.of(mode, chaussures, sport))
                    .build());

            // ========== PRODUITS AGRICULTURE ==========

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Motoculteur Professionnel")
                    .description("Motoculteur 7CV, largeur de travail 80cm, idéal pour petites parcelles")
                    .prix(BigDecimal.valueOf(2899.000))
                    .stock(15)
                    .nombreVentes(34)
                    .noteMoyenne(4.5)
                    .nombreAvis(23)
                    .images(List.of("https://images.unsplash.com/photo-1625246333195-78d9c38ad449?w=800&q=80"))
                    .categories(List.of(agriculture, outils))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Semences Tomates Tunisiennes")
                    .description("Graines de tomates variété locale, sachet 50g, rendement élevé")
                    .prix(BigDecimal.valueOf(12.500))
                    .stock(500)
                    .nombreVentes(678)
                    .noteMoyenne(4.8)
                    .nombreAvis(345)
                    .images(List.of("https://images.unsplash.com/photo-1572441710518-97f7053f22ee?w=800&q=80"))
                    .categories(List.of(agriculture, semences))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Engrais Organique Bio 25kg")
                    .description("Engrais naturel pour toutes cultures, enrichi en nutriments")
                    .prix(BigDecimal.valueOf(89.000))
                    .stock(200)
                    .nombreVentes(234)
                    .noteMoyenne(4.6)
                    .nombreAvis(145)
                    .images(List.of("https://images.unsplash.com/photo-1517430816045-df4b7de1ca71?w=800&q=80"))
                    .categories(List.of(agriculture, engrais))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Système d'Irrigation Goutte à Goutte")
                    .description("Kit complet pour 100m², économie d'eau 70%, facile à installer")
                    .prix(BigDecimal.valueOf(349.000))
                    .prixPromo(BigDecimal.valueOf(299.000))
                    .stock(45)
                    .nombreVentes(89)
                    .noteMoyenne(4.7)
                    .nombreAvis(56)
                    .images(List.of("https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=800&q=80"))
                    .categories(List.of(agriculture, outils))
                    .build());

            // ========== PRODUITS PÊCHE ==========

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Canne à Pêche Télescopique 3.6m")
                    .description("Canne en fibre de carbone, légère et résistante, idéale mer et rivière")
                    .prix(BigDecimal.valueOf(189.000))
                    .stock(75)
                    .nombreVentes(145)
                    .noteMoyenne(4.5)
                    .nombreAvis(89)
                    .images(List.of("https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?w=800&q=80"))
                    .categories(List.of(peche, cannes))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Kit Leurres de Pêche 50 pièces")
                    .description("Assortiment de leurres souples et durs, toutes espèces")
                    .prix(BigDecimal.valueOf(79.000))
                    .stock(120)
                    .nombreVentes(267)
                    .noteMoyenne(4.6)
                    .nombreAvis(134)
                    .images(List.of("https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&q=80"))
                    .categories(List.of(peche, appats))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Kayak de Pêche 2 Places")
                    .description("Kayak stable avec porte-cannes, coffres étanches, pagaies incluses")
                    .prix(BigDecimal.valueOf(1899.000))
                    .prixPromo(BigDecimal.valueOf(1599.000))
                    .stock(8)
                    .nombreVentes(23)
                    .noteMoyenne(4.8)
                    .nombreAvis(18)
                    .images(List.of("https://images.unsplash.com/photo-1464820453369-31d2c0b651af?w=800&q=80"))
                    .categories(List.of(peche, bateaux))
                    .build());

            // ========== PRODUITS ALIMENTATION ==========

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Huile d'Olive Extra Vierge 5L")
                    .description("Huile d'olive tunisienne première pression à froid, Sfax")
                    .prix(BigDecimal.valueOf(149.000))
                    .stock(200)
                    .nombreVentes(456)
                    .noteMoyenne(4.9)
                    .nombreAvis(289)
                    .images(List.of("https://images.unsplash.com/photo-1476224203421-9ac39bcb3322?w=800&q=80"))
                    .categories(List.of(alimentation))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Dattes Deglet Nour 1kg")
                    .description("Dattes premium de Tozeur, calibre supérieur, conditionnement soigné")
                    .prix(BigDecimal.valueOf(45.000))
                    .stock(300)
                    .nombreVentes(678)
                    .noteMoyenne(4.8)
                    .nombreAvis(423)
                    .images(List.of("https://images.unsplash.com/photo-1570976919727-69f0e1ff5866?w=800&q=80"))
                    .categories(List.of(alimentation))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Harissa Artisanale 500g")
                    .description("Harissa tunisienne traditionnelle, piments frais, épices sélectionnées")
                    .prix(BigDecimal.valueOf(18.500))
                    .stock(250)
                    .nombreVentes(534)
                    .noteMoyenne(4.7)
                    .nombreAvis(312)
                    .images(List.of("https://images.unsplash.com/photo-1494809610411-52cac6067b9e?w=800&q=80"))
                    .categories(List.of(alimentation))
                    .build());

            // ========== PRODUITS MAISON ==========

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Tapis Berbère Fait Main")
                    .description("Tapis artisanal 200x150cm, laine pure, motifs traditionnels")
                    .prix(BigDecimal.valueOf(899.000))
                    .prixPromo(BigDecimal.valueOf(749.000))
                    .stock(25)
                    .nombreVentes(45)
                    .noteMoyenne(4.9)
                    .nombreAvis(34)
                    .images(List.of("https://images.unsplash.com/photo-1560185127-6d5de1e61449?w=800&q=80"))
                    .categories(List.of(maison))
                    .build());

            productRepository.save(Product.builder()
                    .seller(seller)
                    .nom("Service à Thé Tunisien")
                    .description("Service complet 6 personnes, céramique peinte main, plateau inclus")
                    .prix(BigDecimal.valueOf(249.000))
                    .stock(40)
                    .nombreVentes(89)
                    .noteMoyenne(4.7)
                    .nombreAvis(67)
                    .images(List.of("https://images.unsplash.com/photo-1518118573786-cf5a2ac0c6fa?w=800&q=80"))
                    .categories(List.of(maison))
                    .build());

            // Créer un coupon
            couponRepository.save(Coupon.builder()
                    .code("WELCOME10")
                    .type(CouponType.PERCENT)
                    .valeur(BigDecimal.valueOf(10))
                    .usagesMax(1000)
                    .actif(true)
                    .build());

            couponRepository.save(Coupon.builder()
                    .code("PROMO20")
                    .type(CouponType.FIXED)
                    .valeur(BigDecimal.valueOf(20))
                    .usagesMax(500)
                    .actif(true)
                    .build());

            log.info("Données initialisées avec succès !");
            log.info("Admin: admin@shopflow.com / Admin123!");
            log.info("Vendeur: vendeur@shopflow.com / Seller123!");
            log.info("Client: client@shopflow.com / Client123!");
        };
    }
}

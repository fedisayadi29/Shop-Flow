export type Role = 'ADMIN' | 'SELLER' | 'CUSTOMER';
export type OrderStatus = 'PENDING' | 'PAID' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'REFUNDED';
export type CouponType = 'PERCENT' | 'FIXED';

export interface User {
  id: number;
  email: string;
  prenom: string;
  nom: string;
  role: Role;
  actif: boolean;
  dateCreation: string;
  sellerProfile?: SellerProfile;
}

export interface SellerProfile {
  id: number;
  nomBoutique: string;
  description?: string;
  logo?: string;
  note: number;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: User;
}

export interface Category {
  id: number;
  nom: string;
  description?: string;
  parentId?: number;
  sousCategories?: Category[];
}

export interface ProductVariant {
  id: number;
  attribut: string;
  valeur: string;
  stockSupplementaire: number;
  prixDelta: number;
}

export interface Product {
  id: number;
  nom: string;
  description?: string;
  prix: number;
  prixPromo?: number;
  stock: number;
  actif: boolean;
  dateCreation: string;
  nombreVentes: number;
  noteMoyenne: number;
  nombreAvis: number;
  enPromotion: boolean;
  pourcentageRemise: number;
  images: string[];
  categories: Category[];
  variants: ProductVariant[];
  seller?: {
    id: number;
    prenom: string;
    nom: string;
    nomBoutique?: string;
    logo?: string;
  };
}

export interface CartItem {
  id: number;
  productId: number;
  nomProduit: string;
  imageProduit?: string;
  prixUnitaire: number;
  variantId?: number;
  variantInfo?: string;
  quantite: number;
  sousTotal: number;
  stockDisponible: number;
}

export interface Coupon {
  id: number;
  code: string;
  type: CouponType;
  valeur: number;
  dateExpiration?: string;
  usagesMax: number;
  usagesActuels: number;
  actif: boolean;
}

export interface Cart {
  id: number;
  lignes: CartItem[];
  coupon?: Coupon;
  sousTotal: number;
  remiseCoupon: number;
  fraisLivraison: number;
  totalTTC: number;
  dateModification: string;
}

export interface Address {
  id: number;
  rue: string;
  ville: string;
  codePostal: string;
  pays: string;
  principal: boolean;
}

export interface OrderItem {
  id: number;
  productId: number;
  nomProduit: string;
  imageProduit?: string;
  variantId?: number;
  variantInfo?: string;
  quantite: number;
  prixUnitaire: number;
  sousTotal: number;
}

export interface Order {
  id: number;
  numeroCommande: string;
  statut: OrderStatus;
  adresseLivraisonRue: string;
  adresseLivraisonVille: string;
  adresseLivraisonCodePostal: string;
  adresseLivraisonPays: string;
  sousTotal: number;
  fraisLivraison: number;
  remiseCoupon: number;
  totalTTC: number;
  dateCommande: string;
  isNew: boolean;
  lignes: OrderItem[];
  coupon?: Coupon;
}

export interface Review {
  id: number;
  productId: number;
  customerPrenom: string;
  customerNom: string;
  note: number;
  commentaire?: string;
  dateCreation: string;
  approuve: boolean;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
}

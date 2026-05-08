import { Order, Product } from './index';

export interface DashboardSellerResponse {
  revenus: number;
  commandesEnAttente: number;
  totalProduits: number;
  alertesStockFaible: number;
  produitsStockFaible: Product[];
  commandesRecentes: Order[];
}

export interface DashboardAdminResponse {
  chiffreAffairesGlobal: number;
  totalCommandes: number;
  totalUtilisateurs: number;
  totalProduits: number;
  topProduits: Product[];
  topVendeurs: Array<{
    id: number;
    prenom: string;
    nom: string;
    nomBoutique?: string;
    logo?: string;
  }>;
  commandesRecentes: Order[];
}

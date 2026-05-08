import api from '@/lib/api';
import { PageResponse, Product } from '@/types';

export interface ProductFilters {
  categoryId?: number;
  prixMin?: number;
  prixMax?: number;
  sellerId?: number;
  promo?: boolean;
  sortBy?: string;
  page?: number;
  size?: number;
}

export const productService = {
  async getProducts(filters: ProductFilters = {}): Promise<PageResponse<Product>> {
    const params = new URLSearchParams();
    if (filters.categoryId) params.append('categoryId', String(filters.categoryId));
    if (filters.prixMin !== undefined) params.append('prixMin', String(filters.prixMin));
    if (filters.prixMax !== undefined) params.append('prixMax', String(filters.prixMax));
    if (filters.sellerId) params.append('sellerId', String(filters.sellerId));
    if (filters.promo !== undefined) params.append('promo', String(filters.promo));
    if (filters.sortBy) params.append('sortBy', filters.sortBy);
    params.append('page', String(filters.page ?? 0));
    params.append('size', String(filters.size ?? 12));

    const response = await api.get(`/api/products?${params.toString()}`);
    return response.data;
  },

  async getProduct(id: number): Promise<Product> {
    const response = await api.get(`/api/products/${id}`);
    return response.data;
  },

  async searchProducts(q: string, page = 0, size = 12): Promise<PageResponse<Product>> {
    const response = await api.get(`/api/products/search?q=${encodeURIComponent(q)}&page=${page}&size=${size}`);
    return response.data;
  },

  async getTopSelling(): Promise<Product[]> {
    const response = await api.get('/api/products/top-selling');
    return response.data;
  },

  async createProduct(data: unknown): Promise<Product> {
    const response = await api.post('/api/products', data);
    return response.data;
  },

  async updateProduct(id: number, data: unknown): Promise<Product> {
    const response = await api.put(`/api/products/${id}`, data);
    return response.data;
  },

  async deleteProduct(id: number): Promise<void> {
    await api.delete(`/api/products/${id}`);
  },
};

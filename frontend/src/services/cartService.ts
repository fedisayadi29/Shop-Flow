import api from '@/lib/api';
import { Cart } from '@/types';

export const cartService = {
  async getCart(): Promise<Cart> {
    const response = await api.get('/api/cart');
    return response.data;
  },

  async addItem(productId: number, quantite: number, variantId?: number): Promise<Cart> {
    const response = await api.post('/api/cart/items', { productId, quantite, variantId });
    return response.data;
  },

  async updateItem(itemId: number, quantite: number): Promise<Cart> {
    const response = await api.put(`/api/cart/items/${itemId}`, { quantite });
    return response.data;
  },

  async removeItem(itemId: number): Promise<Cart> {
    const response = await api.delete(`/api/cart/items/${itemId}`);
    return response.data;
  },

  async applyCoupon(code: string): Promise<Cart> {
    const response = await api.post('/api/cart/coupon', { code });
    return response.data;
  },

  async removeCoupon(): Promise<Cart> {
    const response = await api.delete('/api/cart/coupon');
    return response.data;
  },
};

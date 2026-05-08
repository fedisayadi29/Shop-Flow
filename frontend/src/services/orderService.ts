import api from '@/lib/api';
import { Order, PageResponse } from '@/types';

export const orderService = {
  async createOrder(addressId: number): Promise<Order> {
    const response = await api.post('/api/orders', { addressId });
    return response.data;
  },

  async getOrder(id: number): Promise<Order> {
    const response = await api.get(`/api/orders/${id}`);
    return response.data;
  },

  async getMyOrders(page = 0, size = 10): Promise<PageResponse<Order>> {
    const response = await api.get(`/api/orders/my?page=${page}&size=${size}`);
    return response.data;
  },

  async getAllOrders(queryParams?: string): Promise<PageResponse<Order>> {
    const url = queryParams ? `/api/orders?${queryParams}` : '/api/orders';
    const response = await api.get(url);
    return response.data;
  },

  async updateStatus(id: number, statut: string): Promise<Order> {
    const response = await api.put(`/api/orders/${id}/status`, { statut });
    return response.data;
  },

  async cancelOrder(id: number): Promise<Order> {
    const response = await api.put(`/api/orders/${id}/cancel`);
    return response.data;
  },
};

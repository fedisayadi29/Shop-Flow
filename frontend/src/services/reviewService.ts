import api from '@/lib/api';
import { PageResponse, Review } from '@/types';

export const reviewService = {
  async getProductReviews(productId: number, page = 0, size = 10): Promise<PageResponse<Review>> {
    const response = await api.get(`/api/reviews/product/${productId}?page=${page}&size=${size}`);
    return response.data;
  },

  async createReview(data: { productId: number; note: number; commentaire?: string }): Promise<Review> {
    const response = await api.post('/api/reviews', data);
    return response.data;
  },

  async approveReview(id: number): Promise<Review> {
    const response = await api.put(`/api/reviews/${id}/approve`);
    return response.data;
  },
};

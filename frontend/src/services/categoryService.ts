import api from '@/lib/api';
import { Category } from '@/types';

export const categoryService = {
  async getCategories(): Promise<Category[]> {
    const response = await api.get('/api/categories');
    return response.data;
  },

  async createCategory(data: { nom: string; description?: string; parentId?: number }): Promise<Category> {
    const response = await api.post('/api/categories', data);
    return response.data;
  },

  async updateCategory(id: number, data: { nom: string; description?: string; parentId?: number }): Promise<Category> {
    const response = await api.put(`/api/categories/${id}`, data);
    return response.data;
  },

  async deleteCategory(id: number): Promise<void> {
    await api.delete(`/api/categories/${id}`);
  },
};

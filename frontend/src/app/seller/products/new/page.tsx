'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ArrowLeft, Plus, X, Upload, Image as ImageIcon } from 'lucide-react';
import { productService } from '@/services/productService';
import { categoryService } from '@/services/categoryService';
import { useAuthStore } from '@/store/authStore';
import { Category } from '@/types';
import toast from 'react-hot-toast';
import axios from 'axios';

export default function NewProductPage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(false);
  const [uploadingImages, setUploadingImages] = useState(false);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);

  const [form, setForm] = useState({
    nom: '',
    description: '',
    prix: '',
    prixPromo: '',
    stock: '0',
    images: [] as string[],
    categoryIds: [] as number[],
  });

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'SELLER') { router.push('/auth/login'); return; }
    categoryService.getCategories().then(setCategories).catch(console.error);
  }, [isAuthenticated, user, router]);

  const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (!files || files.length === 0) return;

    setUploadingImages(true);
    const formData = new FormData();
    
    Array.from(files).forEach((file) => {
      formData.append('files', file);
    });

    try {
      const token = localStorage.getItem('access_token');
      if (!token) {
        toast.error('Vous devez être connecté pour uploader des images');
        setUploadingImages(false);
        return;
      }

      const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/api/images/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.data.success && response.data.imageUrls) {
        const newImageUrls = response.data.imageUrls.map((url: string) => 
          `${process.env.NEXT_PUBLIC_API_URL}${url}`
        );
        
        setForm((prev) => ({
          ...prev,
          images: [...prev.images, ...newImageUrls],
        }));
        
        setImagePreviews((prev) => [...prev, ...newImageUrls]);
        toast.success(response.data.message || 'Images uploadées avec succès');
        
        if (response.data.errors && response.data.errors.length > 0) {
          response.data.errors.forEach((error: string) => toast.error(error));
        }
      }
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Erreur lors de l\'upload');
      console.error('Upload error:', err);
    } finally {
      setUploadingImages(false);
      // Reset file input
      e.target.value = '';
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const data = {
        nom: form.nom,
        description: form.description,
        prix: parseFloat(form.prix),
        prixPromo: form.prixPromo ? parseFloat(form.prixPromo) : null,
        stock: parseInt(form.stock),
        images: form.images.filter((img) => img.trim()),
        categoryIds: form.categoryIds,
      };
      await productService.createProduct(data);
      toast.success('Produit créé !');
      router.push('/seller/products');
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Erreur');
    } finally {
      setLoading(false);
    }
  };

  const toggleCategory = (id: number) => {
    setForm((prev) => ({
      ...prev,
      categoryIds: prev.categoryIds.includes(id)
        ? prev.categoryIds.filter((c) => c !== id)
        : [...prev.categoryIds, id],
    }));
  };

  const removeImage = (index: number) => {
    setForm((prev) => ({
      ...prev,
      images: prev.images.filter((_, i) => i !== index),
    }));
    setImagePreviews((prev) => prev.filter((_, i) => i !== index));
  };

  return (
    <div className="max-w-2xl mx-auto px-4 py-8">
      <div className="flex items-center space-x-3 mb-6">
        <Link href="/seller/products" className="text-gray-500 hover:text-indigo-600">
          <ArrowLeft className="h-5 w-5" />
        </Link>
        <h1 className="text-2xl font-bold text-gray-800">Nouveau produit</h1>
      </div>

      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm p-6 space-y-5">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Nom du produit *</label>
          <input
            value={form.nom}
            onChange={(e) => setForm({ ...form, nom: e.target.value })}
            required
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
          <textarea
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            rows={4}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
          />
        </div>

        <div className="grid grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Prix (TND) *</label>
            <input
              type="number"
              step="0.001"
              min="0.001"
              value={form.prix}
              onChange={(e) => setForm({ ...form, prix: e.target.value })}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Prix promo (TND)</label>
            <input
              type="number"
              step="0.001"
              min="0"
              value={form.prixPromo}
              onChange={(e) => setForm({ ...form, prixPromo: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Stock</label>
            <input
              type="number"
              min="0"
              value={form.stock}
              onChange={(e) => setForm({ ...form, stock: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
            />
          </div>
        </div>

        {/* Images */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">Images du produit</label>
          
          {/* File Upload Button */}
          <div className="mb-3">
            <label className="inline-flex items-center px-4 py-2 bg-indigo-600 text-white rounded-lg cursor-pointer hover:bg-indigo-700 transition-colors">
              <Upload className="h-4 w-4 mr-2" />
              <span className="text-sm font-medium">
                {uploadingImages ? 'Upload en cours...' : 'Choisir des images'}
              </span>
              <input
                type="file"
                multiple
                accept="image/*"
                onChange={handleFileUpload}
                disabled={uploadingImages}
                className="hidden"
              />
            </label>
            <p className="text-xs text-gray-600 mt-1">
              Formats acceptés: JPG, PNG, GIF (max 5MB par image)
            </p>
          </div>

          {/* Image Previews */}
          {imagePreviews.length > 0 && (
            <div className="grid grid-cols-3 gap-3">
              {imagePreviews.map((preview, index) => (
                <div key={index} className="relative group">
                  <div className="aspect-square rounded-lg overflow-hidden border-2 border-gray-200">
                    <img
                      src={preview}
                      alt={`Preview ${index + 1}`}
                      className="w-full h-full object-cover"
                    />
                  </div>
                  <button
                    type="button"
                    onClick={() => removeImage(index)}
                    className="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1 opacity-0 group-hover:opacity-100 transition-opacity"
                  >
                    <X className="h-4 w-4" />
                  </button>
                </div>
              ))}
            </div>
          )}

          {imagePreviews.length === 0 && (
            <div className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center">
              <ImageIcon className="h-12 w-12 text-gray-400 mx-auto mb-2" />
              <p className="text-sm text-gray-600">Aucune image ajoutée</p>
            </div>
          )}
        </div>

        {/* Categories */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">Catégories</label>
          <div className="flex flex-wrap gap-2">
            {categories.map((cat) => (
              <button
                key={cat.id}
                type="button"
                onClick={() => toggleCategory(cat.id)}
                className={`px-3 py-1.5 border rounded-full text-sm transition-colors ${
                  form.categoryIds.includes(cat.id)
                    ? 'border-indigo-600 bg-indigo-50 text-indigo-600'
                    : 'border-gray-300 text-gray-600 hover:border-indigo-400'
                }`}
              >
                {cat.nom}
              </button>
            ))}
          </div>
        </div>

        <div className="flex space-x-3 pt-2">
          <Link href="/seller/products" className="flex-1 border border-gray-300 text-gray-700 py-2.5 rounded-lg text-center text-sm font-medium hover:bg-gray-50">
            Annuler
          </Link>
          <button
            type="submit"
            disabled={loading}
            className="flex-1 bg-indigo-600 text-white py-2.5 rounded-lg text-sm font-semibold hover:bg-indigo-700 disabled:opacity-50"
          >
            {loading ? 'Création...' : 'Créer le produit'}
          </button>
        </div>
      </form>
    </div>
  );
}


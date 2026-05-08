'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Plus, Edit, Trash2, Eye } from 'lucide-react';
import { productService } from '@/services/productService';
import { useAuthStore } from '@/store/authStore';
import { Product, PageResponse } from '@/types';
import LoadingSpinner from '@/components/ui/LoadingSpinner';
import toast from 'react-hot-toast';

export default function SellerProductsPage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [products, setProducts] = useState<PageResponse<Product> | null>(null);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'SELLER') { router.push('/auth/login'); return; }
    productService.getProducts({ sellerId: user.id, page, size: 10 })
      .then(setProducts)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [isAuthenticated, user, router, page]);

  const handleDelete = async (id: number) => {
    if (!confirm('Désactiver ce produit ?')) return;
    try {
      await productService.deleteProduct(id);
      toast.success('Produit désactivé');
      setProducts((prev) => prev ? {
        ...prev,
        content: prev.content.map((p) => p.id === id ? { ...p, actif: false } : p)
      } : null);
    } catch {
      toast.error('Erreur');
    }
  };

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Mes produits</h1>
        <Link
          href="/seller/products/new"
          className="bg-indigo-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-indigo-700 flex items-center space-x-2"
        >
          <Plus className="h-4 w-4" />
          <span>Nouveau produit</span>
        </Link>
      </div>

      {products?.content.length === 0 ? (
        <div className="text-center py-16">
          <p className="text-gray-500">Aucun produit. Créez votre premier produit !</p>
        </div>
      ) : (
        <div className="bg-white rounded-xl shadow-sm overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="text-left px-4 py-3 text-xs font-medium text-gray-500 uppercase">Produit</th>
                <th className="text-left px-4 py-3 text-xs font-medium text-gray-500 uppercase">Prix</th>
                <th className="text-left px-4 py-3 text-xs font-medium text-gray-500 uppercase">Stock</th>
                <th className="text-left px-4 py-3 text-xs font-medium text-gray-500 uppercase">Statut</th>
                <th className="text-left px-4 py-3 text-xs font-medium text-gray-500 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {products?.content.map((product) => (
                <tr key={product.id} className="hover:bg-gray-50">
                  <td className="px-4 py-3">
                    <div className="flex items-center space-x-3">
                      <img
                        src={product.images?.[0] || 'https://via.placeholder.com/40'}
                        alt={product.nom}
                        className="w-10 h-10 object-cover rounded-lg"
                      />
                      <div>
                        <p className="text-sm font-medium text-gray-800">{product.nom}</p>
                        <p className="text-xs text-gray-500">{product.nombreVentes} ventes</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-4 py-3">
                    <p className="text-sm font-medium text-gray-800">{product.prix.toFixed(3)} TND</p>
                    {product.enPromotion && (
                      <p className="text-xs text-red-500">{product.prixPromo?.toFixed(3)} TND promo</p>
                    )}
                  </td>
                  <td className="px-4 py-3">
                    <span className={`text-sm font-medium ${product.stock <= 5 ? 'text-red-600' : 'text-gray-800'}`}>
                      {product.stock}
                    </span>
                  </td>
                  <td className="px-4 py-3">
                    <span className={`text-xs px-2 py-1 rounded-full font-medium ${product.actif ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-600'}`}>
                      {product.actif ? 'Actif' : 'Inactif'}
                    </span>
                  </td>
                  <td className="px-4 py-3">
                    <div className="flex items-center space-x-2">
                      <Link href={`/produits/${product.id}`} className="text-gray-400 hover:text-indigo-600">
                        <Eye className="h-4 w-4" />
                      </Link>
                      <Link href={`/seller/products/${product.id}/edit`} className="text-gray-400 hover:text-indigo-600">
                        <Edit className="h-4 w-4" />
                      </Link>
                      {product.actif && (
                        <button onClick={() => handleDelete(product.id)} className="text-gray-400 hover:text-red-600">
                          <Trash2 className="h-4 w-4" />
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Pagination */}
          {products && products.totalPages > 1 && (
            <div className="flex justify-center space-x-2 p-4 border-t">
              <button disabled={products.first} onClick={() => setPage(page - 1)} className="px-3 py-1 border rounded text-sm disabled:opacity-50 text-black font-medium hover:bg-gray-100">Précédent</button>
              <span className="px-3 py-1 text-sm text-gray-600">{products.page + 1} / {products.totalPages}</span>
              <button disabled={products.last} onClick={() => setPage(page + 1)} className="px-3 py-1 border rounded text-sm disabled:opacity-50 text-black font-medium hover:bg-gray-100">Suivant</button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

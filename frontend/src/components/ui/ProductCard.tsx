'use client';

import Link from 'next/link';
import Image from 'next/image';
import { Star, ShoppingCart } from 'lucide-react';
import { Product } from '@/types';
import { cartService } from '@/services/cartService';
import { useCartStore } from '@/store/cartStore';
import { useAuthStore } from '@/store/authStore';
import toast from 'react-hot-toast';
import { useState } from 'react';

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  const { setCart } = useCartStore();
  const { isAuthenticated, user } = useAuthStore();
  const [loading, setLoading] = useState(false);

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault();
    if (!isAuthenticated || user?.role !== 'CUSTOMER') {
      toast.error('Connectez-vous en tant que client pour ajouter au panier');
      return;
    }
    setLoading(true);
    try {
      const cart = await cartService.addItem(product.id, 1);
      setCart(cart);
      toast.success('Ajouté au panier !');
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Erreur lors de l\'ajout');
    } finally {
      setLoading(false);
    }
  };

  const imageUrl = product.images?.[0] || 'https://via.placeholder.com/300x300?text=Produit';
  const prix = product.enPromotion ? product.prixPromo! : product.prix;

  return (
    <Link href={`/produits/${product.id}`} className="group">
      <div className="bg-white rounded-xl shadow-sm hover:shadow-md transition-shadow duration-200 overflow-hidden border border-gray-100">
        {/* Image */}
        <div className="relative h-48 bg-gray-100 overflow-hidden">
          <img
            src={imageUrl}
            alt={product.nom}
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          />
          {product.enPromotion && (
            <span className="absolute top-2 left-2 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded-full">
              -{Math.round(product.pourcentageRemise)}%
            </span>
          )}
          {product.stock === 0 && (
            <div className="absolute inset-0 bg-black/40 flex items-center justify-center">
              <span className="text-white font-semibold">Rupture de stock</span>
            </div>
          )}
        </div>

        {/* Content */}
        <div className="p-4">
          <h3 className="font-semibold text-gray-800 line-clamp-2 text-sm mb-1">{product.nom}</h3>

          {/* Rating */}
          {product.nombreAvis > 0 && (
            <div className="flex items-center space-x-1 mb-2">
              <div className="flex">
                {[1, 2, 3, 4, 5].map((star) => (
                  <Star
                    key={star}
                    className={`h-3 w-3 ${star <= Math.round(product.noteMoyenne) ? 'text-yellow-400 fill-yellow-400' : 'text-gray-300'}`}
                  />
                ))}
              </div>
              <span className="text-xs text-gray-500">({product.nombreAvis})</span>
            </div>
          )}

          {/* Price */}
          <div className="flex items-center justify-between mt-2">
            <div>
              <span className="text-lg font-bold text-indigo-600">{prix?.toFixed(3)} TND</span>
              {product.enPromotion && (
                <span className="text-sm text-gray-400 line-through ml-2">{product.prix.toFixed(3)} TND</span>
              )}
            </div>

            <button
              onClick={handleAddToCart}
              disabled={loading || product.stock === 0}
              className="p-2 bg-indigo-600 text-white rounded-full hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
              title="Ajouter au panier"
            >
              <ShoppingCart className="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </Link>
  );
}

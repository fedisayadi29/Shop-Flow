'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Trash2, Plus, Minus, Tag, ShoppingBag } from 'lucide-react';
import { cartService } from '@/services/cartService';
import { useCartStore } from '@/store/cartStore';
import { useAuthStore } from '@/store/authStore';
import LoadingSpinner from '@/components/ui/LoadingSpinner';
import toast from 'react-hot-toast';

export default function CartPage() {
  const { cart, setCart } = useCartStore();
  const { isAuthenticated, user } = useAuthStore();
  const [loading, setLoading] = useState(true);
  const [couponCode, setCouponCode] = useState('');
  const [applyingCoupon, setApplyingCoupon] = useState(false);
  const router = useRouter();

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'CUSTOMER') {
      router.push('/auth/login');
      return;
    }
    cartService.getCart()
      .then((c) => { setCart(c); setLoading(false); })
      .catch(() => setLoading(false));
  }, [isAuthenticated, user, router, setCart]);

  const handleUpdateQuantity = async (itemId: number, newQty: number) => {
    try {
      const updated = await cartService.updateItem(itemId, newQty);
      setCart(updated);
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Erreur');
    }
  };

  const handleRemoveItem = async (itemId: number) => {
    try {
      const updated = await cartService.removeItem(itemId);
      setCart(updated);
      toast.success('Article retiré');
    } catch {
      toast.error('Erreur');
    }
  };

  const handleApplyCoupon = async () => {
    if (!couponCode.trim()) return;
    setApplyingCoupon(true);
    try {
      const updated = await cartService.applyCoupon(couponCode.trim());
      setCart(updated);
      toast.success('Code promo appliqué !');
      setCouponCode('');
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Code promo invalide');
    } finally {
      setApplyingCoupon(false);
    }
  };

  const handleRemoveCoupon = async () => {
    try {
      const updated = await cartService.removeCoupon();
      setCart(updated);
      toast.success('Code promo retiré');
    } catch {
      toast.error('Erreur');
    }
  };

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  if (!cart || cart.lignes.length === 0) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-20 text-center">
        <ShoppingBag className="h-16 w-16 text-gray-300 mx-auto mb-4" />
        <h2 className="text-xl font-semibold text-gray-700 mb-2">Votre panier est vide</h2>
        <p className="text-gray-500 mb-6">Découvrez nos produits et ajoutez-les à votre panier</p>
        <Link href="/catalogue" className="bg-indigo-600 text-white px-6 py-3 rounded-full font-medium hover:bg-indigo-700">
          Continuer mes achats
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Mon panier ({cart.lignes.length} article{cart.lignes.length > 1 ? 's' : ''})</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Cart Items */}
        <div className="lg:col-span-2 space-y-4">
          {cart.lignes.map((item) => (
            <div key={item.id} className="bg-white rounded-xl shadow-sm p-4 flex items-center space-x-4">
              <img
                src={item.imageProduit || 'https://via.placeholder.com/80x80?text=Produit'}
                alt={item.nomProduit}
                className="w-20 h-20 object-cover rounded-lg flex-shrink-0"
              />
              <div className="flex-1 min-w-0">
                <Link href={`/produits/${item.productId}`} className="font-medium text-gray-800 hover:text-indigo-600 line-clamp-2 text-sm">
                  {item.nomProduit}
                </Link>
                {item.variantInfo && (
                  <p className="text-xs text-gray-500 mt-0.5">{item.variantInfo}</p>
                )}
                <p className="text-indigo-600 font-semibold mt-1">{item.prixUnitaire.toFixed(3)} TND</p>
              </div>

              {/* Quantity Controls */}
              <div className="flex items-center space-x-2">
                <button
                  onClick={() => handleUpdateQuantity(item.id, item.quantite - 1)}
                  className="w-7 h-7 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 text-sm"
                >
                  <Minus className="h-3 w-3" />
                </button>
                <span className="w-6 text-center text-sm font-medium text-gray-900">{item.quantite}</span>
                <button
                  onClick={() => handleUpdateQuantity(item.id, item.quantite + 1)}
                  disabled={item.quantite >= item.stockDisponible}
                  className="w-7 h-7 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50 text-sm disabled:opacity-50"
                >
                  <Plus className="h-3 w-3" />
                </button>
              </div>

              <div className="text-right">
                <p className="font-semibold text-gray-800">{item.sousTotal.toFixed(3)} TND</p>
                <button
                  onClick={() => handleRemoveItem(item.id)}
                  className="text-red-400 hover:text-red-600 mt-1"
                >
                  <Trash2 className="h-4 w-4" />
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Order Summary */}
        <div className="space-y-4">
          {/* Coupon */}
          <div className="bg-white rounded-xl shadow-sm p-5">
            <h3 className="font-semibold text-gray-800 mb-3 flex items-center space-x-2">
              <Tag className="h-4 w-4 text-indigo-600" />
              <span>Code promo</span>
            </h3>
            {cart.coupon ? (
              <div className="flex items-center justify-between bg-green-50 border border-green-200 rounded-lg px-3 py-2">
                <span className="text-sm font-medium text-green-700">{cart.coupon.code}</span>
                <button onClick={handleRemoveCoupon} className="text-red-400 hover:text-red-600 text-xs">
                  Retirer
                </button>
              </div>
            ) : (
              <div className="flex space-x-2">
                <input
                  type="text"
                  value={couponCode}
                  onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
                  placeholder="WELCOME10"
                  className="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
                <button
                  onClick={handleApplyCoupon}
                  disabled={applyingCoupon}
                  className="bg-indigo-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-indigo-700 disabled:opacity-50"
                >
                  {applyingCoupon ? '...' : 'Appliquer'}
                </button>
              </div>
            )}
          </div>

          {/* Totals */}
          <div className="bg-white rounded-xl shadow-sm p-5 space-y-3">
            <h3 className="font-semibold text-gray-800 mb-3">Récapitulatif</h3>
            <div className="flex justify-between text-sm text-gray-600">
              <span>Sous-total</span>
              <span>{cart.sousTotal.toFixed(3)} TND</span>
            </div>
            {cart.remiseCoupon > 0 && (
              <div className="flex justify-between text-sm text-green-600">
                <span>Remise coupon</span>
                <span>-{cart.remiseCoupon.toFixed(3)} TND</span>
              </div>
            )}
            <div className="flex justify-between text-sm text-gray-600">
              <span>Frais de livraison</span>
              <span>{cart.fraisLivraison.toFixed(3)} TND</span>
            </div>
            <div className="border-t pt-3 flex justify-between font-bold text-gray-900">
              <span>Total TTC</span>
              <span className="text-indigo-600 text-lg">{cart.totalTTC.toFixed(3)} TND</span>
            </div>

            <Link
              href="/checkout"
              className="block w-full bg-indigo-600 text-white py-3 rounded-xl font-semibold text-center hover:bg-indigo-700 transition-colors mt-4"
            >
              Commander
            </Link>
            <Link href="/catalogue" className="block text-center text-sm text-indigo-600 hover:underline">
              Continuer mes achats
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

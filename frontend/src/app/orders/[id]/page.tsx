'use client';

import { useEffect, useState } from 'react';
import { useParams, useSearchParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { CheckCircle, MapPin, Package } from 'lucide-react';
import { orderService } from '@/services/orderService';
import { useAuthStore } from '@/store/authStore';
import { Order } from '@/types';
import OrderStatusBadge from '@/components/ui/OrderStatusBadge';
import LoadingSpinner from '@/components/ui/LoadingSpinner';
import toast from 'react-hot-toast';

export default function OrderDetailPage() {
  const { id } = useParams();
  const searchParams = useSearchParams();
  const router = useRouter();
  const { isAuthenticated } = useAuthStore();
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const isSuccess = searchParams.get('success') === 'true';

  useEffect(() => {
    if (!isAuthenticated) { router.push('/auth/login'); return; }
    orderService.getOrder(Number(id))
      .then(setOrder)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [id, isAuthenticated, router]);

  const handleCancel = async () => {
    if (!confirm('Annuler cette commande ?')) return;
    try {
      const updated = await orderService.cancelOrder(Number(id));
      setOrder(updated);
      toast.success('Commande annulée');
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Impossible d\'annuler');
    }
  };

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;
  if (!order) return <div className="text-center py-20 text-gray-500">Commande non trouvée</div>;

  const canCancel = order.statut === 'PENDING' || order.statut === 'PAID';

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      {/* Success Banner */}
      {isSuccess && (
        <div className="bg-green-50 border border-green-200 rounded-xl p-5 mb-6 flex items-center space-x-3">
          <CheckCircle className="h-8 w-8 text-green-500 flex-shrink-0" />
          <div>
            <p className="font-semibold text-green-800">Commande confirmée !</p>
            <p className="text-sm text-green-600">Votre commande {order.numeroCommande} a été passée avec succès.</p>
          </div>
        </div>
      )}

      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-xl font-bold text-gray-800">{order.numeroCommande}</h1>
          <p className="text-sm text-gray-500">
            {new Date(order.dateCommande).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' })}
          </p>
        </div>
        <OrderStatusBadge status={order.statut} />
      </div>

      {/* Order Items */}
      <div className="bg-white rounded-xl shadow-sm p-5 mb-4">
        <h2 className="font-semibold text-gray-800 mb-4">Articles commandés</h2>
        <div className="space-y-3">
          {order.lignes.map((item) => (
            <div key={item.id} className="flex items-center space-x-3">
              <img
                src={item.imageProduit || 'https://via.placeholder.com/60x60'}
                alt={item.nomProduit}
                className="w-14 h-14 object-cover rounded-lg"
              />
              <div className="flex-1">
                <Link href={`/produits/${item.productId}`} className="font-medium text-gray-800 hover:text-indigo-600 text-sm">
                  {item.nomProduit}
                </Link>
                {item.variantInfo && <p className="text-xs text-gray-500">{item.variantInfo}</p>}
                <p className="text-xs text-gray-900">x{item.quantite} × {item.prixUnitaire.toFixed(3)} TND</p>
              </div>
              <p className="font-semibold text-gray-800">{item.sousTotal.toFixed(3)} TND</p>
            </div>
          ))}
        </div>
      </div>

      {/* Delivery Address */}
      <div className="bg-white rounded-xl shadow-sm p-5 mb-4">
        <h2 className="font-semibold text-gray-800 mb-3 flex items-center space-x-2">
          <MapPin className="h-4 w-4 text-indigo-600" />
          <span>Adresse de livraison</span>
        </h2>
        <p className="text-sm text-gray-700">{order.adresseLivraisonRue}</p>
        <p className="text-sm text-gray-700">{order.adresseLivraisonCodePostal} {order.adresseLivraisonVille}</p>
        <p className="text-sm text-gray-700">{order.adresseLivraisonPays}</p>
      </div>

      {/* Totals */}
      <div className="bg-white rounded-xl shadow-sm p-5 mb-4">
        <h2 className="font-semibold text-gray-800 mb-3">Récapitulatif financier</h2>
        <div className="space-y-2 text-sm">
          <div className="flex justify-between text-gray-600">
            <span>Sous-total</span>
            <span>{order.sousTotal?.toFixed(3)} TND</span>
          </div>
          {order.remiseCoupon > 0 && (
            <div className="flex justify-between text-green-600">
              <span>Remise coupon</span>
              <span>-{order.remiseCoupon.toFixed(3)} TND</span>
            </div>
          )}
          <div className="flex justify-between text-gray-600">
            <span>Frais de livraison</span>
            <span>{order.fraisLivraison?.toFixed(3)} TND</span>
          </div>
          <div className="flex justify-between font-bold text-gray-900 text-base border-t pt-2">
            <span>Total TTC</span>
            <span className="text-indigo-600">{order.totalTTC?.toFixed(3)} TND</span>
          </div>
        </div>
      </div>

      {/* Actions */}
      <div className="flex space-x-3">
        <Link href="/orders" className="flex-1 border border-gray-300 text-gray-700 py-2.5 rounded-xl text-center text-sm font-medium hover:bg-gray-50">
          Mes commandes
        </Link>
        {canCancel && (
          <button
            onClick={handleCancel}
            className="flex-1 border border-red-300 text-red-600 py-2.5 rounded-xl text-sm font-medium hover:bg-red-50"
          >
            Annuler la commande
          </button>
        )}
      </div>
    </div>
  );
}

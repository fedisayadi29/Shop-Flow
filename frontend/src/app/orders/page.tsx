'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Package } from 'lucide-react';
import { orderService } from '@/services/orderService';
import { useAuthStore } from '@/store/authStore';
import { Order, PageResponse } from '@/types';
import OrderStatusBadge from '@/components/ui/OrderStatusBadge';
import LoadingSpinner from '@/components/ui/LoadingSpinner';

export default function OrdersPage() {
  const { isAuthenticated, user } = useAuthStore();
  const router = useRouter();
  const [orders, setOrders] = useState<PageResponse<Order> | null>(null);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);

  useEffect(() => {
    if (!isAuthenticated) { router.push('/auth/login'); return; }
    orderService.getMyOrders(page)
      .then(setOrders)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [isAuthenticated, router, page]);

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Mes commandes</h1>

      {orders?.content.length === 0 ? (
        <div className="text-center py-16">
          <Package className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-500">Aucune commande pour le moment</p>
          <Link href="/catalogue" className="mt-4 inline-block text-indigo-600 hover:underline">
            Commencer mes achats
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {orders?.content.map((order) => (
            <Link key={order.id} href={`/orders/${order.id}`}>
              <div className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow border border-gray-100">
                <div className="flex items-center justify-between mb-3">
                  <div>
                    <p className="font-semibold text-gray-800">{order.numeroCommande}</p>
                    <p className="text-sm text-gray-500">
                      {new Date(order.dateCommande).toLocaleDateString('fr-FR', {
                        day: 'numeric', month: 'long', year: 'numeric'
                      })}
                    </p>
                  </div>
                  <div className="text-right">
                    <OrderStatusBadge status={order.statut} />
                    <p className="text-lg font-bold text-indigo-600 mt-1">{order.totalTTC.toFixed(2)} TND</p>
                  </div>
                </div>

                <div className="flex items-center space-x-2 overflow-x-auto">
                  {order.lignes.slice(0, 4).map((item) => (
                    <img
                      key={item.id}
                      src={item.imageProduit || 'https://via.placeholder.com/50x50'}
                      alt={item.nomProduit}
                      className="w-12 h-12 object-cover rounded-lg flex-shrink-0"
                      title={item.nomProduit}
                    />
                  ))}
                  {order.lignes.length > 4 && (
                    <span className="text-sm text-gray-500">+{order.lignes.length - 4} autres</span>
                  )}
                </div>
              </div>
            </Link>
          ))}

          {/* Pagination */}
          {orders && orders.totalPages > 1 && (
            <div className="flex justify-center space-x-2 mt-6">
              <button disabled={orders.first} onClick={() => setPage(page - 1)} className="px-4 py-2 border rounded-lg text-sm disabled:opacity-50 text-black font-medium hover:bg-gray-100">
                Précédent
              </button>
              <span className="px-4 py-2 text-sm text-gray-600">Page {orders.page + 1} / {orders.totalPages}</span>
              <button disabled={orders.last} onClick={() => setPage(page + 1)} className="px-4 py-2 border rounded-lg text-sm disabled:opacity-50 text-black font-medium hover:bg-gray-100">
                Suivant
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

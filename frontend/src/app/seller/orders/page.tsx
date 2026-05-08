'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ShoppingBag, Search, Filter } from 'lucide-react';
import api from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import { Order, PageResponse } from '@/types';
import OrderStatusBadge from '@/components/ui/OrderStatusBadge';
import LoadingSpinner from '@/components/ui/LoadingSpinner';

export default function SellerOrdersPage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [orders, setOrders] = useState<PageResponse<Order> | null>(null);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [page, setPage] = useState(0);

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'SELLER') {
      router.push('/auth/login');
      return;
    }
    loadOrders();
  }, [isAuthenticated, user, router, page, statusFilter]);

  const loadOrders = () => {
    setLoading(true);
    const params = new URLSearchParams({
      page: page.toString(),
      size: '20',
      sellerId: user?.id?.toString() || '',
    });
    if (statusFilter) params.append('statut', statusFilter);

    api.get(`/api/orders?${params.toString()}`)
      .then((r) => setOrders(r.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const handleStatusUpdate = async (orderId: number, newStatus: string) => {
    try {
      await api.put(`/api/orders/${orderId}/status`, { statut: newStatus });
      loadOrders(); // Reload orders
    } catch (error) {
      console.error('Error updating status:', error);
    }
  };

  const filteredOrders = orders?.content.filter((o) =>
    o.numeroCommande.toLowerCase().includes(search.toLowerCase()) ||
    o.client?.email.toLowerCase().includes(search.toLowerCase())
  );

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center space-x-3">
          <ShoppingBag className="h-8 w-8 text-indigo-600" />
          <div>
            <h1 className="text-2xl font-bold text-gray-800">Mes commandes reçues</h1>
            <p className="text-sm text-gray-500">{orders?.totalElements || 0} commandes</p>
          </div>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl shadow-sm p-4 mb-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Rechercher par numéro ou email client..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div className="relative">
            <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
            <select
              value={statusFilter}
              onChange={(e) => { setStatusFilter(e.target.value); setPage(0); }}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 appearance-none"
            >
              <option value="">Tous les statuts</option>
              <option value="EN_ATTENTE">En attente</option>
              <option value="CONFIRMEE">Confirmée</option>
              <option value="EN_PREPARATION">En préparation</option>
              <option value="EXPEDIEE">Expédiée</option>
              <option value="LIVREE">Livrée</option>
              <option value="ANNULEE">Annulée</option>
            </select>
          </div>
        </div>
      </div>

      {/* Orders Table */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Commande
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Client
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Statut
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {filteredOrders?.map((order) => (
                <tr key={order.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{order.numeroCommande}</div>
                    <div className="text-xs text-gray-500">{order.lignes.length} article(s)</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">
                      {order.client?.prenom} {order.client?.nom}
                    </div>
                    <div className="text-xs text-gray-500">{order.client?.email}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {new Date(order.dateCommande).toLocaleDateString('fr-FR')}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <OrderStatusBadge status={order.statut} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-semibold text-gray-900">{order.totalTTC.toFixed(2)} TND</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm space-x-2">
                    <Link
                      href={`/orders/${order.id}`}
                      className="text-indigo-600 hover:text-indigo-900 font-medium"
                    >
                      Détails
                    </Link>
                    {order.statut === 'EN_ATTENTE' && (
                      <button
                        onClick={() => handleStatusUpdate(order.id, 'CONFIRMEE')}
                        className="text-green-600 hover:text-green-900 font-medium"
                      >
                        Confirmer
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {filteredOrders?.length === 0 && (
          <div className="text-center py-12">
            <ShoppingBag className="h-12 w-12 text-gray-300 mx-auto mb-3" />
            <p className="text-gray-500">Aucune commande trouvée</p>
          </div>
        )}

        {/* Pagination */}
        {orders && orders.totalPages > 1 && (
          <div className="bg-gray-50 px-6 py-4 flex items-center justify-between border-t border-gray-200">
            <div className="text-sm text-black">
              Page {orders.page + 1} sur {orders.totalPages}
            </div>
            <div className="flex space-x-2">
              <button
                disabled={orders.first}
                onClick={() => setPage(page - 1)}
                className="px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-black hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Précédent
              </button>
              <button
                disabled={orders.last}
                onClick={() => setPage(page + 1)}
                className="px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-black hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Suivant
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}


'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Package, ShoppingBag, TrendingUp, AlertTriangle, Plus } from 'lucide-react';
import api from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import { DashboardSellerResponse } from '@/types/dashboard';
import OrderStatusBadge from '@/components/ui/OrderStatusBadge';
import LoadingSpinner from '@/components/ui/LoadingSpinner';

export default function SellerDashboardPage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [dashboard, setDashboard] = useState<DashboardSellerResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'SELLER') {
      router.push('/auth/login');
      return;
    }
    api.get('/api/dashboard/seller')
      .then((r) => setDashboard(r.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [isAuthenticated, user, router]);

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Tableau de bord vendeur</h1>
          <p className="text-gray-500">{user?.sellerProfile?.nomBoutique || `${user?.prenom} ${user?.nom}`}</p>
        </div>
        <Link
          href="/seller/products/new"
          className="bg-indigo-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-indigo-700 flex items-center space-x-2"
        >
          <Plus className="h-4 w-4" />
          <span>Nouveau produit</span>
        </Link>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Revenus</p>
              <p className="text-2xl font-bold text-gray-800">{dashboard?.revenus?.toFixed(3) || '0.000'} TND</p>
            </div>
            <TrendingUp className="h-8 w-8 text-green-500" />
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Commandes en attente</p>
              <p className="text-2xl font-bold text-gray-800">{dashboard?.commandesEnAttente || 0}</p>
            </div>
            <ShoppingBag className="h-8 w-8 text-yellow-500" />
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Produits actifs</p>
              <p className="text-2xl font-bold text-gray-800">{dashboard?.totalProduits || 0}</p>
            </div>
            <Package className="h-8 w-8 text-indigo-500" />
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Alertes stock</p>
              <p className="text-2xl font-bold text-red-600">{dashboard?.alertesStockFaible || 0}</p>
            </div>
            <AlertTriangle className="h-8 w-8 text-red-500" />
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Low Stock Products */}
        {dashboard?.produitsStockFaible && dashboard.produitsStockFaible.length > 0 && (
          <div className="bg-white rounded-xl shadow-sm p-5">
            <h2 className="font-semibold text-gray-800 mb-4 flex items-center space-x-2">
              <AlertTriangle className="h-5 w-5 text-red-500" />
              <span>Stock faible</span>
            </h2>
            <div className="space-y-3">
              {dashboard.produitsStockFaible.map((p) => (
                <div key={p.id} className="flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    <img src={p.images?.[0] || 'https://via.placeholder.com/40'} alt={p.nom} className="w-10 h-10 object-cover rounded-lg" />
                    <p className="text-sm font-medium text-gray-800">{p.nom}</p>
                  </div>
                  <span className="text-sm font-bold text-red-600">{p.stock} restants</span>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Recent Orders */}
        <div className="bg-white rounded-xl shadow-sm p-5">
          <h2 className="font-semibold text-gray-800 mb-4">Commandes récentes</h2>
          {dashboard?.commandesRecentes?.length === 0 ? (
            <p className="text-gray-500 text-sm">Aucune commande</p>
          ) : (
            <div className="space-y-3">
              {dashboard?.commandesRecentes?.slice(0, 5).map((order) => (
                <div key={order.id} className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{order.numeroCommande}</p>
                    <p className="text-xs text-gray-500">{new Date(order.dateCommande).toLocaleDateString('fr-FR')}</p>
                  </div>
                  <div className="text-right">
                    <OrderStatusBadge status={order.statut} />
                    <p className="text-sm font-semibold text-gray-800 mt-1">{order.totalTTC?.toFixed(3)} TND</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Quick Links */}
      <div className="grid grid-cols-2 gap-4 mt-6">
        <Link href="/seller/products" className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow flex items-center space-x-3">
          <Package className="h-8 w-8 text-indigo-600" />
          <div>
            <p className="font-semibold text-gray-800">Mes produits</p>
            <p className="text-xs text-gray-500">Gérer le catalogue</p>
          </div>
        </Link>
        <Link href="/seller/orders" className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow flex items-center space-x-3">
          <ShoppingBag className="h-8 w-8 text-green-600" />
          <div>
            <p className="font-semibold text-gray-800">Commandes reçues</p>
            <p className="text-xs text-gray-500">Gérer les commandes</p>
          </div>
        </Link>
      </div>
    </div>
  );
}

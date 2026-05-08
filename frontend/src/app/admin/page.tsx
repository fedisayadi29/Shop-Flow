'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Users, Package, ShoppingBag, TrendingUp, Tag } from 'lucide-react';
import api from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import { DashboardAdminResponse } from '@/types/dashboard';
import OrderStatusBadge from '@/components/ui/OrderStatusBadge';
import LoadingSpinner from '@/components/ui/LoadingSpinner';

export default function AdminDashboardPage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [dashboard, setDashboard] = useState<DashboardAdminResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'ADMIN') { router.push('/auth/login'); return; }
    api.get('/api/dashboard/admin')
      .then((r) => setDashboard(r.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [isAuthenticated, user, router]);

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Administration ShopFlow</h1>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Chiffre d&apos;affaires</p>
              <p className="text-2xl font-bold text-gray-800">{dashboard?.chiffreAffairesGlobal?.toFixed(3) || '0.000'} TND</p>
            </div>
            <TrendingUp className="h-8 w-8 text-green-500" />
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Commandes</p>
              <p className="text-2xl font-bold text-gray-800">{dashboard?.totalCommandes || 0}</p>
            </div>
            <ShoppingBag className="h-8 w-8 text-blue-500" />
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Utilisateurs</p>
              <p className="text-2xl font-bold text-gray-800">{dashboard?.totalUtilisateurs || 0}</p>
            </div>
            <Users className="h-8 w-8 text-purple-500" />
          </div>
        </div>
        <div className="bg-white rounded-xl shadow-sm p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Produits</p>
              <p className="text-2xl font-bold text-gray-800">{dashboard?.totalProduits || 0}</p>
            </div>
            <Package className="h-8 w-8 text-indigo-500" />
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        {/* Recent Orders */}
        <div className="bg-white rounded-xl shadow-sm p-5">
          <h2 className="font-semibold text-gray-800 mb-4">Commandes récentes</h2>
          <div className="space-y-3">
            {dashboard?.commandesRecentes?.slice(0, 8).map((order) => (
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
        </div>

        {/* Top Products */}
        <div className="bg-white rounded-xl shadow-sm p-5">
          <h2 className="font-semibold text-gray-800 mb-4">Top produits</h2>
          <div className="space-y-3">
            {dashboard?.topProduits?.map((p, i) => (
              <div key={p.id} className="flex items-center space-x-3">
                <span className="text-sm font-bold text-gray-400 w-5">#{i + 1}</span>
                <img src={p.images?.[0] || 'https://via.placeholder.com/40'} alt={p.nom} className="w-10 h-10 object-cover rounded-lg" />
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-gray-800 truncate">{p.nom}</p>
                  <p className="text-xs text-gray-500">{p.nombreVentes} ventes</p>
                </div>
                <p className="text-sm font-semibold text-indigo-600">{p.prix.toFixed(3)} TND</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Admin Quick Links */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <Link href="/admin/users" className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow flex items-center space-x-3">
          <Users className="h-8 w-8 text-purple-600" />
          <div>
            <p className="font-semibold text-gray-800">Utilisateurs</p>
            <p className="text-xs text-gray-500">Gérer les comptes</p>
          </div>
        </Link>
        <Link href="/admin/orders" className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow flex items-center space-x-3">
          <ShoppingBag className="h-8 w-8 text-blue-600" />
          <div>
            <p className="font-semibold text-gray-800">Commandes</p>
            <p className="text-xs text-gray-500">Toutes les commandes</p>
          </div>
        </Link>
        <Link href="/admin/categories" className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow flex items-center space-x-3">
          <Package className="h-8 w-8 text-indigo-600" />
          <div>
            <p className="font-semibold text-gray-800">Catégories</p>
            <p className="text-xs text-gray-500">Gérer les catégories</p>
          </div>
        </Link>
        <Link href="/admin/coupons" className="bg-white rounded-xl shadow-sm p-5 hover:shadow-md transition-shadow flex items-center space-x-3">
          <Tag className="h-8 w-8 text-green-600" />
          <div>
            <p className="font-semibold text-gray-800">Coupons</p>
            <p className="text-xs text-gray-500">Codes promo</p>
          </div>
        </Link>
      </div>
    </div>
  );
}

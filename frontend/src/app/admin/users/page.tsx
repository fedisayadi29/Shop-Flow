'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Users, Search, Shield, Store, User as UserIcon } from 'lucide-react';
import api from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import LoadingSpinner from '@/components/ui/LoadingSpinner';

interface User {
  id: number;
  email: string;
  prenom: string;
  nom: string;
  role: string;
  actif: boolean;
  dateInscription: string;
  sellerProfile?: {
    nomBoutique: string;
  };
}

interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export default function AdminUsersPage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [users, setUsers] = useState<PageResponse<User> | null>(null);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'ADMIN') {
      router.push('/auth/login');
      return;
    }
    loadUsers();
  }, [isAuthenticated, user, router, page]);

  const loadUsers = () => {
    setLoading(true);
    api.get(`/api/users?page=${page}&size=20`)
      .then((r) => setUsers(r.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const getRoleIcon = (role: string) => {
    switch (role) {
      case 'ADMIN': return <Shield className="h-4 w-4 text-red-500" />;
      case 'SELLER': return <Store className="h-4 w-4 text-indigo-500" />;
      default: return <UserIcon className="h-4 w-4 text-gray-500" />;
    }
  };

  const getRoleBadge = (role: string) => {
    const colors = {
      ADMIN: 'bg-red-100 text-red-700',
      SELLER: 'bg-indigo-100 text-indigo-700',
      CLIENT: 'bg-gray-100 text-black',
    };
    return colors[role as keyof typeof colors] || colors.CLIENT;
  };

  const filteredUsers = users?.content.filter((u) =>
    u.email.toLowerCase().includes(search.toLowerCase()) ||
    u.prenom.toLowerCase().includes(search.toLowerCase()) ||
    u.nom.toLowerCase().includes(search.toLowerCase())
  );

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center space-x-3">
          <Users className="h-8 w-8 text-indigo-600" />
          <div>
            <h1 className="text-2xl font-bold text-gray-800">Gestion des utilisateurs</h1>
            <p className="text-sm text-gray-500">{users?.totalElements || 0} utilisateurs au total</p>
          </div>
        </div>
      </div>

      {/* Search */}
      <div className="bg-white rounded-xl shadow-sm p-4 mb-6">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
          <input
            type="text"
            placeholder="Rechercher par email, prénom ou nom..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>
      </div>

      {/* Users Table */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Utilisateur
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Email
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Rôle
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Statut
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Inscription
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {filteredUsers?.map((u) => (
                <tr key={u.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-10 w-10 bg-indigo-100 rounded-full flex items-center justify-center">
                        <span className="text-indigo-600 font-semibold">
                          {u.prenom[0]}{u.nom[0]}
                        </span>
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {u.prenom} {u.nom}
                        </div>
                        {u.sellerProfile?.nomBoutique && (
                          <div className="text-xs text-gray-500">
                            {u.sellerProfile.nomBoutique}
                          </div>
                        )}
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{u.email}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center space-x-2">
                      {getRoleIcon(u.role)}
                      <span className={`px-2 py-1 text-xs font-medium rounded-full ${getRoleBadge(u.role)}`}>
                        {u.role}
                      </span>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                      u.actif ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                    }`}>
                      {u.actif ? 'Actif' : 'Inactif'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {new Date(u.dateInscription).toLocaleDateString('fr-FR')}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        {users && users.totalPages > 1 && (
          <div className="bg-gray-50 px-6 py-4 flex items-center justify-between border-t border-gray-200">
            <div className="text-sm text-black">
              Page {users.page + 1} sur {users.totalPages}
            </div>
            <div className="flex space-x-2">
              <button
                disabled={users.first}
                onClick={() => setPage(page - 1)}
                className="px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-black hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Précédent
              </button>
              <button
                disabled={users.last}
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


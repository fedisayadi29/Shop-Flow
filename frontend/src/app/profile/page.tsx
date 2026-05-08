'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { User, MapPin, Package, Plus, Trash2 } from 'lucide-react';
import api from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import { Address } from '@/types';
import toast from 'react-hot-toast';

export default function ProfilePage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [newAddress, setNewAddress] = useState({ rue: '', ville: '', codePostal: '', pays: 'France', principal: false });

  useEffect(() => {
    if (!isAuthenticated) { router.push('/auth/login'); return; }
    api.get('/api/users/me/addresses').then((r) => setAddresses(r.data)).catch(console.error);
  }, [isAuthenticated, router]);

  const handleAddAddress = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const r = await api.post('/api/users/me/addresses', newAddress);
      setAddresses([...addresses, r.data]);
      setShowForm(false);
      setNewAddress({ rue: '', ville: '', codePostal: '', pays: 'France', principal: false });
      toast.success('Adresse ajoutée');
    } catch {
      toast.error('Erreur');
    }
  };

  const handleDeleteAddress = async (id: number) => {
    try {
      await api.delete(`/api/users/me/addresses/${id}`);
      setAddresses(addresses.filter((a) => a.id !== id));
      toast.success('Adresse supprimée');
    } catch {
      toast.error('Erreur');
    }
  };

  if (!user) return null;

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Mon profil</h1>

      {/* User Info */}
      <div className="bg-white rounded-xl shadow-sm p-6 mb-6">
        <div className="flex items-center space-x-4 mb-4">
          <div className="w-16 h-16 bg-indigo-100 rounded-full flex items-center justify-center">
            <User className="h-8 w-8 text-indigo-600" />
          </div>
          <div>
            <h2 className="text-xl font-semibold text-gray-800">{user.prenom} {user.nom}</h2>
            <p className="text-gray-500">{user.email}</p>
            <span className="text-xs bg-indigo-100 text-indigo-700 px-2 py-0.5 rounded-full font-medium">
              {user.role === 'CUSTOMER' ? 'Client' : user.role === 'SELLER' ? 'Vendeur' : 'Admin'}
            </span>
          </div>
        </div>
      </div>

      {/* Quick Links */}
      {user.role === 'CUSTOMER' && (
        <div className="grid grid-cols-2 gap-4 mb-6">
          <Link href="/orders" className="bg-white rounded-xl shadow-sm p-5 flex items-center space-x-3 hover:shadow-md transition-shadow">
            <Package className="h-8 w-8 text-indigo-600" />
            <div>
              <p className="font-semibold text-gray-800">Mes commandes</p>
              <p className="text-xs text-gray-500">Suivre mes achats</p>
            </div>
          </Link>
          <Link href="/cart" className="bg-white rounded-xl shadow-sm p-5 flex items-center space-x-3 hover:shadow-md transition-shadow">
            <MapPin className="h-8 w-8 text-green-600" />
            <div>
              <p className="font-semibold text-gray-800">Mon panier</p>
              <p className="text-xs text-gray-500">Voir le panier</p>
            </div>
          </Link>
        </div>
      )}

      {/* Addresses */}
      {user.role === 'CUSTOMER' && (
        <div className="bg-white rounded-xl shadow-sm p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="font-semibold text-gray-800 flex items-center space-x-2">
              <MapPin className="h-5 w-5 text-indigo-600" />
              <span>Mes adresses</span>
            </h2>
            <button
              onClick={() => setShowForm(!showForm)}
              className="text-sm text-indigo-600 hover:underline flex items-center space-x-1"
            >
              <Plus className="h-4 w-4" />
              <span>Ajouter</span>
            </button>
          </div>

          {showForm && (
            <form onSubmit={handleAddAddress} className="bg-gray-50 rounded-xl p-4 mb-4 space-y-3">
              <input
                placeholder="Rue"
                value={newAddress.rue}
                onChange={(e) => setNewAddress({ ...newAddress, rue: e.target.value })}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
              <div className="grid grid-cols-2 gap-2">
                <input
                  placeholder="Code postal"
                  value={newAddress.codePostal}
                  onChange={(e) => setNewAddress({ ...newAddress, codePostal: e.target.value })}
                  required
                  className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
                <input
                  placeholder="Ville"
                  value={newAddress.ville}
                  onChange={(e) => setNewAddress({ ...newAddress, ville: e.target.value })}
                  required
                  className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <input
                placeholder="Pays"
                value={newAddress.pays}
                onChange={(e) => setNewAddress({ ...newAddress, pays: e.target.value })}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
              <label className="flex items-center space-x-2 text-sm">
                <input
                  type="checkbox"
                  checked={newAddress.principal}
                  onChange={(e) => setNewAddress({ ...newAddress, principal: e.target.checked })}
                  className="rounded text-indigo-600"
                />
                <span>Adresse principale</span>
              </label>
              <button type="submit" className="w-full bg-indigo-600 text-white py-2 rounded-lg text-sm font-medium hover:bg-indigo-700">
                Enregistrer
              </button>
            </form>
          )}

          {addresses.length === 0 ? (
            <p className="text-gray-500 text-sm text-center py-4">Aucune adresse enregistrée</p>
          ) : (
            <div className="space-y-3">
              {addresses.map((addr) => (
                <div key={addr.id} className="flex items-start justify-between border border-gray-200 rounded-xl p-3">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{addr.rue}</p>
                    <p className="text-xs text-gray-500">{addr.codePostal} {addr.ville}, {addr.pays}</p>
                    {addr.principal && <span className="text-xs text-indigo-600 font-medium">Principale</span>}
                  </div>
                  <button onClick={() => handleDeleteAddress(addr.id)} className="text-red-400 hover:text-red-600">
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

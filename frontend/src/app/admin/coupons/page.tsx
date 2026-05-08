'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Tag, Plus, Edit, Trash2, Copy, Check } from 'lucide-react';
import api from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import LoadingSpinner from '@/components/ui/LoadingSpinner';
import toast from 'react-hot-toast';

interface Coupon {
  id: number;
  code: string;
  type: 'PERCENT' | 'FIXED';
  valeur: number;
  usagesMax: number;
  usagesActuels: number;
  actif: boolean;
  dateDebut?: string;
  dateFin?: string;
}

export default function AdminCouponsPage() {
  const { user, isAuthenticated } = useAuthStore();
  const router = useRouter();
  const [coupons, setCoupons] = useState<Coupon[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingCoupon, setEditingCoupon] = useState<Coupon | null>(null);
  const [copiedCode, setCopiedCode] = useState<string | null>(null);

  const [form, setForm] = useState({
    code: '',
    type: 'PERCENT' as 'PERCENT' | 'FIXED',
    valeur: '',
    usagesMax: '100',
    actif: true,
  });

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'ADMIN') {
      router.push('/auth/login');
      return;
    }
    loadCoupons();
  }, [isAuthenticated, user, router]);

  const loadCoupons = async () => {
    try {
      const response = await api.get('/api/coupons');
      setCoupons(response.data);
    } catch (error) {
      console.error('Error loading coupons:', error);
      toast.error('Erreur lors du chargement des coupons');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const data = {
        code: form.code.toUpperCase(),
        type: form.type,
        valeur: parseFloat(form.valeur),
        usagesMax: parseInt(form.usagesMax),
        actif: form.actif,
      };

      if (editingCoupon) {
        await api.put(`/api/coupons/${editingCoupon.id}`, data);
        toast.success('Coupon modifié avec succès');
      } else {
        await api.post('/api/coupons', data);
        toast.success('Coupon créé avec succès');
      }

      setShowModal(false);
      setEditingCoupon(null);
      resetForm();
      loadCoupons();
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Erreur lors de l\'enregistrement');
    }
  };

  const handleEdit = (coupon: Coupon) => {
    setEditingCoupon(coupon);
    setForm({
      code: coupon.code,
      type: coupon.type,
      valeur: coupon.valeur.toString(),
      usagesMax: coupon.usagesMax.toString(),
      actif: coupon.actif,
    });
    setShowModal(true);
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Supprimer ce coupon ?')) return;
    try {
      await api.delete(`/api/coupons/${id}`);
      toast.success('Coupon supprimé');
      loadCoupons();
    } catch (error) {
      toast.error('Erreur lors de la suppression');
    }
  };

  const handleToggleStatus = async (coupon: Coupon) => {
    try {
      await api.put(`/api/coupons/${coupon.id}`, { ...coupon, actif: !coupon.actif });
      toast.success(coupon.actif ? 'Coupon désactivé' : 'Coupon activé');
      loadCoupons();
    } catch (error) {
      toast.error('Erreur');
    }
  };

  const copyCode = (code: string) => {
    navigator.clipboard.writeText(code);
    setCopiedCode(code);
    toast.success('Code copié !');
    setTimeout(() => setCopiedCode(null), 2000);
  };

  const resetForm = () => {
    setForm({
      code: '',
      type: 'PERCENT',
      valeur: '',
      usagesMax: '100',
      actif: true,
    });
  };

  const openNewModal = () => {
    setEditingCoupon(null);
    resetForm();
    setShowModal(true);
  };

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center space-x-3">
          <Tag className="h-8 w-8 text-green-600" />
          <div>
            <h1 className="text-2xl font-bold text-gray-800">Gestion des Coupons</h1>
            <p className="text-sm text-gray-500">{coupons.length} coupon(s) au total</p>
          </div>
        </div>
        <button
          onClick={openNewModal}
          className="bg-green-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-green-700 flex items-center space-x-2"
        >
          <Plus className="h-4 w-4" />
          <span>Nouveau coupon</span>
        </button>
      </div>

      {/* Coupons Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {coupons.map((coupon) => (
          <div
            key={coupon.id}
            className={`bg-white rounded-xl shadow-sm p-5 border-2 ${
              coupon.actif ? 'border-green-200' : 'border-gray-200'
            }`}
          >
            <div className="flex items-start justify-between mb-3">
              <div className="flex items-center space-x-2">
                <Tag className={`h-5 w-5 ${coupon.actif ? 'text-green-600' : 'text-gray-400'}`} />
                <span className={`text-xs px-2 py-1 rounded-full font-medium ${
                  coupon.actif ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-600'
                }`}>
                  {coupon.actif ? 'Actif' : 'Inactif'}
                </span>
              </div>
              <div className="flex items-center space-x-1">
                <button
                  onClick={() => handleEdit(coupon)}
                  className="text-gray-400 hover:text-indigo-600 p-1"
                >
                  <Edit className="h-4 w-4" />
                </button>
                <button
                  onClick={() => handleDelete(coupon.id)}
                  className="text-gray-400 hover:text-red-600 p-1"
                >
                  <Trash2 className="h-4 w-4" />
                </button>
              </div>
            </div>

            <div className="mb-3">
              <div className="flex items-center justify-between mb-1">
                <span className="text-2xl font-bold text-gray-800">{coupon.code}</span>
                <button
                  onClick={() => copyCode(coupon.code)}
                  className="text-gray-400 hover:text-indigo-600"
                >
                  {copiedCode === coupon.code ? (
                    <Check className="h-4 w-4 text-green-600" />
                  ) : (
                    <Copy className="h-4 w-4" />
                  )}
                </button>
              </div>
              <p className="text-sm text-gray-600">
                {coupon.type === 'PERCENT' ? (
                  <span className="text-green-600 font-semibold">{coupon.valeur}% de réduction</span>
                ) : (
                  <span className="text-green-600 font-semibold">{coupon.valeur.toFixed(3)} TND de réduction</span>
                )}
              </p>
            </div>

            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-gray-600">Utilisations:</span>
                <span className="font-medium text-gray-800">
                  {coupon.usagesActuels} / {coupon.usagesMax}
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-green-600 h-2 rounded-full transition-all"
                  style={{ width: `${(coupon.usagesActuels / coupon.usagesMax) * 100}%` }}
                />
              </div>
            </div>

            <button
              onClick={() => handleToggleStatus(coupon)}
              className={`w-full mt-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                coupon.actif
                  ? 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                  : 'bg-green-100 text-green-700 hover:bg-green-200'
              }`}
            >
              {coupon.actif ? 'Désactiver' : 'Activer'}
            </button>
          </div>
        ))}
      </div>

      {coupons.length === 0 && (
        <div className="text-center py-16">
          <Tag className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-500 mb-4">Aucun coupon créé</p>
          <button
            onClick={openNewModal}
            className="text-green-600 hover:underline font-medium"
          >
            Créer votre premier coupon
          </button>
        </div>
      )}

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl max-w-md w-full p-6">
            <h2 className="text-xl font-bold text-gray-800 mb-4">
              {editingCoupon ? 'Modifier le coupon' : 'Nouveau coupon'}
            </h2>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Code *</label>
                <input
                  type="text"
                  value={form.code}
                  onChange={(e) => setForm({ ...form, code: e.target.value.toUpperCase() })}
                  required
                  placeholder="PROMO2024"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 uppercase"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Type *</label>
                <select
                  value={form.type}
                  onChange={(e) => setForm({ ...form, type: e.target.value as 'PERCENT' | 'FIXED' })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="PERCENT">Pourcentage (%)</option>
                  <option value="FIXED">Montant fixe (TND)</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Valeur * {form.type === 'PERCENT' ? '(%)' : '(TND)'}
                </label>
                <input
                  type="number"
                  step={form.type === 'PERCENT' ? '1' : '0.001'}
                  min="0"
                  max={form.type === 'PERCENT' ? '100' : undefined}
                  value={form.valeur}
                  onChange={(e) => setForm({ ...form, valeur: e.target.value })}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Utilisations max *</label>
                <input
                  type="number"
                  min="1"
                  value={form.usagesMax}
                  onChange={(e) => setForm({ ...form, usagesMax: e.target.value })}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>

              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="actif"
                  checked={form.actif}
                  onChange={(e) => setForm({ ...form, actif: e.target.checked })}
                  className="w-4 h-4 text-green-600 border-gray-300 rounded focus:ring-green-500"
                />
                <label htmlFor="actif" className="text-sm font-medium text-gray-700">
                  Actif
                </label>
              </div>

              <div className="flex space-x-3 pt-2">
                <button
                  type="button"
                  onClick={() => {
                    setShowModal(false);
                    setEditingCoupon(null);
                    resetForm();
                  }}
                  className="flex-1 border border-gray-300 text-gray-700 py-2 rounded-lg text-sm font-medium hover:bg-gray-50"
                >
                  Annuler
                </button>
                <button
                  type="submit"
                  className="flex-1 bg-green-600 text-white py-2 rounded-lg text-sm font-semibold hover:bg-green-700"
                >
                  {editingCoupon ? 'Modifier' : 'Créer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Package } from 'lucide-react';
import { authService } from '@/services/authService';
import { useAuthStore } from '@/store/authStore';
import toast from 'react-hot-toast';

export default function RegisterPage() {
  const [form, setForm] = useState({
    email: '',
    motDePasse: '',
    prenom: '',
    nom: '',
    role: 'CUSTOMER',
    nomBoutique: '',
    descriptionBoutique: '',
  });
  const [loading, setLoading] = useState(false);
  const { setAuth } = useAuthStore();
  const router = useRouter();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    // Validation côté client
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    if (!passwordRegex.test(form.motDePasse)) {
      toast.error('Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre');
      return;
    }
    
    setLoading(true);
    try {
      const data = await authService.register(form);
      setAuth(data.user, data.accessToken, data.refreshToken);
      toast.success('Compte créé avec succès !');
      if (data.user.role === 'SELLER') router.push('/seller');
      else router.push('/');
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string; validationErrors?: Record<string, string> } } };
      
      // Afficher les erreurs de validation détaillées
      if (error.response?.data?.validationErrors) {
        const errors = error.response.data.validationErrors;
        Object.values(errors).forEach(msg => toast.error(msg as string));
      } else {
        toast.error(error.response?.data?.message || 'Erreur lors de l\'inscription');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <Package className="h-12 w-12 text-indigo-600 mx-auto" />
          <h2 className="mt-4 text-3xl font-bold text-gray-900">Créer un compte</h2>
          <p className="mt-2 text-gray-600">
            Déjà inscrit ?{' '}
            <Link href="/auth/login" className="text-indigo-600 hover:underline font-medium">
              Se connecter
            </Link>
          </p>
        </div>

        <form onSubmit={handleSubmit} className="bg-white shadow-md rounded-xl p-8 space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Prénom</label>
              <input
                name="prenom"
                value={form.prenom}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm text-black"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Nom</label>
              <input
                name="nom"
                value={form.nom}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm text-black"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-black"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Mot de passe</label>
            <input
              type="password"
              name="motDePasse"
              value={form.motDePasse}
              onChange={handleChange}
              required
              minLength={8}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-black"
              placeholder="Ex: Motdepasse123"
            />
            <p className="mt-1 text-xs text-gray-500">
              ✓ Minimum 8 caractères<br/>
              ✓ Au moins 1 majuscule (A-Z)<br/>
              ✓ Au moins 1 minuscule (a-z)<br/>
              ✓ Au moins 1 chiffre (0-9)
            </p>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Type de compte</label>
            <select
              name="role"
              value={form.role}
              onChange={handleChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-black"
            >
              <option value="CUSTOMER">Client</option>
              <option value="SELLER">Vendeur</option>
            </select>
          </div>

          {form.role === 'SELLER' && (
            <>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Nom de la boutique *</label>
                <input
                  name="nomBoutique"
                  value={form.nomBoutique}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-black"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Description boutique</label>
                <textarea
                  name="descriptionBoutique"
                  value={form.descriptionBoutique}
                  onChange={handleChange}
                  rows={3}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm text-black"
                />
              </div>
            </>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 disabled:opacity-50 transition-colors"
          >
            {loading ? 'Création...' : 'Créer mon compte'}
          </button>
        </form>
      </div>
    </div>
  );
}

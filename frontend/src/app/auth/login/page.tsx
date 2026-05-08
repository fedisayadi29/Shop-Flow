'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Package } from 'lucide-react';
import { authService } from '@/services/authService';
import { useAuthStore } from '@/store/authStore';
import toast from 'react-hot-toast';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const { setAuth } = useAuthStore();
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const data = await authService.login(email, password);
      setAuth(data.user, data.accessToken, data.refreshToken);
      toast.success(`Bienvenue, ${data.user.prenom} !`);

      // Redirect based on role
      if (data.user.role === 'ADMIN') router.push('/admin');
      else if (data.user.role === 'SELLER') router.push('/seller');
      else router.push('/');
    } catch (err: unknown) {
      console.error('Erreur de connexion:', err);
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Email ou mot de passe incorrect');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <Package className="h-12 w-12 text-indigo-600 mx-auto" />
          <h2 className="mt-4 text-3xl font-bold text-gray-900">Connexion</h2>
          <p className="mt-2 text-gray-900">
            Pas encore de compte ?{' '}
            <Link href="/auth/register" className="text-indigo-600 hover:underline font-medium">
              S&apos;inscrire
            </Link>
          </p>
        </div>

        <form onSubmit={handleSubmit} className="bg-white shadow-md rounded-xl p-8 space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-900 mb-1">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
              placeholder="vous@exemple.com"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-900 mb-1">Mot de passe</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 disabled:opacity-50 transition-colors"
          >
            {loading ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>
      </div>
    </div>
  );
}

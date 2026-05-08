'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { ShoppingCart, User, LogOut, Package, LayoutDashboard, Search } from 'lucide-react';
import { useAuthStore } from '@/store/authStore';
import { useCartStore } from '@/store/cartStore';
import { authService } from '@/services/authService';
import toast from 'react-hot-toast';
import { useState } from 'react';

export default function Navbar() {
  const { user, isAuthenticated, logout, refreshToken } = useAuthStore();
  const { itemCount } = useCartStore();
  const router = useRouter();
  const [searchQuery, setSearchQuery] = useState('');

  const handleLogout = async () => {
    try {
      if (refreshToken) {
        await authService.logout(refreshToken);
      }
    } catch {
      // ignore
    }
    logout();
    toast.success('Déconnexion réussie');
    router.push('/');
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      router.push(`/catalogue?q=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  return (
    <nav className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link href="/" className="flex items-center space-x-2">
            <Package className="h-8 w-8 text-indigo-600" />
            <span className="text-xl font-bold text-indigo-600">ShopFlow</span>
          </Link>

          {/* Search */}
          <form onSubmit={handleSearch} className="hidden md:flex flex-1 max-w-lg mx-8">
            <div className="relative w-full">
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Rechercher des produits..."
                className="w-full pl-4 pr-10 py-2 border border-gray-300 rounded-full text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
              <button type="submit" className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-indigo-600">
                <Search className="h-4 w-4" />
              </button>
            </div>
          </form>

          {/* Nav links */}
          <div className="flex items-center space-x-4">
            <Link href="/catalogue" className="text-gray-600 hover:text-indigo-600 text-sm font-medium">
              Catalogue
            </Link>

            {isAuthenticated ? (
              <>
                {user?.role === 'CUSTOMER' && (
                  <Link href="/cart" className="relative text-gray-600 hover:text-indigo-600">
                    <ShoppingCart className="h-6 w-6" />
                    {itemCount > 0 && (
                      <span className="absolute -top-2 -right-2 bg-indigo-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                        {itemCount > 99 ? '99+' : itemCount}
                      </span>
                    )}
                  </Link>
                )}

                {user?.role === 'ADMIN' && (
                  <Link href="/admin" className="text-gray-600 hover:text-indigo-600">
                    <LayoutDashboard className="h-6 w-6" />
                  </Link>
                )}

                {user?.role === 'SELLER' && (
                  <Link href="/seller" className="text-gray-600 hover:text-indigo-600">
                    <LayoutDashboard className="h-6 w-6" />
                  </Link>
                )}

                <div className="flex items-center space-x-2">
                  <Link href="/profile" className="flex items-center space-x-1 text-sm text-gray-700 hover:text-indigo-600">
                    <User className="h-5 w-5" />
                    <span className="hidden md:block">{user?.prenom}</span>
                  </Link>
                  <button
                    onClick={handleLogout}
                    className="text-gray-500 hover:text-red-500"
                    title="Déconnexion"
                  >
                    <LogOut className="h-5 w-5" />
                  </button>
                </div>
              </>
            ) : (
              <div className="flex items-center space-x-3">
                <Link
                  href="/auth/login"
                  className="text-sm text-gray-600 hover:text-indigo-600 font-medium"
                >
                  Connexion
                </Link>
                <Link
                  href="/auth/register"
                  className="text-sm bg-indigo-600 text-white px-4 py-2 rounded-full hover:bg-indigo-700 font-medium"
                >
                  S&apos;inscrire
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

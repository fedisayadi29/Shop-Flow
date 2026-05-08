'use client';

import { useEffect, useState, useCallback } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import { Search, SlidersHorizontal, X } from 'lucide-react';
import ProductCard from '@/components/ui/ProductCard';
import ProductCardSkeleton from '@/components/ui/ProductCardSkeleton';
import { productService } from '@/services/productService';
import { categoryService } from '@/services/categoryService';
import { Product, Category, PageResponse } from '@/types';

export default function CataloguePage() {
  const searchParams = useSearchParams();
  const router = useRouter();

  const [products, setProducts] = useState<PageResponse<Product> | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [showFilters, setShowFilters] = useState(false);

  const [filters, setFilters] = useState({
    q: searchParams.get('q') || '',
    categoryId: searchParams.get('categoryId') ? Number(searchParams.get('categoryId')) : undefined,
    prixMin: searchParams.get('prixMin') ? Number(searchParams.get('prixMin')) : undefined,
    prixMax: searchParams.get('prixMax') ? Number(searchParams.get('prixMax')) : undefined,
    promo: searchParams.get('promo') === 'true' ? true : undefined,
    sortBy: searchParams.get('sortBy') || 'nouveautes',
    page: 0,
    size: 12,
  });

  const fetchProducts = useCallback(async () => {
    setLoading(true);
    try {
      let result: PageResponse<Product>;
      if (filters.q) {
        result = await productService.searchProducts(filters.q, filters.page, filters.size);
      } else {
        result = await productService.getProducts(filters);
      }
      setProducts(result);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [filters]);

  useEffect(() => {
    categoryService.getCategories().then(setCategories).catch(console.error);
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const updateFilter = (key: string, value: unknown) => {
    setFilters((prev) => ({ ...prev, [key]: value, page: 0 }));
  };

  const clearFilters = () => {
    setFilters({ q: '', categoryId: undefined, prixMin: undefined, prixMax: undefined, promo: undefined, sortBy: 'nouveautes', page: 0, size: 12 });
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Catalogue</h1>
        <button
          onClick={() => setShowFilters(!showFilters)}
          className="flex items-center space-x-2 text-sm text-gray-600 hover:text-indigo-600 md:hidden"
        >
          <SlidersHorizontal className="h-4 w-4" />
          <span>Filtres</span>
        </button>
      </div>

      <div className="flex gap-6">
        {/* Sidebar Filters */}
        <aside className={`w-64 flex-shrink-0 ${showFilters ? 'block' : 'hidden md:block'}`}>
          <div className="bg-white rounded-xl shadow-sm p-5 space-y-5 sticky top-20">
            <div className="flex items-center justify-between">
              <h3 className="font-semibold text-gray-800">Filtres</h3>
              <button onClick={clearFilters} className="text-xs text-indigo-600 hover:underline">
                Réinitialiser
              </button>
            </div>

            {/* Search */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Recherche</label>
              <div className="relative">
                <input
                  type="text"
                  value={filters.q}
                  onChange={(e) => updateFilter('q', e.target.value)}
                  placeholder="Nom, description..."
                  className="w-full pl-8 pr-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
                <Search className="absolute left-2 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
              </div>
            </div>

            {/* Categories */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Catégorie</label>
              <div className="space-y-1 max-h-48 overflow-y-auto">
                <button
                  onClick={() => updateFilter('categoryId', undefined)}
                  className={`w-full text-left text-sm px-2 py-1 rounded ${!filters.categoryId ? 'text-indigo-600 font-medium' : 'text-gray-600 hover:text-indigo-600'}`}
                >
                  Toutes
                </button>
                {categories.map((cat) => (
                  <button
                    key={cat.id}
                    onClick={() => updateFilter('categoryId', cat.id)}
                    className={`w-full text-left text-sm px-2 py-1 rounded ${filters.categoryId === cat.id ? 'text-indigo-600 font-medium' : 'text-gray-600 hover:text-indigo-600'}`}
                  >
                    {cat.nom}
                  </button>
                ))}
              </div>
            </div>

            {/* Price Range */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Prix (€)</label>
              <div className="flex space-x-2">
                <input
                  type="number"
                  placeholder="Min"
                  value={filters.prixMin || ''}
                  onChange={(e) => updateFilter('prixMin', e.target.value ? Number(e.target.value) : undefined)}
                  className="w-1/2 px-2 py-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500"
                />
                <input
                  type="number"
                  placeholder="Max"
                  value={filters.prixMax || ''}
                  onChange={(e) => updateFilter('prixMax', e.target.value ? Number(e.target.value) : undefined)}
                  className="w-1/2 px-2 py-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500"
                />
              </div>
            </div>

            {/* Promo */}
            <div>
              <label className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={filters.promo === true}
                  onChange={(e) => updateFilter('promo', e.target.checked ? true : undefined)}
                  className="rounded text-indigo-600"
                />
                <span className="text-sm text-gray-700">Promotions uniquement</span>
              </label>
            </div>
          </div>
        </aside>

        {/* Products Grid */}
        <div className="flex-1">
          {/* Sort & Count */}
          <div className="flex items-center justify-between mb-4">
            <p className="text-sm text-gray-600">
              {products ? `${products.totalElements} produit(s)` : ''}
            </p>
            <select
              value={filters.sortBy}
              onChange={(e) => updateFilter('sortBy', e.target.value)}
              className="text-sm border border-gray-300 rounded-lg px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="nouveautes">Nouveautés</option>
              <option value="prix_asc">Prix croissant</option>
              <option value="prix_desc">Prix décroissant</option>
              <option value="popularite">Popularité</option>
              <option value="note">Meilleures notes</option>
            </select>
          </div>

          {loading ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {Array.from({ length: 12 }).map((_, i) => <ProductCardSkeleton key={i} />)}
            </div>
          ) : products?.content.length === 0 ? (
            <div className="text-center py-16">
              <p className="text-gray-500 text-lg">Aucun produit trouvé</p>
              <button onClick={clearFilters} className="mt-4 text-indigo-600 hover:underline">
                Réinitialiser les filtres
              </button>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {products?.content.map((product) => (
                  <ProductCard key={product.id} product={product} />
                ))}
              </div>

              {/* Pagination */}
              {products && products.totalPages > 1 && (
                <div className="flex justify-center space-x-2 mt-8">
                  <button
                    disabled={products.first}
                    onClick={() => updateFilter('page', filters.page - 1)}
                    className="px-4 py-2 border rounded-lg text-sm disabled:opacity-50 hover:bg-gray-50 text-black font-medium"
                  >
                    Précédent
                  </button>
                  <span className="px-4 py-2 text-sm text-gray-600">
                    Page {products.page + 1} / {products.totalPages}
                  </span>
                  <button
                    disabled={products.last}
                    onClick={() => updateFilter('page', filters.page + 1)}
                    className="px-4 py-2 border rounded-lg text-sm disabled:opacity-50 hover:bg-gray-50"
                  >
                    Suivant
                  </button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
}


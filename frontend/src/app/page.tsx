'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { ArrowRight, Tag, Zap, Shield, Truck } from 'lucide-react';
import ProductCard from '@/components/ui/ProductCard';
import ProductCardSkeleton from '@/components/ui/ProductCardSkeleton';
import { productService } from '@/services/productService';
import { categoryService } from '@/services/categoryService';
import { Product, Category } from '@/types';

export default function HomePage() {
  const [featuredProducts, setFeaturedProducts] = useState<Product[]>([]);
  const [promoProducts, setPromoProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [topRes, promoRes, catRes] = await Promise.all([
          productService.getTopSelling(),
          productService.getProducts({ promo: true, size: 4 }),
          categoryService.getCategories(),
        ]);
        setFeaturedProducts(topRes.slice(0, 8));
        setPromoProducts(promoRes.content);
        setCategories(catRes);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      {/* Hero Banner */}
      <section className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <h1 className="text-4xl md:text-6xl font-bold mb-4">
            Bienvenue sur <span className="text-yellow-300">ShopFlow</span>
          </h1>
          <p className="text-xl text-indigo-100 mb-8 max-w-2xl mx-auto">
            Découvrez des milliers de produits de qualité. Livraison rapide, paiement sécurisé.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              href="/catalogue"
              className="bg-white text-indigo-600 px-8 py-3 rounded-full font-semibold hover:bg-indigo-50 transition-colors flex items-center justify-center space-x-2"
            >
              <span>Explorer le catalogue</span>
              <ArrowRight className="h-5 w-5" />
            </Link>
            <Link
              href="/catalogue?promo=true"
              className="border-2 border-white text-white px-8 py-3 rounded-full font-semibold hover:bg-white/10 transition-colors flex items-center justify-center space-x-2"
            >
              <Tag className="h-5 w-5" />
              <span>Voir les promotions</span>
            </Link>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="bg-white py-10 border-b">
        <div className="max-w-7xl mx-auto px-4">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            {[
              { icon: Truck, title: 'Livraison rapide', desc: 'Dès 5,99€' },
              { icon: Shield, title: 'Paiement sécurisé', desc: '100% sécurisé' },
              { icon: Tag, title: 'Meilleurs prix', desc: 'Garantis' },
              { icon: Zap, title: 'Service client', desc: '7j/7' },
            ].map(({ icon: Icon, title, desc }) => (
              <div key={title} className="flex items-center space-x-3">
                <div className="p-2 bg-indigo-100 rounded-lg">
                  <Icon className="h-6 w-6 text-indigo-600" />
                </div>
                <div>
                  <p className="font-semibold text-sm text-gray-800">{title}</p>
                  <p className="text-xs text-gray-500">{desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Categories */}
      {categories.length > 0 && (
        <section className="py-12 max-w-7xl mx-auto px-4">
          <h2 className="text-2xl font-bold text-gray-800 mb-6">Catégories</h2>
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
            {categories.map((cat) => (
              <Link
                key={cat.id}
                href={`/catalogue?categoryId=${cat.id}`}
                className="bg-white rounded-xl p-4 text-center shadow-sm hover:shadow-md hover:border-indigo-300 border border-gray-100 transition-all"
              >
                <div className="w-12 h-12 bg-indigo-100 rounded-full flex items-center justify-center mx-auto mb-2">
                  <span className="text-xl">🛍️</span>
                </div>
                <p className="text-sm font-medium text-gray-700">{cat.nom}</p>
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Promotions */}
      {promoProducts.length > 0 && (
        <section className="py-12 bg-red-50">
          <div className="max-w-7xl mx-auto px-4">
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-2xl font-bold text-gray-800 flex items-center space-x-2">
                <Tag className="h-6 w-6 text-red-500" />
                <span>Promotions en cours</span>
              </h2>
              <Link href="/catalogue?promo=true" className="text-indigo-600 hover:underline text-sm font-medium flex items-center space-x-1">
                <span>Voir tout</span>
                <ArrowRight className="h-4 w-4" />
              </Link>
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
              {promoProducts.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          </div>
        </section>
      )}

      {/* Top Products */}
      <section className="py-12 max-w-7xl mx-auto px-4">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Meilleures ventes</h2>
          <Link href="/catalogue?sortBy=popularite" className="text-indigo-600 hover:underline text-sm font-medium flex items-center space-x-1">
            <span>Voir tout</span>
            <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
        {loading ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
            {Array.from({ length: 8 }).map((_, i) => <ProductCardSkeleton key={i} />)}
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
            {featuredProducts.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

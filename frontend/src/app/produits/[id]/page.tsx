'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { ShoppingCart, Star, Package, Truck, Shield } from 'lucide-react';
import { productService } from '@/services/productService';
import { reviewService } from '@/services/reviewService';
import { cartService } from '@/services/cartService';
import { useCartStore } from '@/store/cartStore';
import { useAuthStore } from '@/store/authStore';
import { Product, Review, PageResponse, ProductVariant } from '@/types';
import RatingStars from '@/components/ui/RatingStars';
import OrderStatusBadge from '@/components/ui/OrderStatusBadge';
import LoadingSpinner from '@/components/ui/LoadingSpinner';
import toast from 'react-hot-toast';

export default function ProductDetailPage() {
  const { id } = useParams();
  const [product, setProduct] = useState<Product | null>(null);
  const [reviews, setReviews] = useState<PageResponse<Review> | null>(null);
  const [selectedVariant, setSelectedVariant] = useState<ProductVariant | null>(null);
  const [selectedImage, setSelectedImage] = useState(0);
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);
  const [addingToCart, setAddingToCart] = useState(false);
  const [reviewForm, setReviewForm] = useState({ note: 5, commentaire: '' });
  const [submittingReview, setSubmittingReview] = useState(false);

  const { setCart } = useCartStore();
  const { isAuthenticated, user } = useAuthStore();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [prod, revs] = await Promise.all([
          productService.getProduct(Number(id)),
          reviewService.getProductReviews(Number(id)),
        ]);
        setProduct(prod);
        setReviews(revs);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const handleAddToCart = async () => {
    if (!isAuthenticated || user?.role !== 'CUSTOMER') {
      toast.error('Connectez-vous en tant que client');
      return;
    }
    setAddingToCart(true);
    try {
      const cart = await cartService.addItem(product!.id, quantity, selectedVariant?.id);
      setCart(cart);
      toast.success('Ajouté au panier !');
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Erreur');
    } finally {
      setAddingToCart(false);
    }
  };

  const handleSubmitReview = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmittingReview(true);
    try {
      await reviewService.createReview({ productId: product!.id, ...reviewForm });
      toast.success('Avis soumis, en attente de modération');
      setReviewForm({ note: 5, commentaire: '' });
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Erreur');
    } finally {
      setSubmittingReview(false);
    }
  };

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;
  if (!product) return <div className="text-center py-20 text-gray-500">Produit non trouvé</div>;

  const prix = product.enPromotion ? product.prixPromo! : product.prix;
  const stock = selectedVariant
    ? product.stock + selectedVariant.stockSupplementaire
    : product.stock;

  // Group variants by attribute
  const variantsByAttr: Record<string, ProductVariant[]> = {};
  product.variants?.forEach((v) => {
    if (!variantsByAttr[v.attribut]) variantsByAttr[v.attribut] = [];
    variantsByAttr[v.attribut].push(v);
  });

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
        {/* Images */}
        <div>
          <div className="bg-gray-100 rounded-xl overflow-hidden h-96 mb-4">
            <img
              src={product.images?.[selectedImage] || 'https://via.placeholder.com/600x400?text=Produit'}
              alt={product.nom}
              className="w-full h-full object-cover"
            />
          </div>
          {product.images?.length > 1 && (
            <div className="flex space-x-2">
              {product.images.map((img, i) => (
                <button
                  key={i}
                  onClick={() => setSelectedImage(i)}
                  className={`w-16 h-16 rounded-lg overflow-hidden border-2 ${selectedImage === i ? 'border-indigo-600' : 'border-gray-200'}`}
                >
                  <img src={img} alt="" className="w-full h-full object-cover" />
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Product Info */}
        <div className="space-y-4">
          <div>
            {product.categories?.map((cat) => (
              <span key={cat.id} className="text-xs text-indigo-600 font-medium bg-indigo-50 px-2 py-1 rounded-full mr-2">
                {cat.nom}
              </span>
            ))}
          </div>

          <h1 className="text-2xl font-bold text-gray-900">{product.nom}</h1>

          {/* Rating */}
          {product.nombreAvis > 0 && (
            <div className="flex items-center space-x-2">
              <RatingStars rating={product.noteMoyenne} size="md" />
              <span className="text-sm text-gray-600">{product.noteMoyenne.toFixed(1)} ({product.nombreAvis} avis)</span>
            </div>
          )}

          {/* Price */}
          <div className="flex items-baseline space-x-3">
            <span className="text-3xl font-bold text-indigo-600">{prix?.toFixed(3)} TND</span>
            {product.enPromotion && (
              <>
                <span className="text-xl text-gray-400 line-through">{product.prix.toFixed(3)} TND</span>
                <span className="bg-red-100 text-red-600 text-sm font-bold px-2 py-0.5 rounded-full">
                  -{Math.round(product.pourcentageRemise)}%
                </span>
              </>
            )}
          </div>

          {/* Description */}
          {product.description && (
            <p className="text-gray-600 text-sm leading-relaxed">{product.description}</p>
          )}

          {/* Variants */}
          {Object.entries(variantsByAttr).map(([attr, variants]) => (
            <div key={attr}>
              <label className="block text-sm font-medium text-gray-700 mb-2">{attr}</label>
              <div className="flex flex-wrap gap-2">
                {variants.map((v) => (
                  <button
                    key={v.id}
                    onClick={() => setSelectedVariant(selectedVariant?.id === v.id ? null : v)}
                    className={`px-3 py-1.5 border rounded-lg text-sm font-medium transition-colors ${
                      selectedVariant?.id === v.id
                        ? 'border-indigo-600 bg-indigo-50 text-indigo-600'
                        : 'border-gray-300 text-gray-700 hover:border-indigo-400'
                    }`}
                  >
                    {v.valeur}
                    {v.prixDelta > 0 && <span className="text-xs ml-1">(+{v.prixDelta}€)</span>}
                  </button>
                ))}
              </div>
            </div>
          ))}

          {/* Quantity */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Quantité</label>
            <div className="flex items-center space-x-3">
              <button
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                className="w-8 h-8 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50"
              >
                -
              </button>
              <span className="w-8 text-center font-medium text-gray-900">{quantity}</span>
              <button
                onClick={() => setQuantity(Math.min(stock, quantity + 1))}
                className="w-8 h-8 border border-gray-300 rounded-lg flex items-center justify-center hover:bg-gray-50"
              >
                +
              </button>
              <span className="text-sm text-gray-500">({stock} disponibles)</span>
            </div>
          </div>

          {/* Add to Cart */}
          <button
            onClick={handleAddToCart}
            disabled={addingToCart || stock === 0}
            className="w-full bg-indigo-600 text-white py-3 rounded-xl font-semibold hover:bg-indigo-700 disabled:opacity-50 flex items-center justify-center space-x-2 transition-colors"
          >
            <ShoppingCart className="h-5 w-5" />
            <span>{stock === 0 ? 'Rupture de stock' : addingToCart ? 'Ajout...' : 'Ajouter au panier'}</span>
          </button>

          {/* Seller */}
          {product.seller && (
            <div className="border border-gray-200 rounded-xl p-4 flex items-center space-x-3">
              <Package className="h-8 w-8 text-indigo-600" />
              <div>
                <p className="text-sm font-medium text-gray-800">{product.seller.nomBoutique || `${product.seller.prenom} ${product.seller.nom}`}</p>
                <p className="text-xs text-gray-500">Vendeur vérifié</p>
              </div>
            </div>
          )}

          {/* Guarantees */}
          <div className="grid grid-cols-2 gap-3 text-xs text-gray-600">
            <div className="flex items-center space-x-2">
              <Truck className="h-4 w-4 text-green-500" />
              <span>Livraison 5,99€</span>
            </div>
            <div className="flex items-center space-x-2">
              <Shield className="h-4 w-4 text-blue-500" />
              <span>Paiement sécurisé</span>
            </div>
          </div>
        </div>
      </div>

      {/* Reviews */}
      <div className="mt-12">
        <h2 className="text-xl font-bold text-gray-800 mb-6">Avis clients ({reviews?.totalElements || 0})</h2>

        {/* Review Form */}
        {isAuthenticated && user?.role === 'CUSTOMER' && (
          <form onSubmit={handleSubmitReview} className="bg-white rounded-xl shadow-sm p-6 mb-6">
            <h3 className="font-semibold text-gray-800 mb-4">Laisser un avis</h3>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">Note</label>
              <RatingStars
                rating={reviewForm.note}
                size="lg"
                interactive
                onRate={(note) => setReviewForm({ ...reviewForm, note })}
              />
            </div>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">Commentaire</label>
              <textarea
                value={reviewForm.commentaire}
                onChange={(e) => setReviewForm({ ...reviewForm, commentaire: e.target.value })}
                rows={3}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                placeholder="Partagez votre expérience..."
              />
            </div>
            <button
              type="submit"
              disabled={submittingReview}
              className="bg-indigo-600 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-indigo-700 disabled:opacity-50"
            >
              {submittingReview ? 'Envoi...' : 'Publier l\'avis'}
            </button>
          </form>
        )}

        {/* Reviews List */}
        <div className="space-y-4">
          {reviews?.content.length === 0 ? (
            <p className="text-gray-500 text-center py-8">Aucun avis pour ce produit</p>
          ) : (
            reviews?.content.map((review) => (
              <div key={review.id} className="bg-white rounded-xl shadow-sm p-5">
                <div className="flex items-start justify-between mb-2">
                  <div>
                    <p className="font-medium text-gray-800">{review.customerPrenom} {review.customerNom[0]}.</p>
                    <RatingStars rating={review.note} size="sm" />
                  </div>
                  <span className="text-xs text-gray-400">
                    {new Date(review.dateCreation).toLocaleDateString('fr-FR')}
                  </span>
                </div>
                {review.commentaire && <p className="text-sm text-gray-600 mt-2">{review.commentaire}</p>}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

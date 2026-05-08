'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { MapPin, Plus, CheckCircle } from 'lucide-react';
import { cartService } from '@/services/cartService';
import { orderService } from '@/services/orderService';
import api from '@/lib/api';
import { useCartStore } from '@/store/cartStore';
import { useAuthStore } from '@/store/authStore';
import { Address, Cart } from '@/types';
import LoadingSpinner from '@/components/ui/LoadingSpinner';
import toast from 'react-hot-toast';

export default function CheckoutPage() {
  const { cart, setCart, clearCart } = useCartStore();
  const { isAuthenticated, user } = useAuthStore();
  const router = useRouter();

  const [addresses, setAddresses] = useState<Address[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [placing, setPlacing] = useState(false);
  const [showAddressForm, setShowAddressForm] = useState(false);
  const [newAddress, setNewAddress] = useState({ rue: '', ville: '', codePostal: '', pays: 'France', principal: false });

  useEffect(() => {
    if (!isAuthenticated || user?.role !== 'CUSTOMER') {
      router.push('/auth/login');
      return;
    }

    const fetchData = async () => {
      try {
        const [cartData, addrData] = await Promise.all([
          cartService.getCart(),
          api.get('/api/users/me/addresses').then((r) => r.data),
        ]);
        setCart(cartData);
        setAddresses(addrData);
        const principal = addrData.find((a: Address) => a.principal);
        if (principal) setSelectedAddressId(principal.id);
        else if (addrData.length > 0) setSelectedAddressId(addrData[0].id);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [isAuthenticated, user, router, setCart]);

  const handleAddAddress = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await api.post('/api/users/me/addresses', newAddress);
      const addr = response.data;
      setAddresses([...addresses, addr]);
      setSelectedAddressId(addr.id);
      setShowAddressForm(false);
      setNewAddress({ rue: '', ville: '', codePostal: '', pays: 'France', principal: false });
      toast.success('Adresse ajoutée');
    } catch {
      toast.error('Erreur lors de l\'ajout de l\'adresse');
    }
  };

  const handlePlaceOrder = async () => {
    if (!selectedAddressId) {
      toast.error('Veuillez sélectionner une adresse de livraison');
      return;
    }
    setPlacing(true);
    try {
      const order = await orderService.createOrder(selectedAddressId);
      clearCart();
      toast.success('Commande passée avec succès !');
      router.push(`/orders/${order.id}?success=true`);
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      toast.error(error.response?.data?.message || 'Erreur lors de la commande');
    } finally {
      setPlacing(false);
    }
  };

  if (loading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>;

  if (!cart || cart.lignes.length === 0) {
    router.push('/cart');
    return null;
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-800 mb-8">Finaliser la commande</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Delivery Address */}
        <div>
          <h2 className="text-lg font-semibold text-gray-800 mb-4 flex items-center space-x-2">
            <MapPin className="h-5 w-5 text-indigo-600" />
            <span>Adresse de livraison</span>
          </h2>

          <div className="space-y-3">
            {addresses.map((addr) => (
              <label
                key={addr.id}
                className={`block border-2 rounded-xl p-4 cursor-pointer transition-colors ${
                  selectedAddressId === addr.id ? 'border-indigo-600 bg-indigo-50' : 'border-gray-200 hover:border-indigo-300'
                }`}
              >
                <input
                  type="radio"
                  name="address"
                  value={addr.id}
                  checked={selectedAddressId === addr.id}
                  onChange={() => setSelectedAddressId(addr.id)}
                  className="sr-only"
                />
                <div className="flex items-start justify-between">
                  <div>
                    <p className="font-medium text-gray-800">{addr.rue}</p>
                    <p className="text-sm text-gray-600">{addr.codePostal} {addr.ville}, {addr.pays}</p>
                  </div>
                  {selectedAddressId === addr.id && (
                    <CheckCircle className="h-5 w-5 text-indigo-600 flex-shrink-0" />
                  )}
                </div>
                {addr.principal && (
                  <span className="text-xs text-indigo-600 font-medium">Adresse principale</span>
                )}
              </label>
            ))}

            <button
              onClick={() => setShowAddressForm(!showAddressForm)}
              className="w-full border-2 border-dashed border-gray-300 rounded-xl p-4 text-gray-500 hover:border-indigo-400 hover:text-indigo-600 flex items-center justify-center space-x-2 transition-colors"
            >
              <Plus className="h-4 w-4" />
              <span className="text-sm">Ajouter une adresse</span>
            </button>

            {showAddressForm && (
              <form onSubmit={handleAddAddress} className="bg-gray-50 rounded-xl p-4 space-y-3">
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
                <button type="submit" className="w-full bg-indigo-600 text-white py-2 rounded-lg text-sm font-medium hover:bg-indigo-700">
                  Enregistrer
                </button>
              </form>
            )}
          </div>
        </div>

        {/* Order Summary */}
        <div>
          <h2 className="text-lg font-semibold text-gray-800 mb-4">Récapitulatif</h2>
          <div className="bg-white rounded-xl shadow-sm p-5 space-y-3">
            {cart.lignes.map((item) => (
              <div key={item.id} className="flex items-center space-x-3">
                <img
                  src={item.imageProduit || 'https://via.placeholder.com/50x50'}
                  alt={item.nomProduit}
                  className="w-12 h-12 object-cover rounded-lg"
                />
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-gray-800 line-clamp-1">{item.nomProduit}</p>
                  {item.variantInfo && <p className="text-xs text-gray-500">{item.variantInfo}</p>}
                  <p className="text-xs text-gray-500">x{item.quantite}</p>
                </div>
                <p className="text-sm font-semibold text-gray-800">{item.sousTotal.toFixed(2)} TND</p>
              </div>
            ))}

            <div className="border-t pt-3 space-y-2">
              <div className="flex justify-between text-sm text-gray-600">
                <span>Sous-total</span>
                <span>{cart.sousTotal.toFixed(2)} TND</span>
              </div>
              {cart.remiseCoupon > 0 && (
                <div className="flex justify-between text-sm text-green-600">
                  <span>Remise</span>
                  <span>-{cart.remiseCoupon.toFixed(2)} TND</span>
                </div>
              )}
              <div className="flex justify-between text-sm text-gray-600">
                <span>Livraison</span>
                <span>{cart.fraisLivraison.toFixed(2)} TND</span>
              </div>
              <div className="flex justify-between font-bold text-gray-900 text-lg border-t pt-2">
                <span>Total</span>
                <span className="text-indigo-600">{cart.totalTTC.toFixed(2)} TND</span>
              </div>
            </div>

            <button
              onClick={handlePlaceOrder}
              disabled={placing || !selectedAddressId}
              className="w-full bg-indigo-600 text-white py-3 rounded-xl font-semibold hover:bg-indigo-700 disabled:opacity-50 transition-colors mt-4"
            >
              {placing ? 'Traitement...' : `Confirmer la commande — ${cart.totalTTC.toFixed(2)} TND`}
            </button>
            <p className="text-xs text-gray-500 text-center">Paiement simulé — aucune transaction réelle</p>
          </div>
        </div>
      </div>
    </div>
  );
}

'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { FolderTree, Plus, Edit, Trash2, Search } from 'lucide-react';
import api from '@/lib/api';
import toast from 'react-hot-toast';

interface Category {
  id: number;
  nom: string;
  description: string;
  parent?: {
    id: number;
    nom: string;
  };
}

export default function AdminCategoriesPage() {
  const router = useRouter();
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingCategory, setEditingCategory] = useState<Category | null>(null);
  const [formData, setFormData] = useState({
    nom: '',
    description: '',
    parentId: ''
  });

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await api.get('/api/categories');
      setCategories(response.data);
    } catch (error) {
      console.error('Erreur lors du chargement des catégories:', error);
      toast.error('Erreur lors du chargement des catégories');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      const payload = {
        nom: formData.nom,
        description: formData.description,
        parentId: formData.parentId ? parseInt(formData.parentId) : null
      };

      if (editingCategory) {
        await api.put(`/api/categories/${editingCategory.id}`, payload);
        toast.success('Catégorie modifiée avec succès');
      } else {
        await api.post('/api/categories', payload);
        toast.success('Catégorie créée avec succès');
      }

      setShowModal(false);
      setEditingCategory(null);
      setFormData({ nom: '', description: '', parentId: '' });
      fetchCategories();
    } catch (error: any) {
      console.error('Erreur:', error);
      toast.error(error.response?.data?.message || 'Erreur lors de l\'enregistrement');
    }
  };

  const handleEdit = (category: Category) => {
    setEditingCategory(category);
    setFormData({
      nom: category.nom,
      description: category.description,
      parentId: category.parent?.id?.toString() || ''
    });
    setShowModal(true);
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette catégorie ?')) return;

    try {
      await api.delete(`/api/categories/${id}`);
      toast.success('Catégorie supprimée avec succès');
      fetchCategories();
    } catch (error: any) {
      console.error('Erreur:', error);
      toast.error(error.response?.data?.message || 'Erreur lors de la suppression');
    }
  };

  const handleNewCategory = () => {
    setEditingCategory(null);
    setFormData({ nom: '', description: '', parentId: '' });
    setShowModal(true);
  };

  const filteredCategories = categories.filter(cat =>
    cat.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cat.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Séparer les catégories principales et sous-catégories
  const mainCategories = filteredCategories.filter(cat => !cat.parent);
  const subCategories = filteredCategories.filter(cat => cat.parent);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
                <FolderTree className="w-8 h-8 text-indigo-600" />
                Gestion des Catégories
              </h1>
              <p className="mt-2 text-gray-600">
                Gérez les catégories et sous-catégories de produits
              </p>
            </div>
            <button
              onClick={handleNewCategory}
              className="flex items-center gap-2 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors"
            >
              <Plus className="w-5 h-5" />
              Nouvelle Catégorie
            </button>
          </div>
        </div>

        {/* Search */}
        <div className="mb-6">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Rechercher une catégorie..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent text-black"
            />
          </div>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Total Catégories</p>
                <p className="text-2xl font-bold text-gray-900">{categories.length}</p>
              </div>
              <FolderTree className="w-12 h-12 text-indigo-600 opacity-20" />
            </div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Catégories Principales</p>
                <p className="text-2xl font-bold text-gray-900">{mainCategories.length}</p>
              </div>
              <FolderTree className="w-12 h-12 text-green-600 opacity-20" />
            </div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">Sous-catégories</p>
                <p className="text-2xl font-bold text-gray-900">{subCategories.length}</p>
              </div>
              <FolderTree className="w-12 h-12 text-blue-600 opacity-20" />
            </div>
          </div>
        </div>

        {/* Categories List */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Nom
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Description
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Catégorie Parente
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Type
                  </th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredCategories.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="px-6 py-12 text-center text-gray-500">
                      Aucune catégorie trouvée
                    </td>
                  </tr>
                ) : (
                  filteredCategories.map((category) => (
                    <tr key={category.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <FolderTree className={`w-5 h-5 mr-3 ${category.parent ? 'text-blue-600' : 'text-indigo-600'}`} />
                          <span className="text-sm font-medium text-gray-900">{category.nom}</span>
                        </div>
                      </td>
                      <td className="px-6 py-4">
                        <span className="text-sm text-gray-600">{category.description}</span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {category.parent ? (
                          <span className="text-sm text-gray-900">{category.parent.nom}</span>
                        ) : (
                          <span className="text-sm text-gray-400">-</span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {category.parent ? (
                          <span className="px-2 py-1 text-xs font-medium bg-blue-100 text-blue-800 rounded-full">
                            Sous-catégorie
                          </span>
                        ) : (
                          <span className="px-2 py-1 text-xs font-medium bg-indigo-100 text-indigo-800 rounded-full">
                            Principale
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                        <button
                          onClick={() => handleEdit(category)}
                          className="text-indigo-600 hover:text-indigo-900 mr-4"
                        >
                          <Edit className="w-5 h-5" />
                        </button>
                        <button
                          onClick={() => handleDelete(category.id)}
                          className="text-red-600 hover:text-red-900"
                        >
                          <Trash2 className="w-5 h-5" />
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg max-w-md w-full p-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">
              {editingCategory ? 'Modifier la Catégorie' : 'Nouvelle Catégorie'}
            </h2>
            <form onSubmit={handleSubmit}>
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Nom *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.nom}
                    onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent text-black"
                    placeholder="Ex: Électronique"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Description *
                  </label>
                  <textarea
                    required
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent text-black"
                    placeholder="Description de la catégorie"
                    rows={3}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Catégorie Parente (optionnel)
                  </label>
                  <select
                    value={formData.parentId}
                    onChange={(e) => setFormData({ ...formData, parentId: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent text-black"
                  >
                    <option value="">Aucune (Catégorie principale)</option>
                    {mainCategories.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.nom}
                      </option>
                    ))}
                  </select>
                  <p className="mt-1 text-xs text-gray-500">
                    Sélectionnez une catégorie parente pour créer une sous-catégorie
                  </p>
                </div>
              </div>

              <div className="flex gap-3 mt-6">
                <button
                  type="button"
                  onClick={() => {
                    setShowModal(false);
                    setEditingCategory(null);
                    setFormData({ nom: '', description: '', parentId: '' });
                  }}
                  className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                >
                  Annuler
                </button>
                <button
                  type="submit"
                  className="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors"
                >
                  {editingCategory ? 'Modifier' : 'Créer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

import { OrderStatus } from '@/types';

interface OrderStatusBadgeProps {
  status: OrderStatus;
}

const statusConfig: Record<OrderStatus, { label: string; className: string }> = {
  PENDING: { label: 'En attente', className: 'bg-yellow-100 text-yellow-800' },
  PAID: { label: 'Payé', className: 'bg-blue-100 text-blue-800' },
  PROCESSING: { label: 'En traitement', className: 'bg-purple-100 text-purple-800' },
  SHIPPED: { label: 'Expédié', className: 'bg-indigo-100 text-indigo-800' },
  DELIVERED: { label: 'Livré', className: 'bg-green-100 text-green-800' },
  CANCELLED: { label: 'Annulé', className: 'bg-red-100 text-red-800' },
  REFUNDED: { label: 'Remboursé', className: 'bg-gray-100 text-gray-800' },
};

export default function OrderStatusBadge({ status }: OrderStatusBadgeProps) {
  const config = statusConfig[status] || { label: status, className: 'bg-gray-100 text-gray-800' };

  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${config.className}`}>
      {config.label}
    </span>
  );
}

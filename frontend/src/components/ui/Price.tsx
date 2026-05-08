import React from 'react';

interface PriceProps {
  amount: number;
  className?: string;
  showCurrency?: boolean;
}

export default function Price({ amount, className = '', showCurrency = true }: PriceProps) {
  const formatted = amount.toFixed(3);
  
  return (
    <span className={className}>
      {formatted} {showCurrency && 'TND'}
    </span>
  );
}

interface PriceRangeProps {
  min: number;
  max: number;
  className?: string;
}

export function PriceRange({ min, max, className = '' }: PriceRangeProps) {
  return (
    <span className={className}>
      {min.toFixed(3)} - {max.toFixed(3)} TND
    </span>
  );
}

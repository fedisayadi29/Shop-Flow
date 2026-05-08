'use client';

import { Star } from 'lucide-react';

interface RatingStarsProps {
  rating: number;
  maxRating?: number;
  size?: 'sm' | 'md' | 'lg';
  interactive?: boolean;
  onRate?: (rating: number) => void;
}

export default function RatingStars({
  rating,
  maxRating = 5,
  size = 'md',
  interactive = false,
  onRate,
}: RatingStarsProps) {
  const sizeClass = {
    sm: 'h-3 w-3',
    md: 'h-4 w-4',
    lg: 'h-6 w-6',
  }[size];

  return (
    <div className="flex items-center space-x-0.5">
      {Array.from({ length: maxRating }, (_, i) => i + 1).map((star) => (
        <button
          key={star}
          type="button"
          disabled={!interactive}
          onClick={() => interactive && onRate?.(star)}
          className={interactive ? 'cursor-pointer hover:scale-110 transition-transform' : 'cursor-default'}
        >
          <Star
            className={`${sizeClass} ${
              star <= rating ? 'text-yellow-400 fill-yellow-400' : 'text-gray-300'
            }`}
          />
        </button>
      ))}
    </div>
  );
}

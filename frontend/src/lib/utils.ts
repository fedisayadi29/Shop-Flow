/**
 * Formate un prix en dinars tunisiens (TND)
 * @param price - Le prix à formater
 * @returns Le prix formaté avec le symbole TND
 */
export function formatPrice(price: number): string {
  return `${price.toFixed(3)} TND`;
}

/**
 * Formate un prix avec 2 décimales (pour compatibilité)
 * @param price - Le prix à formater
 * @returns Le prix formaté avec 2 décimales et TND
 */
export function formatPrice2(price: number): string {
  return `${price.toFixed(2)} TND`;
}

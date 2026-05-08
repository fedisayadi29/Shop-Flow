// Constantes pour la Tunisie

export const GOUVERNORATS = [
  'Tunis',
  'Ariana',
  'Ben Arous',
  'Manouba',
  'Nabeul',
  'Zaghouan',
  'Bizerte',
  'Béja',
  'Jendouba',
  'Le Kef',
  'Siliana',
  'Kairouan',
  'Kasserine',
  'Sidi Bouzid',
  'Sousse',
  'Monastir',
  'Mahdia',
  'Sfax',
  'Gabès',
  'Médenine',
  'Tataouine',
  'Gafsa',
  'Tozeur',
  'Kebili',
];

export const VILLES_PAR_GOUVERNORAT: Record<string, string[]> = {
  'Tunis': ['Tunis', 'La Marsa', 'Carthage', 'Sidi Bou Said', 'Le Bardo', 'La Goulette'],
  'Ariana': ['Ariana', 'Ettadhamen', 'Raoued', 'Soukra', 'Mnihla'],
  'Ben Arous': ['Ben Arous', 'Hammam Lif', 'Radès', 'Mégrine', 'Mohamedia', 'Ezzahra'],
  'Manouba': ['Manouba', 'Oued Ellil', 'Douar Hicher', 'Tebourba'],
  'Nabeul': ['Nabeul', 'Hammamet', 'Kelibia', 'Korba', 'Menzel Temime', 'Grombalia'],
  'Zaghouan': ['Zaghouan', 'El Fahs', 'Bir Mcherga'],
  'Bizerte': ['Bizerte', 'Menzel Bourguiba', 'Mateur', 'Ras Jebel', 'Sejnane'],
  'Béja': ['Béja', 'Medjez el-Bab', 'Testour', 'Teboursouk'],
  'Jendouba': ['Jendouba', 'Tabarka', 'Aïn Draham', 'Fernana'],
  'Le Kef': ['Le Kef', 'Dahmani', 'Tajerouine', 'Nebeur'],
  'Siliana': ['Siliana', 'Makthar', 'Bou Arada', 'Rouhia'],
  'Kairouan': ['Kairouan', 'Haffouz', 'Sbikha', 'Nasrallah'],
  'Kasserine': ['Kasserine', 'Sbeitla', 'Feriana', 'Thala'],
  'Sidi Bouzid': ['Sidi Bouzid', 'Regueb', 'Meknassy', 'Menzel Bouzaiane'],
  'Sousse': ['Sousse', 'Msaken', 'Kalaa Kebira', 'Hammam Sousse', 'Akouda'],
  'Monastir': ['Monastir', 'Moknine', 'Jemmal', 'Ksar Hellal', 'Teboulba'],
  'Mahdia': ['Mahdia', 'Ksour Essef', 'El Jem', 'Chebba'],
  'Sfax': ['Sfax', 'Sakiet Ezzit', 'Sakiet Eddaier', 'Agareb', 'Jebiniana'],
  'Gabès': ['Gabès', 'Mareth', 'Matmata', 'El Hamma'],
  'Médenine': ['Médenine', 'Djerba', 'Zarzis', 'Ben Gardane', 'Houmt Souk'],
  'Tataouine': ['Tataouine', 'Ghomrassen', 'Remada'],
  'Gafsa': ['Gafsa', 'Metlaoui', 'Redeyef', 'Mdhilla'],
  'Tozeur': ['Tozeur', 'Nefta', 'Degache'],
  'Kebili': ['Kebili', 'Douz', 'Souk Lahad'],
};

export const CURRENCY = {
  code: 'TND',
  symbol: 'TND',
  name: 'Dinar Tunisien',
  decimals: 3,
};

export const PHONE_PREFIX = '+216';

export const formatPrice = (price: number): string => {
  return `${price.toFixed(3)} ${CURRENCY.symbol}`;
};

export const formatPhoneNumber = (phone: string): string => {
  // Format: +216 XX XXX XXX
  const cleaned = phone.replace(/\D/g, '');
  if (cleaned.startsWith('216')) {
    const number = cleaned.substring(3);
    return `+216 ${number.substring(0, 2)} ${number.substring(2, 5)} ${number.substring(5)}`;
  }
  return phone;
};

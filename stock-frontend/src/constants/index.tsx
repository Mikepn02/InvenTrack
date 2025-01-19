import { FaHome, FaShoppingBag, FaShoppingCart, FaChartBar, FaTruck } from 'react-icons/fa';

export const navigation = [
  {
    name: 'Overview',
    icon: FaHome,
    path: '/dashboard',
  },
  {
    name: 'Inventory',
    icon: FaShoppingBag,
    path: '/dashboard/stock',
  },
  {
    name: 'Orders',
    icon: FaShoppingCart,
    path: '/dashboard/order',
  },
  {
    name: 'Suppliers',
    icon: FaTruck,
    path: '/dashboard/supplier',
  },
  {
    name: 'Analytics',
    icon: FaChartBar,
    path: '/dashboard/analytics',
  },
];



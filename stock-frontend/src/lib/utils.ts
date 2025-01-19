import { type ClassValue, clsx } from "clsx";
import dayjs from 'dayjs';
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function encryptKey(passkey: string) {
  return btoa(passkey);
}

export function decryptKey(passkey: string) {
  return atob(passkey);
}


export const getLast30DaysRange = () => {
  const endDate = dayjs().format('YYYY-MM-DD');
  const startDate = dayjs().subtract(30, 'day').format('YYYY-MM-DD');
  return { startDate, endDate };
};

export const setCookie = (name: string, value: string, days: number) => {
  const date = new Date();
  date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
  const expires = `expires=${date.toUTCString()}`;
  document.cookie = `${name}=${value};${expires};path=/`;
};

export const getCookie = (name: string) => {
  const cookieName = `${name}=`;
  const cookies = document.cookie.split(";");
  for (let i = 0; i < cookies.length; i++) {
    let cookie = cookies[i];
    while (cookie.charAt(0) === " ") {
      cookie = cookie.substring(1);
    }
    if (cookie.indexOf(cookieName) === 0) {
      return cookie.substring(cookieName.length, cookie.length);
    }
  }
  return "";
};

export const deleteCookie = (name: string) => {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
};

export const getGreeting = (): string => {
  const currentHour = new Date().getHours();

  if (currentHour < 12) {
    return "Good Morning";
  } else if (currentHour < 18) {
    return "Good Afternoon";
  } else {
    return "Good Evening";
  }
};

export const getDate = (): string => {
  const currentDate = new Date();
  const options: Intl.DateTimeFormatOptions = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  };

  return currentDate.toLocaleDateString(undefined, options);
};
export const getObjValue = (key: string | number, obj: any) => {
  const keys = key.toString().split('.');
  let result = obj;
  for (const key of keys) {
      if (result && Object.prototype.hasOwnProperty.call(result, key)) {
          result = result[key];
      } else {
          return undefined;
      }
  }
  return result as string;
};

export const getStockStatus = (quantity: number | string): string => {
  const numericQuantity = typeof quantity === "string" ? parseFloat(quantity) : quantity;
  
  if (numericQuantity > 10) {
    return "In Stock";
  } else if (numericQuantity > 5) {
    return "Low Stock";
  } else if (numericQuantity > 0) {
    return "Critically Low";
  } else {
    return "Out of Stock";
  }
};

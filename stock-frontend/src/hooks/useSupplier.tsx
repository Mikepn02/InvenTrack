import axios from "../lib/axios.config";
import useSWR from "swr";
import { Supplier } from "../types";

export default function useSupplier() {
  const {
    data: suppliers,
    isLoading,
    error,
    mutate,
  } = useSWR<Supplier[]>("/suppliers", async (url: string) => {
    const { data } = await axios.get(url);

    console.log("Here are all suppliers: ", data);
    return data;
  });

  const createSupplier = async (
    supplier: Omit<Supplier, "id" | "itemsNames">
  ) => {
    try {
      const { data } = await axios.post("/suppliers", supplier);
      mutate([...suppliers!, data], false);
    } catch (error) {
      console.error("Failed to create new stock item: ", error);
    }
  };

  return {
    suppliers,
    createSupplier,
    isLoading,
    error,
  };
}

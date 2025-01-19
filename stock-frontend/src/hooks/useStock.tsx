import useSWR from "swr";
import { StockItem } from "../types";
import axios from "../lib/axios.config";
import { notifications } from "@mantine/notifications";

export default function useStock() {
  const {
    data: items,
    isLoading,
    error,
    mutate,
  } = useSWR<StockItem[]>("/stock", async (url: string) => {
    const { data } = await axios.get(url);
    return data.map((item: { stock: StockItem }) => item.stock);
  });

  const {
    data: topConsumedProducts,
    isLoading: isTopConsumedLoading,
    error: topConsumedError,
  } = useSWR<StockItem[]>("/stock/top-consumed", async (url: string) => {
    const { data } = await axios.get(url);
    return data.map((item: { stock: StockItem }) => item.stock);
  });

  const createItem = async (newItem: Omit<StockItem, "id">) => {
    try {
      const { data } = await axios.post("/stock", newItem);
      mutate([...items!, data], false);
    } catch (error) {
      console.error("Failed to create new stock item: ", error);
    }
  };

  const updateItem = async (
    productId: string,
    updatedItem: Partial<StockItem>
  ) => {
    try {
      const { data } = await axios.put(`/stock/${productId}`, updatedItem);
      mutate(
        items?.map((item) => (item.id === productId ? data : item)),
        false
      );
    } catch (error) {
      console.error("Failed to update the Item: ", error);
    }
  };

  const deleteItem = async (productId: string) => {
    try {
      await axios.delete(`/stock/${productId}`);
      mutate(
        items?.filter((item) => item.id !== productId),
        false
      );
    } catch (error) {
      console.error("Failed to delete an item in the stock: ", error);
    }
  };

  const consumeStock = async (productId: string, consumedAmount: number) => {
    try {
      const { data } = await axios.post(`/stock/${productId}/consume`, {
        consumedAmount,
      });
      mutate(
        items?.map((item) =>
          item.id === productId ? { ...item, quantity: data.quantity } : item
        ),
        false
      );
      mutate();

      notifications.show({
        title: "Success",
        message: "Consumed the stock successfully",
        color: "green",
      });
    } catch (error: any) {
      console.error(
        "Failed to consume product from the stock: ",
        error?.message
      );
      notifications.show({
        title: "Error",
        message: "Failed to consume the stock",
        color: "red",
      });
    }
  };

  const restock = async (productId: string, additionalQuantity: number) => {
    try {
      const { data } = await axios.post(`/stock/restock/${productId}`, {
        additionalQuantity,
      });
      mutate(
        items?.map((item) =>
          item.id === productId ? { ...item, quantity: data.quantity } : item
        ),
        false
      );
      mutate();
      notifications.show({
        title: "Success",
        message: "Restocked The Item successfully",
        color: "green",
      });
    } catch (error) {
      console.log("Failed to restock item");
      notifications.show({
        title: "Error",
        message: "Failed to restock the item",
        color: "red",
      });
    }
  };

  return {
    stock: items,
    createItem,
    updateItem,
    deleteItem,
    consumeStock,
    restock,
    topConsumedProducts,
    isLoading,
    isTopConsumedLoading,
    error,
    topConsumedError,
  };
}

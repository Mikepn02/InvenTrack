import useSWR from "swr";
import axios from "../lib/axios.config";
import { Order } from "../types";


export default function useOrder(){
    const { data: orders , isLoading , error , mutate} = useSWR<Order[]>("/orders", async (url: string) => {
        const { data} = await axios.get(url);
        return data;
    });

    const createOrder = async( newOrder: Omit<Order , "id">) => {
        try{
            const { data } = await axios.post("/orders", newOrder)
            mutate([...orders!, data], false);
        }catch(error){
            console.error("Failed to create new Order: ", error)
        }
    }

    const updateOrder = async(orderId: string, updatedOrder: Partial<Order>) => {
        try{
            const { data } = await axios.put(`/orders/${orderId}`, updatedOrder);
            mutate(orders?.map(order => (order.id === orderId ? data : order)), false);
        }catch(error){
            console.error("Failed to update the order: ", error)
        }
    }

    const deleteOrder = async (orderId: string) => {
        try {
          await axios.delete(`/orders/${orderId}`);
          mutate(orders?.filter(order => order.id !== orderId), false);
        } catch (error) {
          console.error("Failed to delete order:", error);
        }
      };
    return {
       orders,
       createOrder,
       updateOrder,
       deleteOrder,
       isLoading,
       error
    }
}
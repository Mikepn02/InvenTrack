import { useState, useEffect } from "react";
import useSWR from "swr";
import { Client, Frame, Message } from "@stomp/stompjs"; // Use Client, Frame, and Message from stompjs
import { Notification } from "../types"; // Assuming you have a Notification type
import axios from "../lib/axios.config";
import { getCookie } from "../lib/utils";
import { notifications } from "@mantine/notifications";
import useAuth from "./useAuth";

const WEBSOCKET_URL = `http://localhost:8080/api/v1/ws`;

export default function useNotification() {
  const { user } = useAuth();
  const [socketNotifications, setSocketNotifications] = useState<Notification[]>([]);
  const token = getCookie("token");

  const { data: apiNotifications, isLoading, error, mutate } = useSWR<Notification[]>("/notify", async (url: string) => {
    const { data } = await axios.get(url);
    return data;
  });

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: WEBSOCKET_URL,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      reconnectDelay: 5000,
      onConnect: (frame: Frame) => {
        console.log("Connected to STOMP WebSocket:", frame);
        stompClient.subscribe(`/user/${user?.id}/notification`, (message: Message) => {
          console.log("Raw STOMP message received:", message.body);

          try {
            const notification = JSON.parse(message.body) as Notification;
            console.log("Parsed notification: ", notification);

            mutate((currentNotifications: Notification[] = []) => {
              return [...currentNotifications, notification];
            }, false);

            notifications.show({
              title: "New Notification",
              color: "blue",
              message: notification.message,
            });
          } catch (error) {
            console.error("Error parsing WebSocket message: ", error);
          }
        });
      },
      onStompError: (error) => {
        console.error("STOMP WebSocket error: ", error);
      },
    });

    stompClient.activate(); 

    return () => {
      stompClient.deactivate();
    };
  }, [token, mutate, user?.id]);

  const markAsRead = async (id: string) => {
    try {
      const { data } = await axios.put(`/notify/read/${id}`);
      notifications.show({
        title: "Success",
        color: "green",
        message: "Successfully marked as read",
      });
      mutate();
      return data;
    } catch (error: any) {
      notifications.show({
        title: "Error",
        color: "red",
        message: error?.message,
      });
    }
  };

  return {
    notifications: [...(apiNotifications || []), ...socketNotifications],
    isLoading,
    error,
    markAsRead
  };
}

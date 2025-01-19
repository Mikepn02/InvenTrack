"use client";


import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "../../ui/dialog";
import useOrder from "../../../hooks/useOrder";
import { Button } from "@mantine/core";

interface ConfirmationModalProps {
  type: "delete" | "update";
  orderId: string;
  onSuccess?: () => void;
  orderDetails?: { name?: string; status?: string };
  open: boolean;
  setOpen: (open: boolean) => void;
}

const ConfirmationModal = ({
  type,
  orderId,
  onSuccess,
  orderDetails,
  open,
  setOpen,
}: ConfirmationModalProps) => {
  const { deleteOrder, orders, updateOrder, isLoading } = useOrder();

  const handleSubmit = async () => {
    try {
      if (type === "delete") {
        await deleteOrder(orderId);
      } else if (type === "update" && orderDetails) {
        const existingOrder = orders?.find((order) => order.id === orderId);
        if (existingOrder) {
          await updateOrder(orderId, orderDetails);
        } else {
          console.error("Order does not exist, cannot update.");
        }
      }

      if (onSuccess) onSuccess();
      setOpen(false);
    } catch (error) {
      console.error(`Error while ${type}ing order:`, error);
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogContent>
        <DialogHeader className="mb-4 space-y-3">
          <DialogTitle>
            {type === "delete" ? "Delete" : "Update"} Order
          </DialogTitle>
          <DialogDescription>
            Are you sure you want to {type} the order with ID: {orderId}?
          </DialogDescription>
        </DialogHeader>
        <div className="flex justify-end space-x-2">
          <Button
            onClick={handleSubmit}
            className={`cursor-pointer ${
              type === "delete"
                ? "border border-gray-900 bg-white text-gray-900 hover:bg-red-500 hover:border-none"
                : "bg-gray-900 hover:bg-white hover:border hover:border-gray-900 hover:text-gray-900"
            }`}
          >
            {isLoading ? "Loading..." : type === "delete" ? "Delete" : "Update"}
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default ConfirmationModal;
